"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
const express = require("express");
const axios_1 = require("axios");
const WebsocketManager_1 = require("../WebsocketManager");
const router = express.Router();
const TOKEN = "b0489f7f8df14390879fb6067aa57319";
let messages = {};
router.get("*", (req, res) => __awaiter(void 0, void 0, void 0, function* () {
    const BASE_URL = "https://api.dialogflow.com/v1";
    const { path, query } = req;
    try {
        const { status, data } = yield axios_1.default.get(`${BASE_URL}/${path}`, {
            params: query,
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
        });
        res.status(status).send(data);
    }
    catch (err) {
        res.status(400).send(err);
    }
}));
router.post("*", (req, res) => __awaiter(void 0, void 0, void 0, function* () {
    const BASE_URL = "https://api.dialogflow.com/v1";
    const { path, body, query } = req;
    const { sessionId } = body;
    if (!messages[sessionId]) {
        messages[sessionId] = [];
    }
    console.log(`id: ${sessionId}`);
    messages[sessionId].push({ messageType: "request", message: body.query });
    try {
        const { status, data } = yield axios_1.default.get(`${BASE_URL}/${path}`, {
            // body: body,
            params: Object.assign(Object.assign({}, query), body),
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
        });
        messages[sessionId].push({ messageType: "response", message: extractDialogFlowMessage(data.result.fulfillment) });
        if (data.result.resolvedQuery === "Połączyć się z agentem") {
            connectWithLiveAgent(sessionId);
            data['connectedToLiveAgent'] = true;
        }
        res.status(status).send(data);
    }
    catch (err) {
        res.status(err.response.status).send(err);
    }
}));
function connectWithLiveAgent(sessionId) {
    console.log(`setting up live agent ${sessionId}`);
    const { io } = WebsocketManager_1.WebsocketManager.instance;
    io.emit('live-agent-request', {
        sessionId,
        messages: messages[sessionId],
    });
}
function extractDialogFlowMessage(fulfillment = {}) {
    const { messages } = fulfillment;
    const firstMessage = messages[0];
    switch (true) {
        case firstMessage.type == 0:
            return firstMessage.speech;
        case firstMessage.type == "basic_card":
            return `${firstMessage.title}: ${firstMessage.formattedText}`;
        case firstMessage.type == "suggestion_chips":
            return firstMessage.suggestions.map(x => x.title).join(', ');
        default:
            return firstMessage.speech || firstMessage.textToSpeech;
    }
}
module.exports = router;
//# sourceMappingURL=dialogflow.router.js.map