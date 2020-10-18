import React, {Component} from "react";
import {Comment} from "../types"
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
    renderReplies: boolean
}

export class CommentCard extends Component<Props, State> {
    private readonly timeDiff: string;


    constructor(props: Props) {
        super(props);

        this.state = {
            replies: [],
            renderReplies: false
        }

        this.timeDiff = HumanReadableTimeDiff.calculateTimeDiff(this.props.comment.created);

        this.loadReplies = this.loadReplies.bind(this);
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
                    </Card>
                </div>
                {replyElements}
            </div>
        );
    }
}