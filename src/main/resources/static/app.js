let username, password, newCharacterName, monsterName = "";
let character, commands, encounter = {};
let currentInventory = [];
let isNewUser, isInCombat, isInInventory = false;

let inputMode = "command";

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

//TODO:     U: "Use Item",
const inventoryCommands = {
    E: "Equip Item",
    D: "Drop Item",
    B: "Back",
};

const inventoryItemCommands = {
    "#": "Equip Item #",
    B: "Back",
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
        location = "forest";

        encounter = await startCombat();

        isInCombat = true;
        commands = forestCommands; // swap the active command map

        pushLog(color("You venture into the forest...", "2;37"));
        pushLog(color(`A ${encounter.monsterName} appears!`, "1;31"));
    }

    if (inputMode === "equipItem") {
        if (cmd === "B") {
            inputMode = "inventory";
            commands = inventoryCommands;
            pushLog("Back to inventory.");
            return;
        }

        const itemNumber = Number(cmd);

        if (!Number.isInteger(itemNumber) || itemNumber < 1 || itemNumber > currentInventory.length) {
            pushLog("Invalid item number.");
            return;
        }

        const selectedItem = currentInventory[itemNumber - 1];

        await equipItem(selectedItem.id);

        inputMode = "inventory";
        commands = inventoryCommands;
        pushLog(`Equipped ${selectedItem.itemName}.`);
        return;
    }
    if (cmd === "I") {
        const data = await getInventory();

        currentInventory = data;

        const tableTitle = "Inventory";
        const tableCols = [
            "#",
            "Item Name",
            "Type",
            "Damage/Reduction",
            "Quantity",
            "Equipped"
        ];

        const tableRows = currentInventory.map((item, idx) => {
            return [
                idx + 1,
                item.itemName,
                item.itemType,
                item.damage,
                item.quantity,
                item.equipped
            ];
        });

        invTable = asciiTable(tableTitle, tableCols, tableRows);

        isInInventory = true;
        inputMode = "inventory";
        commands = inventoryCommands;

        pushLogLines(invTable);
        return;
    }

    if (cmd === "E" && isInInventory) {
        inputMode = "equipItem";
        commands = { B: "Back" };
        pushLog("Which item would you like to equip? Enter item #, or B to go back.");
        return;
    }

    if (cmd === "B" && isInInventory) {
        inputMode = "town";
        isInInventory = false;
        commands = townCommands;
        pushLog("You are in town.");

        return;
    }

    if (cmd === "A" && isInCombat) {
        const data = await makeAttack();

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

async function equipItem(invItemId) {
    const res = await fetch("/inventory/equip", {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify({
            itemId: invItemId
        })
    });

    return res.json();
}

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
    const logRows = term.rows - 2;

    // Clear whole screen
    term.write('\x1b[H\x1b[2J');

    const visible = logBuffer.slice(-logRows);
    const padding = logRows - visible.length;

    // Draw log area
    for (let i = 0; i < padding; i++) {
        term.write('\r\n');
    }

    for (const line of visible) {
        term.write('\r' + line + '\x1b[K\r\n');
    }

    // Draw command/status line explicitly on second-to-last row
    term.write(`\x1b[${term.rows - 1};1H`);
    term.write('\x1b[K');
    term.write(commandList(commands));

    // Draw input prompt explicitly on last row
    term.write(`\x1b[${term.rows};1H`);
    term.write('\x1b[K');
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

function pushLogLines(lines) {
    for (const line of lines) {
        logBuffer.push(line);
        if (logBuffer.length > LOG_SIZE) logBuffer.shift();
    }

    renderScreen();
}

function stripAnsi(str) {
    return String(str).replace(/\x1b\[[0-9;]*m/g, "");
}

function visibleLength(str) {
    return stripAnsi(str).length;
}

function padRight(str, width) {
    const diff = width - visibleLength(str);
    return String(str) + " ".repeat(Math.max(0, diff));
}

function centerText(str, width) {
    const len = visibleLength(str);
    const totalPadding = Math.max(0, width - len);
    const left = Math.floor(totalPadding / 2);
    const right = totalPadding - left;
    return " ".repeat(left) + str + " ".repeat(right);
}

function makeBorder(widths) {
    return "+" + widths.map(w => "-".repeat(w + 2)).join("+") + "+";
}

function makeRow(values, widths) {
    return "|" + values.map((value, i) => {
        return " " + padRight(value, widths[i]) + " ";
    }).join("|") + "|";
}

function asciiTable(title, columns, rows) {

    const stringRows = rows.map(row =>
        row.map(value => value == null ? "" : String(value))
    );

    const widths = columns.map((col, i) => {
        const columnWidth = visibleLength(col);

        const rowWidth = stringRows.reduce((max, row) => {
            return Math.max(max, visibleLength(row[i] ?? ""));
        }, 0);

        return Math.max(columnWidth, rowWidth);
    });

    const border = makeBorder(widths);
    const tableWidth = visibleLength(border);

    const titleLine = "|" + centerText(title, tableWidth - 2) + "|";
    const headerLine = makeRow(columns, widths);

    return [
        border,
        titleLine,
        border,
        headerLine,
        border,
        ...stringRows.map(row => makeRow(row, widths)),
        border
    ];
}