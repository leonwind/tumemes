import React from 'react'

{/*<div className="card">
                    <div className="card-body">
                        <h5 className="card-title">{meme.title}</h5>
                        <h6 className="card-subtitle">{meme.voteCount}</h6>
                        <h6 className="card-subtitle">{meme.author}</h6>
                        <img src={require("../meme1.jpg")} alt={"Could not load image"}/>
                        <h6 className="card-subtitle">{meme.created}</h6>
                    </div>
                </div>*/
}

const Memes = ({memes}: any) => {
    let memesArr = Array.from(memes);
    return (
        <div>
            {memesArr.map((meme: any) => (
                <div className="card">
                    <h5 className={"card-title"}>{meme.title}</h5>
                    <h6 className={"card-subtitle mb-2 text-muted"}>Posted by {meme.author}</h6>
                    <img className="card-img-bottom" src={"http://localhost:8080/" + meme.imagePath} alt={"Cannot find image"}/>
                    <div className="card-body">
                        {meme.voteCount}
                        <a href="#" className="btn btn-primary">Go somewhere</a>
                    </div>
                </div>
            ))}
        </div>
    )
};

export default Memes