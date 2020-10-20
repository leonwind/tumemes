import {Requests} from "./requests";
import {JSON_AUTH_HEADER} from "./headers";

export class VoteService {

    static async voteMeme(memeID: string, vote: number): Promise<void> {
        await Requests.sendRequest("vote/meme", {
            method: "POST",
            headers: JSON_AUTH_HEADER,
            body: JSON.stringify({"memeID": memeID, "vote": vote})
        });
    }

    static async voteComment(commentID: string, vote: number): Promise<void> {
        await Requests.sendRequest("vote/comment", {
            method: "POST",
            headers: JSON_AUTH_HEADER,
            body: JSON.stringify({"commentID": commentID, "vote": vote})
        });
    }
}