import express = require("express");
import axios from 'axios';
import uuid = require('uuid');
import { WebsocketManager } from "../WebsocketManager";

const router = express.Router();
const TOKEN = "b0489f7f8df14390879fb6067aa57319";


let messages: { [key: string]: any[] } = {};


router.get("*", async (req: express.Request, res: express.Response) => {
    const BASE_URL = "https://api.dialogflow.com/v1";
    const { path, query } = req;

    try {
        const { status, data } = await axios.get(`${BASE_URL}/${path}`, {
            params: query,
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
        });


        res.status(status).send(data);
    } catch (err) {
        res.status(400).send(err);
    }
});
router.post("*", async (req: express.Request, res: express.Response) => {
    const BASE_URL = "https://api.dialogflow.com/v1";
    const { path, body, query } = req;
    const { sessionId } = body;

    if (!messages[sessionId]) {
        messages[sessionId] = [];
    }

    console.log(`id: ${sessionId}`);
    messages[sessionId].push({ messageType: "request", message: body.query });

    try {
        const { status, data } = await axios.get(`${BASE_URL}/${path}`, {
            // body: body,
            params: { ...query, ...body },
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
    } catch (err) {
        res.status(err.response.status).send(err);
    }
});


function connectWithLiveAgent(sessionId: string) {
    console.log(`setting up live agent ${sessionId}`);
    const { io } = WebsocketManager.instance;

    io.emit('live-agent-request', {
        sessionId,
        messages: messages[sessionId],
    });
}

function extractDialogFlowMessage(fulfillment: any = {}): string {
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

export = router;
