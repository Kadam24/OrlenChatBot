var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
const socketIo = require('socket.io-client');
const clientListener = socketIo('http://localhost:3001');
console.log(`client connected`);
listenForInput2();
function listenForInput2() {
    console.log("Tell me what to listen!");
    var stdin = process.openStdin();
    stdin.addListener("data", (data) => __awaiter(this, void 0, void 0, function* () {
        let args = data.toString().trim().split(" ");
        if (args.length < 1) {
            console.log('give me at least one argument!');
            return;
        }
        console.log(`listening to ${args[0]}`);
        clientListener.on(args[0], console.log);
    }));
}
//# sourceMappingURL=client-listener.js.map