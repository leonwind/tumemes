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
import {HumanReadableTimeDiff} from "./HumanReadableTimeDiff";

interface Props {
    meme: Meme,
    // dont render comments button for the MemeCommentsPage
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

        this.timeDiff = HumanReadableTimeDiff.calculateTimeDiff(this.props.meme.created);

        this.upvote = this.upvote.bind(this);
        this.downvote = this.downvote.bind(this);
        this.goToComments = this.goToComments.bind(this);
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

                            {/* dont render comments button for the MemeCommentsPage */}
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
