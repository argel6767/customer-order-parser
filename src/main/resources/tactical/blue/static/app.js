function updateOctoparseFileName(name) {
    document.getElementById('octoparseFileName').textContent = name;
}

function updateItemDescriptionFileName(name) {
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
    const ecommerceSite = document.querySelector('input[name="ecommerceSite"]:checked');
    if (!ecommerceSite) {
        showMessage("Please select an e-commerce site.");
        return;
    }

    const siteName = ecommerceSite.value;
    console.log("Selected E-commerce Site: " + siteName);
    // Pass the siteName to Java
    javabridge.processFiles(siteName);
}