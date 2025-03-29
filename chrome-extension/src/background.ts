chrome.runtime.onInstalled.addListener(() => {
    console.log("Extension installed");
});

let monitoringActive = false;
let lastImageData: string | null = null;
let lock = false;

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    if (message.action === "startMonitoring") {
        if (monitoringActive) {
            console.log("Already monitoring.");
            return;
        }
        monitoringActive = true;
        startMonitoring(message).then(r => console.log("Monitoring stopped."));
    }
});

async function startMonitoring({ x, y, width, height, interval }: { x: number; y: number; width: number; height: number; interval: number }) {
    console.log("Starting monitoring...");

    while (monitoringActive) {
        try {
            const imageData = await captureArea(x, y, width, height);
            if (lastImageData && lastImageData !== imageData) {
                sendAlert();
            }
            lastImageData = imageData;
        } catch (error) {
            console.error("Error capturing area:", error);
        }

        await new Promise(resolve => setTimeout(resolve, interval * 1000));
    }
}

function captureArea(x: number, y: number, width: number, height: number): Promise<string> {
    return new Promise((resolve, reject) => {
        chrome.tabs.captureVisibleTab((screenshotUrl) => {
            if (!screenshotUrl) {
                return reject("Failed to capture screenshot");
            }

            const img = new Image();
            img.src = screenshotUrl;
            img.onload = () => {
                const canvas = document.createElement("canvas");
                canvas.width = width;
                canvas.height = height;
                const ctx = canvas.getContext("2d");

                if (ctx) {
                    ctx.drawImage(img, x, y, width, height, 0, 0, width, height);
                    resolve(canvas.toDataURL());
                } else {
                    reject("Canvas context is null");
                }
            };
        });
    });
}

function sendAlert() {
    if (lock) return;
    lock = true;

    chrome.notifications.create({
        type: "basic",
        iconUrl: "icon.png",
        title: "변경 감지됨!",
        message: "지정한 영역에서 변경 사항이 감지되었습니다."
    });

    setTimeout(() => {
        lock = false;
    }, 30000);
}
