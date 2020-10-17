import 'bootstrap/dist/css/bootstrap.css';
import React, {Component} from 'react'
import {Meme} from "../types";
import {MemeService} from "../service/memeService";
import styles from "../styles/Meme.css"
import Card from "react-bootstrap/Card";
import {Button} from "react-bootstrap";
import ForwardIcon from "@material-ui/icons/Forward"
import ModeCommentRoundedIcon from "@material-ui/icons/ModeCommentRounded"
import ButtonGroup from "react-bootstrap/ButtonGroup";
import {Redirect} from "react-router";

interface Props {
    meme: Meme,
    showCommentsButton: boolean
}

interface State {
    /**
     *  currVote represents if meme got voted by active user
     * 0 => not voted yet
     * 1 => upvoted
     * -1 => downvoted
     */
    currVote: number,
    redirect: boolean
}

export class MemeCard extends Component<Props, State> {
    private readonly serverUrl: string = "http://localhost:8080/"
    private readonly timeDiff: string;

    constructor(props: Props) {
        super(props);
        this.state = {
            currVote: this.props.meme.userVote,
            redirect: false
        };

        this.timeDiff = this.calculateHumanReadableTimeDiff()

        this.upvote = this.upvote.bind(this);
        this.downvote = this.downvote.bind(this);
        this.goToComments = this.goToComments.bind(this);
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
        let newVote: number = 1;

        if (this.state.currVote === 1) {
            newVote = 0;
            this.props.meme.voteCount--;
        } else if (this.state.currVote === 0) {
            this.props.meme.voteCount++;
        } else if (this.state.currVote === -1) {
            this.props.meme.voteCount += 2;
        }

        MemeService.voteMeme(this.props.meme.memeID, newVote)
            .then(() => {
                this.setState({currVote: newVote});
            });
    }

    private downvote() {
        let newVote: number = -1;

        if (this.state.currVote === 1) {
            this.props.meme.voteCount -= 2;
        } else if (this.state.currVote === 0) {
            this.props.meme.voteCount--;
        } else if (this.state.currVote === -1) {
            newVote = 0;
            this.props.meme.voteCount++;
        }

        MemeService.voteMeme(this.props.meme.memeID, newVote)
            .then(() => {
                this.setState({currVote: newVote});
            });
    }

    private goToComments() {
        this.setState({redirect: true});
    }

    private createVoteButtons(): { upvote: JSX.Element, downvote: JSX.Element } {
        let upvoteButton: JSX.Element;
        let downvoteButton: JSX.Element;

        if (this.state.currVote === 1) {
            upvoteButton =
                <Button variant={"success"} onClick={this.upvote}>
                    <ForwardIcon className={styles.upvoteButton}/>
                </Button>;

            downvoteButton =
                <Button variant={"outline-secondary"} onClick={this.downvote}>
                   <ForwardIcon className={styles.downvoteButton}/>
                </Button>
        } else if (this.state.currVote === -1) {
            upvoteButton =
                <Button variant={"outline-secondary"} onClick={this.upvote}>
                    <ForwardIcon className={styles.upvoteButton}/>
                </Button>;

            downvoteButton =
                <Button variant={"danger"} onClick={this.downvote}>
                    <ForwardIcon className={styles.downvoteButton}/>
                </Button>
        } else {
            upvoteButton =
                <Button variant={"outline-secondary"} onClick={this.upvote}>
                    <ForwardIcon className={styles.upvoteButton}/>
                </Button>;

            downvoteButton =
                <Button variant={"outline-secondary"} onClick={this.downvote}>
                    <ForwardIcon className={styles.downvoteButton}/>
                </Button>
        }

        return {
            upvote: upvoteButton,
            downvote: downvoteButton
        };
    }

    render() {
        if (this.state.redirect) {
            return (<Redirect to={"/meme/" + this.props.meme.memeID}/>);
        }

        const buttons: { upvote: JSX.Element, downvote: JSX.Element } = this.createVoteButtons();

        const upvoteButton: JSX.Element = buttons.upvote;
        const downvoteButton: JSX.Element = buttons.downvote;

        return (
            <div className={styles.memeCard}>
                <Card className={"mt-4"}>

                    <Card.Header>
                        <Card.Title className={styles.memeTitle}>
                            {this.props.meme.title}
                        </Card.Title>

                        <Card.Subtitle className={"text-muted"}>
                            Posted by {this.props.meme.author} {" · "}
                            {this.timeDiff} ago
                        </Card.Subtitle>
                    </Card.Header>

                    <Card.Body>
                        <Card.Img variant={"bottom"}
                                  src={this.serverUrl + this.props.meme.imagePath}
                                  alt={"Excellent meme"}>
                        </Card.Img>

                        <Card.Text className={"text-muted"}>
                            {this.props.meme.voteCount} points {" · "}
                            {this.props.meme.numComments} comments
                        </Card.Text>

                        <ButtonGroup>
                            {upvoteButton}

                            {downvoteButton}

                            {this.props.showCommentsButton &&
                            <Button variant={"outline-secondary"} onClick={this.goToComments}>
                                <ModeCommentRoundedIcon/>
                            </Button>}
                        </ButtonGroup>
                    </Card.Body>
                </Card>
            </div>
        );
    }
}
