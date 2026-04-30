let username = "";
let password = "";
let character = {};
let isNewUser = false;
let newCharacterName = "";

const term = new Terminal();
term.open(document.getElementById('terminal'));

term.writeln("Welcome to SpringRPG");
term.writeln(`Please log in or enter "new" to create a new account.`);
// TODO: "new" or "login"
term.write("Username: ");
//handleCommand("inputUsername");

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

        for (let i = 0; i < (term.rows - 1); i++) {
            term.write('\r\n');
        }
        term.writeln("Welcome, " + character.name + ".");
        //TODO: menu w/ location, gold, level, xp
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
        console.log("Character Name set to: " + newCharacterName);

        const res = await createCharacter(input);
        login(username, password);

        term.write("\r\n");
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

function authHeaders() {
    const token = localStorage.getItem("rpg-token");

    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
    };
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