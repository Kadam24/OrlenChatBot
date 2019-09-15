function add_node(name, link) {

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
        name: "Pobierz "+name,
        priority: 500000,
        responses: [
        {
            resetContexts:false,
            affectedContexts:[],
            parameters:[],
            messages:[{
                type:"link_out_chip",
                platform:"google",
                codition:"",
                destinationName:"Pobierz"+name,
                url:link,
                lang:"pl"
            }],
            defaultResponsePlatforms:{
                google:true
            },
            speech:[]
        }],
        userSays: [{
            "data": [{
                "text": name,
                userDefined: false
            }],
            count: 0
        },
        {
            "data": [{
                "text": "Gdzie mogę pobrać "+name,
                userDefined: false
            }],
            count: 0
        },
        {
            "data": [{
                "text": "Jak pobrać"+name,
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

add_node("Wniosek o wypuszczenie żółwia", 'https://firebasestorage.googleapis.com/v0/b/orlen-chatbot-xloggj.appspot.com/o/Wniosek%20o%20wypuszczenie%20%C5%BC%C3%B3%C5%82wia.docx?alt=media&token=120a965a-2f05-4ace-b04e-54a005264145')