callJavaAPI = (functionName, params = {}) => {

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