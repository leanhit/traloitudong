/**
 * @fileoverview Utility function to export data to a JSON file.
 */

/**
 * Exports a JavaScript object or array to a JSON file, triggering a download in the browser.
 * @param {object|array} data The data to be exported.
 * @param {string} filename The name of the file to be downloaded (e.g., 'data.json').
 */
export function exportDataAsJson(data, filename) {
    try {
        // Convert the data to a JSON string with proper indentation
        const jsonString = JSON.stringify(data, null, 2);
        // Create a Blob object from the JSON string with the specified MIME type
        const blob = new Blob([jsonString], { type: 'application/json' });
        // Create a temporary URL for the Blob object
        const url = URL.createObjectURL(blob);
        // Create a hidden anchor element to trigger the download
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        document.body.appendChild(a);
        // Simulate a click on the anchor element
        a.click();
        // Clean up the temporary URL and the anchor element
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
    } catch (error) {
        console.error("Lỗi khi xuất file JSON:", error);
    }
}
