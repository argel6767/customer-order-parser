function updateOctoparseFileName(name) {
    console.log("filename " + name + " grabbed")
    document.getElementById('octoparseFileName').textContent = name;
}

function updateItemDescriptionFileName(name) {
    console.log("filename " + name + " grabbed")
    document.getElementById('itemDescriptionFileName').textContent = name;
}

function showMessage(msg) {
    const messageDiv = document.getElementById('message');
    messageDiv.textContent = msg;
    if (msg.includes('successfully')) {
        messageDiv.className = 'success';
    } else {
        messageDiv.className = 'error';
    }
}

function processFiles() {
    console.log("processFiles() called")
    const form = document.getElementById('websiteForm');
    // Get the selected radio button
    const selectedRadio = form.querySelector('input[name="website"]:checked');
    if (!selectedRadio) {
        showMessage("Please select an e-commerce site.");
        return;
    }

    const siteName = selectedRadio.value;
    console.log("Selected E-commerce Site: " + siteName);
    // Pass the siteName to Java
    try {
        console.log("Attempting to call Java processFiles method");
        // Make sure javabridge is available
        if (typeof javabridge !== 'undefined' && javabridge.processFiles) {
            javabridge.processFiles(siteName);
            console.log("Java processFiles method called successfully");
        } else {
            throw new Error("Java bridge or processFiles method not available");
        }
    } catch (error) {
        console.error("Error calling Java processFiles method:", error);
        showMessage("Error: " + error.message);
    }
}