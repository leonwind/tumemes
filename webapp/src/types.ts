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
    created: number, // timestamp
    imagePath: string,
    userVote: number,
    numComments: number
}

export interface NewMeme {
    title: string,
    image: null
}

export interface Comment {
    commentID: string,
    parentID: string,
    memeID: string,
    content: string,
    author: string,
    created: number, // timestamp
    numReplies: number,
    voteCount: number,
    userVote: number
}

export interface NewComment {
    parentID: string,
    memeID: string,
    content: string
}

export interface PasswordReset {
    token: string,
    newPassword: string
}

export interface Token {
    iat: number,
    exp: number,
    sub: string
}
