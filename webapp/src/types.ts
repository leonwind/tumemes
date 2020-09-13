export interface User {
    username: string,
    email: string,
}

export interface NewUser {
    username: string,
    email: string,
    password: string,
}

export interface Meme {
    memeID: string,
    title: string,
    author: string,
    voteCount: number,
    created: Date,
    imagePath: string,
}

export interface NewMeme {
    title: string,
    image: null,
}
