export const JSON_HEADER: Headers = new Headers({
    "Content-Type": "Application/json"
});

export const AUTH_HEADER: Headers = new Headers({
    "Authorization" : "Bearer $" + window.localStorage.getItem("access_token")
});

export const JSON_AUTH_HEADER: Headers = new Headers({
    "Content-Type": "Application/json",
    "Authorization" : "Bearer $" + window.localStorage.getItem("access_token")
});