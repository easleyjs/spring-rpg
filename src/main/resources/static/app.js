let username, password, newCharacterName, monsterName = "";
let character, commands, encounter = {};
let isNewUser, isInCombat = false;


const townCommands = {
    "F": "Enter Forest",
    "S": "Enter Shop",
    "I": "View Inventory",
}

const forestCommands = {
    "A": "Attack",
    "U": "Use Item",
    "R": "Return to Town"
}

const shopCommands = {
    "B": "Buy Item",
    "R": "Return to Town"
}

const term = new Terminal();
term.open(document.getElementById('terminal'));

term.writeln("Welcome to SpringRPG");
term.writeln(`Please log in or enter "new" to create a new account.`);

term.write("Username: ");

let input = "";

term.onData(e => {
    if (e === '\x7F') { // backspace
        if (input.length > 0) {
            input = input.slice(0, -1);
            term.write('\b \b'); // erase character visually
        }
    }
    if (e === '\r') {
        if (!username && !isNewUser && input !== "new") {
            handleCommand("inputUsername");

        } else if (!password && !isNewUser && input !== "new") {
            handleCommand("inputPassword");
            term.write("\r\n");

        } else if (input === "new" && !username && !password) {
            isNewUser = true;
            term.write("\r\n");
            handleCommand("newUsernamePrompt");

        } else if (isNewUser && !username) {
            handleCommand("setNewUsername");

        } else if (isNewUser && !password) {
            handleCommand("setNewUserPassword");

        } else if (isNewUser && username && password) {
            handleCommand("setNewCharacterName");

        } else {
            handleCommand(input);
            term.write("\r\n");

        }
        input = "";
    } else {
        input += e;
        if (username && !password) {
            term.write("*");
        } else {
            term.write(e);
        }
    }
});

async function handleCommand(cmd) {
    if (cmd === "inputUsername") {
        username = input;
        term.write("\r\n");
        console.log("Existing user: " + username);

        term.write("Password: ");
    }

    if (cmd === "inputPassword") {
        password = input;
        await login(username, password);
        character = await getCharacter();

        term.clear();

        term.writeln("Welcome, " + character.name + ".");
        term.writeln("You are in town.");
        term.writeln(commandList(townCommands));
        term.write(inputMenu());
    }

    if (cmd === "newUsernamePrompt") {
        console.log("New user creation");

        term.clear();
        term.write("Username (used for login only): ");
    }
    if (cmd === "setNewUsername") {
        username = input;

        console.log("New username: " + username);

        term.write("\r\n");
        term.write("Password: ");
    }

    if (cmd === "setNewUserPassword") {
        password = input;
        console.log("New Password: " + password);

        term.write("\r\n");
        term.write("Character Name: ");
    }

    if (cmd === "setNewCharacterName") {
        newCharacterName = input;

        const res = await createCharacter(input);
        await login(username, password);

        term.write("\r\n");
        term.write(inputMenu());
    }

    if (cmd === "F" || cmd === "f") {
        // Move character to Forest so combat can begin.
        const locChangeResult = await changePlayerLocation("FOREST");

        encounter = await startCombat();

        isInCombat = true;
        commands = forestCommands; // swap the active command map

        pushLog(color("You venture into the forest...", "2;37"));
        pushLog(color(`A ${encounter.monsterName} appears!`, "1;31"));
    }
    //TODO: Add "damage" attribute to returned inventory list so player can evaluate

    if (cmd === "I") {
        const data = await getInventory();
        console.log(data);

        pushLog("Inventory test.");
    }

    if (cmd === "A" && isInCombat) {
        const data = await makeAttack();
        console.log(data);

        character.health = data.playerHp;

        data.messages.forEach(message => {
            pushLog(message);
        });


        if (data.status === "WON") {
            encounter = await startCombat();

            pushLog("");
            pushLog(color(`A ${encounter.monsterName} appears!`, "1;31"));
        }

        if (data.status === "LOST") {
            await changePlayerLocation("TOWN");
            isInCombat = false;
            commands = townCommands;
            pushLog("You have been returned to town.");
        }
    }
}

async function login(username, password) {
    const res = await fetch("/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });

    const data = await res.json();

    localStorage.setItem("rpg-token", data.token);
}

async function createCharacter( characterName ) {
    const res = await fetch("/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" }, // use token helper
        body: JSON.stringify({
            username: username,
            password: password,
            characterName: characterName
        })
    });
    return res.json();
}

async function getCharacter() {
    const res = await fetch("/characters/me", {
        headers: authHeaders()
    });

    return res.json();
}

function authHeaders() {
    const token = localStorage.getItem("rpg-token");

    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
    };
}

async function changePlayerLocation( location ) {
    const res = await fetch("/characters/move", {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify({"location": location })
    });

    return res.json();
}

async function startCombat() {
    const res = await fetch("/combat/create", {
        method: "POST",
        headers: authHeaders()
    });

    return res.json();
}

async function makeAttack() {
    const res = await fetch("/combat/attack", {
        method: "POST",
        headers: authHeaders()
    });

    return res.json();
}

async function getInventory() {
    const res = await fetch("/inventory/me", {
        method: "GET",
        headers: authHeaders()
    });

    return res.json();
}

// TODO: getInventory function
// TODO: shop function (get list of items)
// TODO: buy function
// TODO: turns? add to input menu

const LOG_SIZE = 200;
const logBuffer = [];

function pushLog(line) {
    logBuffer.push(line);
    if (logBuffer.length > LOG_SIZE) logBuffer.shift();
    renderScreen();
}

function renderScreen() {
    const logRows = term.rows - 2; // rows available for log (1 row = commands, 1 = input)

    // Move to top-left and clear
    term.write('\x1b[H\x1b[2J');

    // Take the last N entries that fit
    const visible = logBuffer.slice(-logRows);

    // Pad top with empty lines so content sticks to the bottom of the log area
    const padding = logRows - visible.length;
    for (let i = 0; i < padding; i++) {
        term.write('\r\n');
    }

    for (const line of visible) {
        term.write(line + '\r\n');
    }

    // Status bar — always last two lines
    term.write(commandList(commands) + '\r\n');
    term.write(inputMenu());
}

function color(text, code) {
    return `\x1b[${code}m${text}\x1b[0m`;
}

function commandList(commands) {
    let commandString = "";

    for (const [key, value] of Object.entries(commands)) {
        commandString += `(` + color(key,"1;32") + `) ${value} `;
    }

    return commandString;
}

function inputMenu() {
    return "[" + character.name + "]"
        + ` (HP: ` + color(character.health, 32) + `)`
        + ` Level: ` + color(character.level, 33) + ` `
        + ` Gold: ` + color(character.gold, 33) + ` `
        + `): `;
}