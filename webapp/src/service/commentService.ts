import {Requests} from "./requests";

export class CommentService {
    private static readonly AUTH_HEADER: Headers = new Headers({
        "Authorization" : "Bearer $" + window.localStorage.getItem("access_token")
    });

    private static readonly JSON_AUTH_HEADER: Headers = new Headers({
        "Content-Type": "Application/json",
        "Authorization" : "Bearer $" + window.localStorage.getItem("access_token")
    });

    static async getCommentsFromMeme(memeID: string): Promise<Response> {
        return await Requests.sendRequest("comments/" + memeID, {
            method: "GET",
            headers: this.AUTH_HEADER
        }, false);
    }
}