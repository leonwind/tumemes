import 'bootstrap/dist/css/bootstrap.css';
import React from 'react'
import {Meme} from "../types";

interface Props {
    meme: Meme,
}

export const MemeInfo = (props: Props) => {
    return (
        <div>
            <div className={"card"} style={{width: "50%"}}>
                <h5 className={"card-title"}>{props.meme.title}</h5>
                <h6 className={"card-subtitle mb-2 text-muted"}>Posted by {props.meme.author}</h6>
                <img className="card-img-bottom" src={"http://localhost:8080/" + props.meme.imagePath}
                     alt={"Excellent meme"}/>
                <div className="card-body">
                    {props.meme.voteCount} points
                </div>
            </div>
        </div>
    );
}
