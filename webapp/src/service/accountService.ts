import {Requests} from "./requests"
import {JSON_HEADER} from "./headers"
import {PasswordReset} from "../types";

export class AccountService {

    static async requestPasswordReset(email: string) {
        const data: string = JSON.stringify({"email": email});

        await Requests.sendRequest("account/request/password_reset", {
            method: "POST",
            headers: JSON_HEADER,
            body: data
        });
    }

    static async resetPassword(resetPassword: PasswordReset) {
        const data: string = JSON.stringify({
            "token": resetPassword.token,
            "newPassword": resetPassword.newPassword
        });

        await Requests.sendRequest("account/password_reset", {
            method: "POST",
            headers: JSON_HEADER,
            body: data
        });
    }
}