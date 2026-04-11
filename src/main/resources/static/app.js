const term = new Terminal();
term.open(document.getElementById('terminal'));

term.writeln("Welcome to SpringRPG");
term.write("> ");

let input = "";

term.onData(e => {
    if (e === '\r') {
        handleCommand(input);
        input = "";
        term.write("\r\n> ");
    } else {
        input += e;
        term.write(e);
    }
});

async function handleCommand(cmd) {
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


