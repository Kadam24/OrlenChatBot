import { Server } from "socket.io";
import axios from "axios";

export class WebsocketManager {

    private static _instance: WebsocketManager;
    private static isInitialized = false;

    public io: Server;

    private constructor(io: Server) {
        this.io = io;
    }

    public static start(io: Server) {
        if (this.isInitialized) {
            throw new Error("WebsocketManager alredy initialized.");
        }

        this._instance = new WebsocketManager(io);
        this._instance.startListening();
        this.isInitialized = true;
        return this._instance;
    }

    public static get instance() {
        if (!this.isInitialized) {
            throw new Error("WebsocketManager wasn't initilized. Call init() first.");
        }

        return this._instance;
    }

    private startListening() {
        this.io.on('connect', socket => {
            socket.use((packet, next) => {
                console.log(`incoming packet`);
                console.log(packet);
                next();
            });

            console.log('new client connected');
            var clients = Object.keys(this.io.sockets.sockets);
            console.log(clients.length);

            socket.on('connect-to-agent', data => {
                console.log('connect-to-agent');
                console.log(data);
            });

            socket.on('broadcast', data => {
                socket.broadcast.emit(data);
            });

            socket.on('chat message', data => {
                this.io.emit('chat message', data);
            })

            socket.on('update-db', async data => {
                await updateDialogFlowDb(data.request, data.response);
            });

            // webapp
            socket.on('message', (m) => {
                console.log('[server](message): %s', JSON.stringify(m));
                this.io.emit('message', m);
            });

            socket.on('disconnect', () => {
                console.log('Client disconnected');
            });

            socket.use((packet, next) => {
                console.log(`catch'em all`);
                console.log(packet);

                if (packet[0] == 'update-db') {
                    updateDialogFlowDb(packet[1].request, packet[1].response);
                    return;
                }

                Object.entries(this.io.sockets.sockets)
                    .filter(([k, v]) => k !== socket.id) // dont sent to senter
                    .forEach(([k, v]) => v.emit(packet[0], packet[1]));
                // this.io.emit(packet[0], packet[1]);
            });
        });
    }


    public broadcast(data) {
        this.io.sockets.emit('broadcast', data);
    }
}

async function updateDialogFlowDb(question, answer) {
    const body = {
        contexts: [],
        events: [],
        fallbackIntent: false,
        name: question,
        priority: 500000,
        responses: [{
            "resetContexts": false,
            affectedContexts: [],
            parameters: [],
            messages: [{
                "type": 0,
                condition: "",
                speech: answer
            }],
            defaultResponsePlatforms: {},
            speech: []
        }],
        userSays: [{
            "data": [{
                "text": question,
                userDefined: false
            }],
            count: 0
        }],
        webhookForSlotFilling: false,
        webhookUsed: false
    };

    try {
        await axios.post(
            "https://api.dialogflow.com/v1/intents?v=20150910&lang=pl",
            body,
            {
                headers: {
                    Authorization: "Bearer e12ca75d415341938351f6b2918a3fda",
                    "Content-Type": "application/json; charset=UTF-8"
                },
            }
        );

        console.log(`updated dialogflow db`);
    } catch (err) {
        console.error(`error while updating dialogflow db`, err);
    }
}

