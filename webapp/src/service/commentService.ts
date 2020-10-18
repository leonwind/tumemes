import {Requests} from "./requests";
import {AUTH_HEADER} from "./headers";

export class CommentService {

    static async getCommentsFromMeme(memeID: string): Promise<Response> {
        return await Requests.sendRequest("comments/" + memeID, {
            method: "GET",
            headers: AUTH_HEADER
        }, false);
    }

    static async getRepliesOfComment(commentID: string): Promise<Response> {
        return await Requests.sendRequest("comments/replies/" + commentID, {
            method: "GET",
            headers: AUTH_HEADER
        }, false);
    }
}