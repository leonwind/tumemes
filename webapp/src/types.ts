export interface Meme {
    memeID: string,
    title: string,
    author: string,
    voteCount: number,
    created: Date,
    imagePath: string,
};

export interface NewMeme {
    title: string,
    image: null,
}
