const socketIo = require('socket.io-client');

const clientListener = socketIo('http://localhost:3001');
console.log(`client connected`);

listenForInput2();

function listenForInput2() {
    console.log("Tell me what to listen!");

    var stdin = process.openStdin();
    stdin.addListener("data", async data => {
        let args: string[] = data.toString().trim().split(" ");

        if (args.length < 1) {
            console.log('give me at least one argument!');
            return;
        }

        console.log(`listening to ${args[0]}`);

        clientListener.on(args[0], console.log);
    });
}
