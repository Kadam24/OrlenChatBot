"use strict";
const express = require("express");
const router = express.Router();
const { WebhookClient } = require('dialogflow-fulfillment');
function welcome(agent) {
    agent.add(`Welcome to Express.JS webhook!`);
}
function fallback(agent) {
    agent.add(`I didn't understand`);
    agent.add(`I'm sorry, can you try again?`);
}
function WebhookProcessing(req, res) {
    const agent = new WebhookClient({ request: req, response: res });
    console.info(`agent set`);
    let intentMap = new Map();
    intentMap.set('hubert - tak', welcome);
    intentMap.set('', fallback);
    // intentMap.set('<INTENT_NAME_HERE>', yourFunctionHandler);
    agent.handleRequest(intentMap);
}
// Webhook
router.post('/webhook', function (req, res) {
    console.info(`\n\n>>>>>>> S E R V E R   H I T <<<<<<<`);
    WebhookProcessing(req, res);
});
module.exports = router;
//# sourceMappingURL=wehbook.router.js.map