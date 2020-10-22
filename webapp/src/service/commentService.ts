import {Requests} from "./requests";
import {AUTH_HEADER, JSON_AUTH_HEADER} from "./headers";
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
            headers: JSON_AUTH_HEADER,
            body: data
        });
    }

    static async getCommentsFromMemeByNew(memeID: string): Promise<Response> {
        return await Requests.sendRequest("comments/" + memeID, {
            method: "GET",
            headers: AUTH_HEADER
        }, false);
    }

    static async getCommentsFromMemeByPoints(memeID: string): Promise<Response> {
        return await Requests.sendRequest("comments/" + memeID + "?sortBy=votes", {
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