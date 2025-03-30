chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    if (message.action === "startMonitoring") {
        const { x, y, width, height, interval } = message;

        chrome.runtime.sendMessage({
            action: "startMonitoring",
            x,
            y,
            width,
            height,
            interval
        }).then(r => console.log("Monitoring started."));
        console.log(`Monitoring started at coordinates (${x}, ${y}) with dimensions ${width}x${height}`);
    }

    if (message.action === "stopMonitoring") {
        chrome.runtime.sendMessage(
            {action: "stopMonitoring"}
        ).then(r => console.log("Monitoring stopped"));
        console.log("Monitoring stopped");
    }
});
