import {Meme, NewMeme} from "../types";

export class MemeService {
    private static readonly API_ENDPOINT = "localhost:8080/";

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


/*export const getMemes = (): Promise<Meme[]> => {
    return fetch("http://localhost:8080/memes", {
        method: "GET"
    })
        .then(res => res.json())
};

export const uploadMeme = (newMeme: NewMeme): void => {
    const currTitle: string = newMeme.title;
    const mockUser: string = "mock user";
    const image: any = newMeme.image;

    const meme: string = JSON.stringify({title: currTitle, author: mockUser})

    let data: FormData = new FormData();
    data.append("file", image);
    data.append("meme", meme);

    fetch("http://localhost:8080/upload", {
        method: "POST",
        body: data
    })
        .then(response => response.json())
        .catch(error => console.log(error));
}

export const upvoteMeme = (memeID: string): void => {
    fetch("http://localhost:8080/upvote/" + memeID, {
        method: "POST"
    })
        .then(response => response.json())
        .catch(error => console.log(error));
}

export const downvoteMeme = (memeID: string): void => {
    fetch("http://localhost:8080/downvote/" + memeID, {
        method: "POST"
    })
        .then(response => response.json())
        .catch(error => console.log(error));
}*/
