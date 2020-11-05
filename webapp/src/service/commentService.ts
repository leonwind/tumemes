import {Requests} from "./requests";
import {GENERATE_AUTH_HEADER, GENERATE_JSON_AUTH_HEADER} from "./headers";
import {NewComment} from "../types";

export class CommentService {

    static async postComment(newComment: NewComment) {
        const data: string = JSON.stringify({
            "parentID": newComment.parentID,
            "memeID": newComment.memeID,
            "content": newComment.content
        });

        await Requests.sendRequest("comments/post", {
            method: "POST",
            headers: GENERATE_JSON_AUTH_HEADER(),
            body: data
        });
    }

    static async getCommentsFromMemeByNew(memeID: string): Promise<Response> {
        return await Requests.sendRequest("comments/" + memeID, {
            method: "GET",
            headers: GENERATE_AUTH_HEADER()
        }, false);
    }

    static async getCommentsFromMemeByPoints(memeID: string): Promise<Response> {
        return await Requests.sendRequest("comments/" + memeID + "?sortBy=votes", {
                method: "GET",
                headers: GENERATE_AUTH_HEADER()
            }, false);
    }

    static async getRepliesOfComment(commentID: string): Promise<Response> {
        return await Requests.sendRequest("comments/replies/" + commentID, {
            method: "GET",
            headers: GENERATE_AUTH_HEADER()
        }, false);
    }
}