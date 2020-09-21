import {NewUser} from "../types";

export class AuthorizationService {
    private static readonly API_ENDPOINT: string = "http://localhost:8080/";
    private static readonly JSON_HEADER: Headers = new Headers({
        "Content-Type": "Application/json"});

    static async registerUser(newUser: NewUser): Promise<Response> {
        const data: string = JSON.stringify({
            "username": newUser.username,
            "email": newUser.email, "password": newUser.password
        });

        return await this.sendRequest("register", {
            method: "POST",
            headers: this.JSON_HEADER,
            body: data
        });
    }

    private static async sendRequest(path: string, options: any): Promise<Response> {
        const response = await fetch(this.API_ENDPOINT + path, options);

        if (!response.ok) {
            throw new Error(response.statusText);
        }

        return response;
    }
}