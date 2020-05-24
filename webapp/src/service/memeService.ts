import {Meme, NewMeme} from "../types";

export class MemeService {
    private static readonly API_ENDPOINT: string = "http://localhost:8080/";
    private static readonly JSON_HEADER: Headers = new Headers({"Content-Type": "Application/json"});

    static async getMemes(): Promise<Meme[]> {
        const response = await this.sendRequest("memes", {method: "GET"});
        return await response.json();
    }

    static async uploadMeme(newMeme: NewMeme) {
        const currTitle: string = newMeme.title;
        const mockUser: string = "mock user";
        const image: any = newMeme.image;

        const meme: string = JSON.stringify({"title": currTitle, "author": mockUser})

        let data: FormData = new FormData();
        data.append("file", image);
        data.append("meme", meme);

        await this.sendRequest("upload", {
            method: "POST",
            body: data
        });
    }

    static async voteMeme(memeID: string, vote: number) {
        await this.sendRequest("vote", {
            method: "POST",
            headers: this.JSON_HEADER,
            body: JSON.stringify({"memeID": memeID, "vote": vote})
        });
    }

    private static async sendRequest(url: string, options: any) {
        const response = await fetch(this.API_ENDPOINT + url, options);

        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response;
    }
}
