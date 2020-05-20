import {Meme, NewMeme} from "../types";

export const getMemes = (): Promise<Meme[]> => {
    return fetch("http://localhost:8080/memes", {
        method: "GET"
    })
        .then(res => res.json())
};

export const uploadMeme = (newMeme: NewMeme): void => {
    const currTitle: string = newMeme.title;
    const mockUser: string = "mock user";
    const image: any = newMeme.image;

    console.log(image)

    const meme: string = JSON.stringify({title: currTitle, author: mockUser})
    console.log(meme);

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
}
