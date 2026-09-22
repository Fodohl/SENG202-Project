callJavaAPI = (functionName, params = {}) => {

<<<<<<< HEAD
        // Add salt to prevent call cache
        params = {
            ...params,
            _: Math.random()
        }

        // Convert the params into URL format
        const searchParams = new URLSearchParams(params);
        const queryString = searchParams.toString();

        // Trigger a non-destructive location change
        const url = `http://trashapp.local/api/v1/${functionName}${queryString ? '?' + queryString : ''}`;
        window.location.hash = url;
    }

console.log = (...args) => {
    callJavaAPI("onConsoleLog", {message: args.map(String).join(' ')})
}
=======
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
>>>>>>> main
