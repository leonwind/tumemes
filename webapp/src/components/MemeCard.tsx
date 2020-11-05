import 'bootstrap/dist/css/bootstrap.css';
import React, {Component} from 'react'
import {Meme} from "../types";
import styles from "../styles/Meme.css"
import Card from "react-bootstrap/Card";
import {Button} from "react-bootstrap";
import ForwardIcon from "@material-ui/icons/Forward"
import ModeCommentRoundedIcon from "@material-ui/icons/ModeCommentRounded"
import ButtonGroup from "react-bootstrap/ButtonGroup";
import {Redirect} from "react-router";
import {HumanReadableTimeDiff} from "./HumanReadableTimeDiff";
import {VoteService} from "../service/voteService";
import {VoteButtons} from "./VoteButtons";
import history from "../customHistory";

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
}

export class MemeCard extends Component<Props, State> {
    private readonly serverUrl: string = "http://tumemes.de/"
    private readonly timeDiff: string;

    constructor(props: Props) {
        super(props);
        this.state = {
            currVote: this.props.meme.userVote,
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

        VoteService.voteMeme(this.props.meme.memeID, newVote).then(() => {
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

        VoteService.voteMeme(this.props.meme.memeID, newVote).then(() => {
            this.setState({currVote: newVote});
        });
    }

    private goToComments() {
        history.push("/meme/" + this.props.meme.memeID);
    }

    private createVoteButtons(): { upvote: JSX.Element, downvote: JSX.Element } {
        const upvoteButtonIcon: JSX.Element = <ForwardIcon className={styles.upvoteButton}/>;
        const downvoteButtonIcon: JSX.Element = <ForwardIcon className={styles.downvoteButton}/>;

        return VoteButtons.createVoteButtons(
            this.state.currVote,
            upvoteButtonIcon,
            downvoteButtonIcon,
            this.upvote,
            this.downvote);
    }

    render() {
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

                    <Card.Img variant={"top"}
                              src={this.serverUrl + this.props.meme.imagePath}
                              alt={"Excellent meme"}>
                    </Card.Img>

                    <Card.Body>
                        <Card.Text className={"text-muted mb-2"}>
                            {this.props.meme.voteCount}
                            {this.props.meme.voteCount === 1 ? " point" : " points"} {" · "}
                            {this.props.meme.numComments}
                            {this.props.meme.numComments === 1 ? " comment" : " comments"}
                        </Card.Text>

                        <ButtonGroup className={styles.buttonGroup}>
                            {upvoteButton}

                            {downvoteButton}

                            {/* dont render comments button for the MemeCommentsPage */}
                            {this.props.showCommentsButton &&
                            <Button variant={"outline-secondary"}
                                    onClick={this.goToComments}>
                                <ModeCommentRoundedIcon/>
                            </Button>}
                        </ButtonGroup>
                    </Card.Body>
                </Card>
            </div>
        );
    }
}
