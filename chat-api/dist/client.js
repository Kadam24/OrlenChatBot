var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
const io = require('socket.io-client');
const client = io('http://localhost:3001');
console.log(`client connected`);
listenForInput();
function listenForInput() {
    console.log("Tell me what to send!");
    var stdin = process.openStdin();
    stdin.addListener("data", (data) => __awaiter(this, void 0, void 0, function* () {
        let args = data.toString().trim().split(" ");
        if (args.length < 2) {
            console.log('give me at least two args!');
            return;
        }
        client.emit(args[0], args[1]);
    }));
}
//# sourceMappingURL=client.js.map