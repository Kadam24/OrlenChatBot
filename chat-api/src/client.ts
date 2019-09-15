const io = require('socket.io-client');

const client = io('http://localhost:3001');
console.log(`client connected`);

listenForInput();

function listenForInput() {
    console.log("Tell me what to send!");

    var stdin = process.openStdin();
    stdin.addListener("data", async data => {
        let args: string[] = data.toString().trim().split(" ");

        if (args.length < 2) {
            console.log('give me at least two args!');
            return;
        }

        client.emit(args[0], args[1]);
    });
}
