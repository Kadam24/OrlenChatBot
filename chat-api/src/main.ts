import express = require("express");
import path = require('path');
import _http = require("http");
import _socket = require("socket.io");
import bodyParser = require('body-parser');
import { WebsocketManager } from "./WebsocketManager";
import cors = require('cors');

const PORT = process.env.PORT || 3001;
const app = express();
const http = _http.createServer(app);
const io = _socket(http);

WebsocketManager.start(io);

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use('*', cors()) // include before other routes

app.use(require("./routes/all"));

app.use(express.static(path.join(__dirname, "..", "./static/")));


http.listen(PORT, () => console.log(`Server up. Listening on port ${PORT}`));
