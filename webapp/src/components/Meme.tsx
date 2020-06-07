import 'bootstrap/dist/css/bootstrap.css';
import React, {Component} from 'react'
import {Meme} from "../types";
import {MemeService} from "../service/memeService";
import styles from "../styles/Meme.css"

interface Props {
    meme: Meme,
}

interface State {
    /**
     *  currVote represents if meme got voted by active user
     * 0 => not voted yet
     * 1 => upvoted
     * -1 => downvoted
     */
    currVote: number,
}

export class MemeInfo extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {currVote: 0};

        this.upvote = this.upvote.bind(this);
        this.downvote = this.downvote.bind(this);
    }

    private upvote() {
        let vote: number = 1;

        if (this.state.currVote === 1) {
           vote = 0;
        }

        MemeService.voteMeme(this.props.meme.memeID, vote)
            .then(() => {
                this.props.meme.voteCount += vote;
                this.setState({currVote: vote});
            });
    }

    private downvote() {
        let vote: number = -1;

        if (this.state.currVote === -1) {
           vote = 0;
        }

        MemeService.voteMeme(this.props.meme.memeID, vote)
            .then(() => {
                this.props.meme.voteCount += vote;
                this.setState({currVote: vote});
            });
    }

    render() {
        return (
            <div className={styles.memeCard}>
                <div className={"card"}>
                    <h5 className={"card-title"}>{this.props.meme.title}</h5>
                    <h6 className={"card-subtitle mb-2 text-muted"}>Posted by {this.props.meme.author}</h6>
                    <img className="card-img-bottom" src={"http://localhost:8080/" + this.props.meme.imagePath}
                         alt={"Excellent meme"}/>
                    <div className="card-body">
                        {this.props.meme.voteCount} points
                        <button onClick={this.upvote}>Upvote</button>
                        <button onClick={this.downvote}>Downvote</button>
                    </div>
                </div>
            </div>
        );
    }
}
