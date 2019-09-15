"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const path = require("path");
const _http = require("http");
const _socket = require("socket.io");
const bodyParser = require("body-parser");
const WebsocketManager_1 = require("./WebsocketManager");
const cors = require("cors");
const PORT = process.env.PORT || 3001;
const app = express();
const http = _http.createServer(app);
const io = _socket(http);
WebsocketManager_1.WebsocketManager.start(io);
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use('*', cors()); // include before other routes
app.use(require("./routes/all"));
app.use(express.static(path.join(__dirname, "..", "./static/")));
http.listen(PORT, () => console.log(`Server up. Listening on port ${PORT}`));
//# sourceMappingURL=main.js.map