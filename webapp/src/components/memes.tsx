import React from 'react'

const Memes = ({memes}:any) => {
    let memesArr = Array.from(memes);
    return (
        <div>
            {memesArr.map((meme:any) => (
                <div className="card">
                    <div className="card-body">
                        <h5 className="card-title">{meme.title}</h5>
                        <h6 className="card-subtitle">{meme.voteCount}</h6>
                        <h6 className="card-subtitle">{meme.author}</h6>
                        <h6 className="card-subtitle">{meme.imagePath}</h6>
                        <h6 className="card-subtitle">{meme.created}</h6>
                    </div>
                </div>
            ))}
        </div>
    )
};

export default Memes