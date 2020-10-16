export interface User {
    username: string,
    password: string
}

export interface NewUser {
    username: string,
    email: string,
    password: string
}

export interface Meme {
    memeID: string,
    title: string,
    author: string,
    voteCount: number,
    created: number,
    imagePath: string,
    userVote: number,
    numComments: number
}

export interface NewMeme {
    title: string,
    image: null
}
