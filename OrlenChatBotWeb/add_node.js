function add_node(question, answer) {

    const othePram = {
        headers: {
            "content-type": "application"
        }
    }

    // fetch("https://api.dialogflow.com/v1/intents?v=20150910&lang=pl")
    //     .then(data => '{ "contexts": [], "events": [], "fallbackIntent": false, "name": "' + question + '", "priority": 500000, "responses": [ { "resetContexts": false, "affectedContexts": [], "parameters": [], "messages": [ { "type": 0, "condition": "", "speech": "' + answer + '" } ], "defaultResponsePlatforms": {}, "speech": [] } ], "userSays": [ { "data": [ { "text": "' + question + '", "userDefined": false } ], "count": 0 } ], "webhookForSlotFilling": false, "webhookUsed": false }')
    //     .then
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

    fetch("https://api.dialogflow.com/v1/intents?v=20150910&lang=pl", {
        headers: {
            Authorization: "Bearer e12ca75d415341938351f6b2918a3fda",
            "Content-Type": "application/json; charset=UTF-8"
        },
        method: "POST",
        body: JSON.stringify(body)
    })
        .then(x => x.json())
        .then(response => {
            console.log('response');

            console.log(response)
        })
}

add_node("co", 'noc')