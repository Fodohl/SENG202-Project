// Usage:
// 1. run 'npm i gmaps-apistyle-encoder '
// 2. edit snazzy.json
// 3. run 'node snazzy.js'
// 4. copy output into map.html

const fs = require('fs');
const path = require('path');

async function main() {

    // Import encoded
    const {default: encodeMapStyles} = await import('gmaps-apistyle-encoder');

    // Load json
    const targetFile = process.argv[2] || '../src/main/resources/config/snazzy.json';
    const filePath = path.resolve(process.cwd(), targetFile);
    const rawContent = fs.readFileSync(filePath, 'utf8');

    // Extract JSON
    const jsonString = rawContent.trim();
    const jsonArray = JSON.parse(jsonString);

    // Convert
    const encodedStyle = encodeMapStyles(jsonArray);
    console.log(encodedStyle);
}

main();