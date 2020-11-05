export const JSON_HEADER: Headers = new Headers({
    "Content-Type": "Application/json"
});

export function GENERATE_AUTH_HEADER(): Headers {
    return new Headers({
        "Authorization": "Bearer $" + window.localStorage.getItem("access_token")
    });
};

export function GENERATE_JSON_AUTH_HEADER(): Headers {
    return new Headers({
        "Content-Type": "Application/json",
        "Authorization": "Bearer $" + window.localStorage.getItem("access_token")
    });
}