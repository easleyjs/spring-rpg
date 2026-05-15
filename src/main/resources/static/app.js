let username = "";
let password = "";
let character = {};
let isNewUser = false;
let newCharacterName = "";

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
        console.log(character);

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
        login(username, password);

        term.write("\r\n");
        term.write(inputMenu());
    }

    if (cmd === "start") {
        const res = await fetch("/combat/create", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ playerId: 1 })
        });

        const data = await res.json();
        window.currentEncounterId = data.encounterId;

        term.writeln(`Encounter started. Monster HP: ${data.monsterHp}`);
    }

    if (cmd === "attack") {
        const res = await fetch("/combat/attack", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ encounterId: window.currentEncounterId })
        });

        const data = await res.json();

        term.writeln(data.message);
        term.writeln(`HP: ${data.playerHp} | Monster: ${data.monsterHp}`);
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


// TODO: writeScreen/do the log of commands/messages. Clear screen, write those, then write menu

// TODO: getInventory function
// TODO: startCombat function
// TODO: attack function
// TODO: shop function (get list of items)
// TODO: buy function
// TODO: turns? add to input menu

function printLog() {
    for (let i = 0; i < (term.rows - 1); i++) {
        term.write('\r\n');
    }
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