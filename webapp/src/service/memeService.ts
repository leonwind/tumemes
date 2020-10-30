import {NewMeme} from "../types";
import {Requests} from "./requests";
import {AUTH_HEADER} from "./headers";

export class MemeService {

    /**
     * @param limitParamValue
     * @param queryParam to retrieve memes sort by new or sort by points
     */
    static async getMemes(limitParamValue: number, queryParam: string = ""): Promise<Response> {
        const url: string = "memes?limit=" + limitParamValue + queryParam;

        return await Requests.sendRequest(url, {
            method: "GET",
            headers: AUTH_HEADER

        }, false);
    }

    static async getMemesFromUser(
        username: string, limitParamValue: number, queryParam: string = ""): Promise<Response> {

        const url: string = "memes/user/" + username +  "?limit=" + limitParamValue + queryParam;
        return await Requests.sendRequest(url, {
            method: "GET",
            headers: AUTH_HEADER
        });
    }

    static async getMemeByID(memeID: string): Promise<Response> {
        return await Requests.sendRequest("memes/meme/" + memeID, {
            method: "GET",
            headers: AUTH_HEADER
        }, false);
    }

    static async uploadMeme(newMeme: NewMeme): Promise<void> {
        const currTitle: string = newMeme.title;
        const image: any = newMeme.image;
        const meme: string = JSON.stringify({"title": currTitle})

        let data: FormData = new FormData();
        data.append("file", image);
        data.append("meme", meme);

        await Requests.sendRequest("upload", {
            method: "POST",
            headers: AUTH_HEADER,
            body: data
        });
    }
}
