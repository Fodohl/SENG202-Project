callJavaAPI = (functionName, params = {}) => {

    // Bridge not read
    if(!window.javaBridge)
        return

   window.javaBridge.callJavaAPI(functionName, JSON.stringify(params));
}

console.log = (...args) => {
    callJavaAPI("onConsoleLog", {message: args.map(String).join(' ')})
}

console.warn = (...args) => {
    callJavaAPI("onConsoleWarn", {message: args.map(String).join(' ')})
}

console.error = (...args) => {
    callJavaAPI("onConsoleError", {message: args.map(String).join(' ')})
}

window.onerror = (msg, src, line, col, err) => {
    callJavaAPI("onConsoleError", {message: ` ${line}:${col} - ${msg}`});
};