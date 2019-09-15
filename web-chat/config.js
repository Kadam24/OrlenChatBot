export default {
    app: {
        token: "b0489f7f8df14390879fb6067aa57319", // <- enter your token here
        muted: false, // <- mute microphone by default
        googleIt: true // <- ask users to google their request, in case of input.unknown action
    },
    locale: {
        strings: {
            welcomeTitle: "Witaj! Napisz w czym mogę pomóc.",
            welcomeDescription: `Możesz zacząć od wpisania 'Benefity'. Lub po prostu kliknij mikrofon i powiedz!`,
            offlineTitle: "Oh, nie!",
            offlineDescription: "Uops, nie mam dostępu do internetu. Połącz się, by uzyskać odpowiedzi.",
            queryTitle: "W czym mogę pomóc?",
            voiceTitle: "Powiedz mi co Cię boli."
        },
        settings: {
            speechLang: "pl-PL", // <- output language
            recognitionLang: "pl-PL" // <- input(recognition) language
        }
    }
}
