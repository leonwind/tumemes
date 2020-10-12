import {NewUser, User} from "../types";
import {Requests} from "./requests";

export class AuthorizationService {
    private static readonly JSON_HEADER: Headers = new Headers({
        "Content-Type": "Application/json"
    });

    static async registerUser(newUser: NewUser): Promise<Response> {
        const data: string = JSON.stringify({
            "username": newUser.username,
            "email": newUser.email,
            "password": newUser.password
        });

        return await Requests.sendRequest("register", {
            method: "POST",
            headers: this.JSON_HEADER,
            body: data
        }, false);
    }

    static async loginUser(user: User): Promise<Response> {
        const data: string = JSON.stringify({
            "username": user.username,
            "password": user.password
        });

        return await Requests.sendRequest("login", {
            method: "POST",
            headers: this.JSON_HEADER,
            body: data
        }, false);
    }
}