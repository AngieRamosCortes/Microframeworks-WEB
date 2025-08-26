/**
 * JavaScript functions for interacting with the Web Framework Demo API
 * These functions demonstrate how to consume the REST services provided by the framework
 */

/**
 * Test the hello service with a name parameter
 */
function testHello() {
    const name = document.getElementById('nameInput').value || 'World';
    const url = `/hello?name=${encodeURIComponent(name)}`;
    
    fetch(url)
        .then(response => response.text())
        .then(data => {
            document.getElementById('helloResult').innerHTML = 
                `<div class="success">Response: ${data}</div>`;
        })
        .catch(error => {
            document.getElementById('helloResult').innerHTML = 
                `<div class="error">Error: ${error.message}</div>`;
        });
}

/**
 * Get the PI value from the mathematical constants service
 */
function getPi() {
    fetch('/pi')
        .then(response => response.json())
        .then(data => {
            document.getElementById('piResult').innerHTML = 
                `<div class="success">
                    <strong>PI Value:</strong> ${data.value}<br>
                    <strong>Description:</strong> ${data.description}
                </div>`;
        })
        .catch(error => {
            document.getElementById('piResult').innerHTML = 
                `<div class="error">Error: ${error.message}</div>`;
        });
}

/**
 * Generate a random number using the random number service
 */
function getRandomNumber() {
    fetch('/random')
        .then(response => response.json())
        .then(data => {
            document.getElementById('randomResult').innerHTML = 
                `<div class="success">Random Number: <strong>${data.randomNumber}</strong></div>`;
        })
        .catch(error => {
            document.getElementById('randomResult').innerHTML = 
                `<div class="error">Error: ${error.message}</div>`;
        });
}

/**
 * Perform a calculation using the calculator service
 */
function calculate() {
    const a = document.getElementById('numA').value;
    const b = document.getElementById('numB').value;
    const op = document.getElementById('operation').value;
    
    if (!a || !b) {
        document.getElementById('calcResult').innerHTML = 
            `<div class="error">Please enter both numbers</div>`;
        return;
    }
    
    const url = `/calc?a=${encodeURIComponent(a)}&b=${encodeURIComponent(b)}&op=${encodeURIComponent(op)}`;
    
    fetch(url)
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                document.getElementById('calcResult').innerHTML = 
                    `<div class="error">Error: ${data.error}</div>`;
            } else {
                document.getElementById('calcResult').innerHTML = 
                    `<div class="success">
                        <strong>Calculation:</strong> ${data.a} ${data.operation} ${data.b} = <strong>${data.result}</strong>
                    </div>`;
            }
        })
        .catch(error => {
            document.getElementById('calcResult').innerHTML = 
                `<div class="error">Error: ${error.message}</div>`;
        });
}

/**
 * Check the framework's health status
 */
function checkHealth() {
    fetch('/health')
        .then(response => response.json())
        .then(data => {
            const date = new Date(data.timestamp);
            document.getElementById('healthResult').innerHTML = 
                `<div class="success">
                    <strong>Status:</strong> ${data.status}<br>
                    <strong>Timestamp:</strong> ${date.toLocaleString()}
                </div>`;
        })
        .catch(error => {
            document.getElementById('healthResult').innerHTML = 
                `<div class="error">Error: ${error.message}</div>`;
        });
}

/**
 * Utility function to make HTTP requests with error handling
 */
function makeRequest(url, options = {}) {
    return fetch(url, options)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
            return response;
        });
}

/**
 * Initialize the page when DOM is loaded
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log('Web Framework Demo loaded');
    
    document.getElementById('nameInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            testHello();
        }
    });
    
    document.getElementById('numA').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            calculate();
        }
    });
    
    document.getElementById('numB').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            calculate();
        }
    });
});
