import {Meme, NewMeme} from "../types";
import {Requests} from "./requests";

export class MemeService {
    private static readonly JSON_HEADER: Headers = new Headers({
        "Content-Type": "Application/json"});
    private static readonly AUTH_HEADER: Headers = new Headers({
        "Authorization" : "Bearer $" + window.localStorage.getItem("access_token")});

    static async getMemes(): Promise<Meme[]> {
        const response = await Requests.sendRequest("memes", {
            method: "GET",
            headers: this.AUTH_HEADER
        });
        return await response.json();
    }

    static async uploadMeme(newMeme: NewMeme): Promise<void> {
        const currTitle: string = newMeme.title;
        const mockUser: string = "mock user";
        const image: any = newMeme.image;

        const meme: string = JSON.stringify({
            "title": currTitle, "author": mockUser})

        let data: FormData = new FormData();
        data.append("file", image);
        data.append("meme", meme);

        await Requests.sendRequest("upload", {
            method: "POST",
            body: data
        });
    }

    static async voteMeme(memeID: string, vote: number) {
        await Requests.sendRequest("vote", {
            method: "POST",
            headers: this.JSON_HEADER,
            body: JSON.stringify({"memeID": memeID, "vote": vote})
        });
    }
}
