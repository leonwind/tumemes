import jwt_decode from "jwt-decode"
import {Token} from "../types";
import {AUTH_HEADER} from "./headers";

export class TokenService {

    /**
     * Check if token will expire soon and request a new one
     * if so
     */
    public static checkExpiration() {
        const token: string | null = window.localStorage.getItem("access_token");
        if (token === null) {
            console.log("NO TOKEN");
            return;
        }

        const decoded: Token = jwt_decode(token);
        const secondsLeft = decoded.exp - Math.floor(Date.now() / 1000);

        // get new token if current one expires in one hour or less
        if (secondsLeft <= 3600) {
            this.refreshToken();
        }
    }

    private static refreshToken() {
        console.log("GET NEW TOKEN");
        this.getNewToken()
            .then((ans: Response) => {
                const tokenPromise: Promise<string> = ans.text();
                if (ans.ok) {
                    tokenPromise.then((token: string) => {
                        window.localStorage.setItem("access_token", token);
                        return;
                    })
                }
            })
    }

    /**
     * Extra function for request to prevent recursive endless loops
     */
    private static async getNewToken() {
        return await fetch("/api/access_token", {
            method: "GET",
            headers: AUTH_HEADER
        });
    }
}