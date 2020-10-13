import 'bootstrap/dist/css/bootstrap.css';
import React, {Component} from 'react'
import {Meme} from "../types";
import {MemeService} from "../service/memeService";
import styles from "../styles/Meme.css"
import Card from "react-bootstrap/Card";
import {Button} from "react-bootstrap";

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
    private readonly serverUrl: string = "http://localhost:8080/"
    private readonly timeDiff: string;

    constructor(props: Props) {
        super(props);
        this.state = {currVote: 0};

        this.timeDiff = this.calculateHumanReadableTimeDiff()

        this.upvote = this.upvote.bind(this);
        this.downvote = this.downvote.bind(this);
    }

    /**
     * Convert the time difference of the day posted and the current date
     * to a human readable format
     */
    private calculateHumanReadableTimeDiff(): string {
        const seconds: number = Math.floor((
            new Date().getTime() - this.props.meme.created) / 1000);

        // 31536000 = one year in seconds
        let interval: number = Math.floor(seconds / 31536000);
        if (interval > 1) {
            return interval + " years";
        }

        // 2592000 = one month in seconds
        interval = Math.floor(seconds / 2592000);
        if (interval > 1) {
            return interval + " months";
        }

        // 604800 = one week in seconds
        interval = Math.floor(seconds / 604800);
        if (interval > 1) {
            return interval + " weeks";
        }

        // 86400 = one day in seconds
        interval = Math.floor(seconds / 86400);
        if (interval > 1) {
            return interval + " days";
        }

        // 3600 = one hour in seconds
        interval = Math.floor(seconds / 3600);
        if (interval > 1) {
            return interval + " hours";
        }

        // 60 = one minute in seconds
        interval = Math.floor(seconds / 60);
        if (interval > 1) {
            return interval + " minutes";
        }

        return Math.floor(seconds) + " seconds";
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
                <Card className={"mt-4"}>

                    <Card.Header>
                        <Card.Title className={styles.memeTitle}>
                            {this.props.meme.title}
                        </Card.Title>

                        <Card.Subtitle className={"text-muted"}>
                            Posted by {this.props.meme.author} {" "}
                            {this.timeDiff} ago
                        </Card.Subtitle>
                    </Card.Header>

                    <Card.Body>
                        <Card.Img variant={"bottom"}
                                  src={this.serverUrl + this.props.meme.imagePath}
                                  alt={"Excellent meme"}>
                        </Card.Img>

                        <Card.Text>
                            {this.props.meme.voteCount} points
                        </Card.Text>

                        <Button onClick={this.upvote}>
                            Upvote
                        </Button>

                        <Button onClick={this.downvote}>
                            Downvote
                        </Button>
                    </Card.Body>
                </Card>
            </div>
        );
    }
}
