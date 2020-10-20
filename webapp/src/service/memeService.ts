import {NewMeme} from "../types";
import {Requests} from "./requests";
import {AUTH_HEADER, JSON_AUTH_HEADER} from "./headers";

export class MemeService {
    static async getMemes(): Promise<Response> {
        return await Requests.sendRequest("memes", {
            method: "GET",
            headers: AUTH_HEADER
        }, false);
    }

    static async getMemeByID(memeID: string): Promise<Response> {
        return await Requests.sendRequest("memes/" + memeID, {
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
