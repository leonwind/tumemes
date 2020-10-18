import React, {ChangeEvent, Component, FormEvent} from "react";
import {Comment, NewComment} from "../types"
import {Card} from "react-bootstrap";
import styles from "../styles/Comment.css"
import {HumanReadableTimeDiff} from "./HumanReadableTimeDiff";
import {CommentService} from "../service/commentService";
import {ReplyCard} from "./ReplyCard"
import Button from "react-bootstrap/Button";


interface Props {
    comment: Comment
}

interface State {
    replies: Comment[],
    renderReplies: boolean,
    newReplyContent: string
}

export class CommentCard extends Component<Props, State> {
    private readonly timeDiff: string;


    constructor(props: Props) {
        super(props);

        this.state = {
            replies: [],
            renderReplies: false,
            newReplyContent: ""
        }

        this.timeDiff = HumanReadableTimeDiff.calculateTimeDiff(this.props.comment.created);

        this.loadReplies = this.loadReplies.bind(this);
        this.handleNewReplyChange = this.handleNewReplyChange.bind(this);
        this.postReply = this.postReply.bind(this);
    }

    private loadReplies() {
        CommentService.getRepliesOfComment(this.props.comment.commentID)
            .then((ans: Response) => {
                if (ans.ok) {
                    const repliesPromise: Promise<Comment[]> = ans.json();
                    repliesPromise.then((replies: Comment[]) => {
                        console.log(replies);
                        this.setState({replies});
                        this.setState({renderReplies: true});
                    })
                }
            });
        console.log(this.state.replies);
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

        CommentService.postComment(newComment).then(() => {});
        this.setState({newReplyContent: ""});
        this.loadReplies();
    }

    render() {
        let replyElements: JSX.Element[] = [];

        if (this.state.renderReplies) {
            replyElements = this.state.replies.map((reply: Comment) =>
            <ReplyCard key={reply.commentID} reply={reply}/>);
        }

        return (
            <div>
                <div className={styles.commentCard}>
                    <Card className={"mt-2"}>
                        <Card.Title className={"text-muted"}>
                            {this.props.comment.author} {" · "} {this.timeDiff} ago
                        </Card.Title>

                        {this.props.comment.content}

                        {this.props.comment.numReplies > 0 &&
                        <button className={styles.loadRepliesButton}
                                onClick={this.loadReplies}>
                            Load {this.props.comment.numReplies}
                            {this.props.comment.numReplies === 1 ? " reply" : " replies"}
                        </button>}

                        <form onSubmit={this.postReply}>
                            <input type={"textarea"}
                                   className={styles.writeCommentField}
                                   value={this.state.newReplyContent}
                                   onChange={this.handleNewReplyChange}
                                   placeholder={"Add a reply..."}/>
                            <Button type={"submit"}>
                                Post
                            </Button>
                            </form>
                    </Card>
                </div>
                {replyElements}
            </div>
        );
    }
}