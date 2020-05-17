import {Meme} from "../types";

export const getMemes = (): Promise<Meme[]> => {
    return fetch("http://localhost:8080/memes")
        .then(res => res.json())
};