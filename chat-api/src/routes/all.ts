import express = require("express");
import { WebsocketManager } from "../WebsocketManager";
const router = express.Router();

router.use("/webhook", require("./wehbook.router"));
router.use("/dialogflow", require("./dialogflow.router"));


router.post('/send-anything', (req, res) => {
    const { io } = WebsocketManager.instance;
    const { eventName, data } = req.body;
    io.emit(eventName, data);
    res.send();
});

router.post('/send-as-client', (req, res) => {
    const io = require('socket.io-client');
    const client = io('http://localhost:3001');

    console.log(`test client connected`);

    const { eventName, data } = req.body;
    client.emit(eventName, data);
    res.send();
});

router.post('/send', (req, res) => {
    const wsManager = WebsocketManager.instance;
    wsManager.broadcast(req.body);
    res.send();
})


export = router;
