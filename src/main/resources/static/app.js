let username = "";
let password = "";
let character = {};

const term = new Terminal();
term.open(document.getElementById('terminal'));

term.writeln("Welcome to SpringRPG");
// TODO: "new" or "login"
term.write("Username: ");
//handleCommand("inputUsername");

let input = "";

term.onData(e => {
    if (e === '\r') {
        if (!username) {
            handleCommand("inputUsername");
        } else if (!password) {
            handleCommand("inputPassword");
            term.write("\r\n");
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

        term.write("Password: ");
    }

    if (cmd === "inputPassword") {
        password = input;
        await login(username, password);
        character = await getCharacter();
        console.log(character);

        term.clear();

        const buffer = term.buffer.active;
        const bottom = buffer.length - term.rows;

        console.log("rows: " + term.rows);
        console.log("buffer: " + buffer);
        console.log(bottom - buffer.viewportY);

        for (let i = 0; i < (term.rows - 1); i++) {
            term.write('\r\n');
        }
        term.writeln("Welcome, " + character.name + ".");
        //TODO: menu w/ location, gold, level, xp
    }

    if (cmd === "new") {
        const res = await fetch("/characters", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name: "Shadowbear" })
        });

        const data = await res.json();
        window.currentEncounterId = data.encounterId;

        term.writeln(`User Shadowbear created`);
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
    console.log(data.token);

    localStorage.setItem("rpg-token", data.token);
}

function authHeaders() {
    const token = localStorage.getItem("rpg-token");

    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
    };
}

async function getCharacter() {
    const res = await fetch("/characters/me", {
        headers: authHeaders()
    });

    return res.json();
}