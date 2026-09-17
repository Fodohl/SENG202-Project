callJavaAPI = (functionName, params = {}) => {

    // Bridge not read
    if(!window.javaBridge)
        return

   window.javaBridge.callJavaAPI(functionName, JSON.stringify(params));
}

callJavaAPI("func", {id: 2});

console.log = (...args) => {
    callJavaAPI("onConsoleLog", {message: args.map(String).join(' ')})
}