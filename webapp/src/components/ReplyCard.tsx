import React, {Component} from "react";
import {Comment} from "../types";
import styles from "../styles/Comment.css";
import {Card} from "react-bootstrap";
import {HumanReadableTimeDiff} from "./HumanReadableTimeDiff";
import {VoteService} from "../service/voteService";
import ForwardIcon from "@material-ui/icons/Forward"
import {VoteButtons} from "./VoteButtons";

interface Props {
    reply: Comment
}

interface State {
    currVote: number
}

export class ReplyCard extends Component<Props, State> {
    private readonly timeDiff: string;

    constructor(props: Props) {
        super(props);

        this.state = {
            currVote: this.props.reply.userVote
        };

        this.timeDiff = HumanReadableTimeDiff.calculateTimeDiff(this.props.reply.created);

        this.upvote = this.upvote.bind(this);
        this.downvote = this.downvote.bind(this);
    }

    private upvote() {
        let newVote: number = 1;

        if (this.state.currVote === 1) {
            newVote = 0;
            this.props.reply.voteCount--;
        } else if (this.state.currVote === 0) {
            this.props.reply.voteCount++;
        } else if (this.state.currVote === -1) {
            this.props.reply.voteCount += 2;
        }

        VoteService.voteComment(this.props.reply.commentID, newVote).then(() => {
            this.setState({currVote: newVote});
        });
    }

    private downvote() {
        let newVote: number = -1;

        if (this.state.currVote === 1) {
            this.props.reply.voteCount -= 2;
        } else if (this.state.currVote === 0) {
            this.props.reply.voteCount--;
        } else if (this.state.currVote === -1) {
            newVote = 0;
            this.props.reply.voteCount++;
        }

        VoteService.voteComment(this.props.reply.commentID, newVote).then(() => {
            this.setState({currVote: newVote});
        });
    }

    private createVoteButtons(): { upvote: JSX.Element, downvote: JSX.Element } {
        const upvoteButtonIcon: JSX.Element = <ForwardIcon className={styles.upvoteIcon}/>;
        const downvoteButtonIcon: JSX.Element = <ForwardIcon className={styles.downvoteIcon}/>;

        return VoteButtons.createVoteButtons(
            this.state.currVote,
            upvoteButtonIcon,
            downvoteButtonIcon,
            this.upvote,
            this.downvote,
            styles.upvote,
            styles.noVote,
            styles.downvote);
    }

    render() {
        const buttons: { upvote: JSX.Element, downvote: JSX.Element } = this.createVoteButtons();
        const upvoteButton: JSX.Element = buttons.upvote;
        const downvoteButton: JSX.Element = buttons.downvote;

        return (
            <div className={styles.replyParent}>

                <div className={styles.replyVoteButtonsDiv}>
                    {upvoteButton}
                    {downvoteButton}
                </div>

                <div className={styles.replyCard}>
                    <Card className={"mb-2"}>
                        <Card.Text className={"text-muted ml-2 mt-1"}>
                            {this.props.reply.author} {" · "}
                            {this.props.reply.voteCount}
                            {this.props.reply.voteCount === 1 ? " point" : " points"} {" · "}
                            {this.timeDiff} ago
                        </Card.Text>

                        <Card.Text className={"ml-2 mb-1"}>
                            {this.props.reply.content}
                        </Card.Text>
                    </Card>
                </div>
            </div>
        );
    }
}