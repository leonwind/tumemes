import {Meme, NewMeme} from "../types";

export class MemeService {
    private static readonly API_ENDPOINT = "http://localhost:8080/";

    static async getMemes(): Promise<Meme[]> {
        const response = await this.sendRequest("memes", {method: "GET"});
        return await response.json();
    }

    static async uploadMeme(newMeme: NewMeme) {
        const currTitle: string = newMeme.title;
        const mockUser: string = "mock user";
        const image: any = newMeme.image;

        const meme: string = JSON.stringify({title: currTitle, author: mockUser})

        let data: FormData = new FormData();
        data.append("file", image);
        data.append("meme", meme);

        await this.sendRequest("upload", {
            method: "POST",
            body: data
        });
    }

    static async upvoteMeme(memeID: string)  {
        const url = "upvote/" + memeID;
        await this.sendRequest(url, {method: "POST"})
    }

    static async downvoteMeme(memeID: string) {
        const url = "downvote/" + memeID;
        await this.sendRequest(url, {method: "POST"})
    }

    private static async sendRequest(url: string, options: any) {
        const response = await fetch(this.API_ENDPOINT + url, options);
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response;
    }
}
