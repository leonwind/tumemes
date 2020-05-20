import 'bootstrap/dist/css/bootstrap.css';
import React, {useState} from 'react'
import {Meme} from "../types";
import {downvoteMeme, upvoteMeme} from "../service/memeService";

interface Props {
    meme: Meme,
}

function useForceUpdate() {
    let [value, setState] = useState(true);
    return () => setState(!value);
}

export const MemeInfo = (props: Props) => {
    let forceUpdate = useForceUpdate();

    function upvote() {
        upvoteMeme(props.meme.memeID);
        props.meme.voteCount++;
        forceUpdate()
    }

    function downvote() {
        downvoteMeme(props.meme.memeID);
        props.meme.voteCount--;
        forceUpdate();
    }

    return (
        <div>
            <div className={"card"} style={{width: "50%"}}>
                <h5 className={"card-title"}>{props.meme.title}</h5>
                <h6 className={"card-subtitle mb-2 text-muted"}>Posted by {props.meme.author}</h6>
                <img className="card-img-bottom" src={"http://localhost:8080/" + props.meme.imagePath}
                     alt={"Excellent meme"}/>
                <div className="card-body">
                    {props.meme.voteCount} points
                    <button onClick={upvote}>Upvote</button>
                    <button onClick={downvote}>Downvote</button>
                </div>
            </div>
        </div>
    );
}
