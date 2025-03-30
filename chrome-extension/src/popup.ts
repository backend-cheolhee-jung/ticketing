document.getElementById("start-monitoring")?.addEventListener("click", () => {
    const x = parseInt((<HTMLInputElement>document.getElementById("x-coordinate"))?.value || "0");
    const y = parseInt((<HTMLInputElement>document.getElementById("y-coordinate"))?.value || "0");
    const width = parseInt((<HTMLInputElement>document.getElementById("width"))?.value || "100");
    const height = parseInt((<HTMLInputElement>document.getElementById("height"))?.value || "100");
    const interval = parseInt((<HTMLInputElement>document.getElementById("interval"))?.value || "30");

    chrome.runtime.sendMessage({
        action: "startMonitoring",
        x,
        y,
        width,
        height,
        interval
    }).then(r => console.log("Monitoring started."));
    console.log(`Monitoring started with parameters x:${x}, y:${y}, width:${width}, height:${height}, interval:${interval}s`);
});

document.getElementById("stop-monitoring")?.addEventListener("click", () => {
    chrome.runtime.sendMessage(
        {action: "stopMonitoring"}
    ).then(r => console.log("Monitoring stopped."));
    console.log("Monitoring stopped.");
});
