import React, {ChangeEvent, Component, FormEvent} from "react";
import {Comment, NewComment} from "../types"
import {Card} from "react-bootstrap";
import styles from "../styles/Comment.css"
import {HumanReadableTimeDiff} from "./HumanReadableTimeDiff";
import {CommentService} from "../service/commentService";
import {ReplyCard} from "./ReplyCard"
import Button from "react-bootstrap/Button";
import {Send} from "@material-ui/icons";
import ForwardIcon from "@material-ui/icons/Forward"
import {VoteService} from "../service/voteService";
import {VoteButtons} from "./VoteButtons";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Form from "react-bootstrap/Form";


interface Props {
    comment: Comment
}

interface State {
    currVote: number,
    replies: Comment[],
    renderReplies: boolean,
    newReplyContent: string
}

export class CommentCard extends Component<Props, State> {
    private readonly timeDiff: string;


    constructor(props: Props) {
        super(props);

        this.state = {
            currVote: this.props.comment.userVote,
            replies: [],
            renderReplies: false,
            newReplyContent: ""
        };

        this.timeDiff = HumanReadableTimeDiff.calculateTimeDiff(this.props.comment.created);

        this.loadRepliesButton = this.loadRepliesButton.bind(this);
        this.loadReplies = this.loadReplies.bind(this);
        this.handleNewReplyChange = this.handleNewReplyChange.bind(this);
        this.upvote = this.upvote.bind(this);
        this.downvote = this.downvote.bind(this);
        this.postReply = this.postReply.bind(this);
    }

    private loadRepliesButton() {
        if (this.state.renderReplies) {
            this.setState({
                renderReplies: false,
                replies: []
            });
            return;
        }
        this.loadReplies();
    }

    private loadReplies() {
        CommentService.getRepliesOfComment(this.props.comment.commentID)
            .then((ans: Response) => {
                if (ans.ok) {
                    const repliesPromise: Promise<Comment[]> = ans.json();
                    repliesPromise.then((newReplies: Comment[]) => {
                        this.setState({
                            renderReplies: true,
                            replies: newReplies
                        });
                    })
                }
            });
    }


    private handleNewReplyChange(event: ChangeEvent<HTMLInputElement>) {
        this.setState({newReplyContent: event.target.value});
    }

    private postReply(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();

        const newComment: NewComment = {
            parentID: this.props.comment.commentID,
            memeID: this.props.comment.memeID,
            content: this.state.newReplyContent
        };

        CommentService.postComment(newComment).then(() => {
            this.setState({newReplyContent: ""});
            this.loadReplies();
        });
    }

    private upvote() {
        let newVote: number = 1;

        if (this.state.currVote === 1) {
            newVote = 0;
            this.props.comment.voteCount--;
        } else if (this.state.currVote === 0) {
            this.props.comment.voteCount++;
        } else if (this.state.currVote === -1) {
            this.props.comment.voteCount += 2;
        }

        VoteService.voteComment(this.props.comment.commentID, newVote).then(() => {
            this.setState({currVote: newVote});
        });
    }

    private downvote() {
        let newVote: number = -1;

        if (this.state.currVote === 1) {
            this.props.comment.voteCount -= 2;
        } else if (this.state.currVote === 0) {
            this.props.comment.voteCount--;
        } else if (this.state.currVote === -1) {
            newVote = 0;
            this.props.comment.voteCount++;
        }

        VoteService.voteComment(this.props.comment.commentID, newVote).then(() => {
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
            this.downvote);
    }

    private createReplyButton(): JSX.Element {
        // Display "load reply" Button
        if (this.props.comment.numReplies > 0 && !this.state.renderReplies) {
            return <Button variant={"outline-primary"}
                        className={"float-right"}
                        style={{border: "none"}}
                        onClick={this.loadRepliesButton}>
                            Load {this.props.comment.numReplies}
                            {this.props.comment.numReplies === 1 ? " reply" : " replies"}
                    </Button>;
        }

        // Display "Hide reply" Button
        else if (this.state.renderReplies && this.props.comment.numReplies > 0) {
            return <Button variant={"outline-primary"}
                        className={"float-right"}
                        style={{border: "none"}}
                        onClick={this.loadRepliesButton}>
                            Hide
                            {this.props.comment.numReplies === 1 ? " reply" : " replies"}
                    </Button>
        }

        // Display "Add reply" Button
        else if (this.props.comment.numReplies === 0 && !this.state.renderReplies) {
            return <Button variant={"outline-primary"}
                        className={"float-right"}
                        style={{border: "none"}}
                        onClick={() => {this.setState({renderReplies: true})}}>
                            Add reply
                    </Button>
        }

        // Display "Cancel add reply" Button
        else if (this.props.comment.numReplies === 0 && this.state.renderReplies) {
            return <Button variant={"outline-secondary"}
                        className={"float-right"}
                        style={{border: "none"}}
                        onClick={() => {this.setState({renderReplies: false})}}>
                            Cancel
                    </Button>
        }

        // not reachable else case
        return <button/>
    }

    render() {
        const buttons: { upvote: JSX.Element, downvote: JSX.Element } = this.createVoteButtons();
        const upvoteButton: JSX.Element = buttons.upvote;
        const downvoteButton: JSX.Element = buttons.downvote;

        let replyElements: JSX.Element[] = [];
        if (this.state.renderReplies) {
            replyElements = this.state.replies.map((reply: Comment) =>
                <ReplyCard key={reply.commentID} reply={reply}/>);
        }

        let replyButton: JSX.Element = this.createReplyButton();

        return (
            <div>
                <Card className={"mb-3"}>
                    <Card.Text className={"text-muted ml-2 mt-1"}>
                        {this.props.comment.author} {" · "}
                        {this.props.comment.voteCount}
                        {this.props.comment.voteCount === 1 ? " point" : " points"} {" · "}
                        {this.timeDiff} ago
                    </Card.Text>

                    <Card.Text className={"ml-2"}>
                        {this.props.comment.content}
                    </Card.Text>

                    <Card.Footer>
                        <ButtonGroup>
                            {upvoteButton}
                            {downvoteButton}
                        </ButtonGroup>

                        {/** Load show reply, hide replies,
                         * add reply or cancel add reply button
                         **/}
                        {replyButton}
                    </Card.Footer>
                </Card>

                {this.state.renderReplies &&
                <div className={styles.addReplyDiv}>
                    <Form onSubmit={this.postReply}>
                        <Form.Control as={"textarea"} rows={3}
                                      className={styles.replyTextArea}
                                      value={this.state.newReplyContent}
                                      onChange={this.handleNewReplyChange}
                                      placeholder={"Add a reply..."}
                                      required/>

                        <Button type={"submit"}
                                variant={"outline-primary"}
                                className={styles.submitReplyButton}>
                            <Send className={styles.sendReplyIcon}/>
                        </Button>
                    </Form>
                </div>}

                <div className={styles.space}/>

                {replyElements}

            </div>
        );
    }
}