import React, {ChangeEvent, Component, FormEvent} from "react";
import {Comment, Meme, NewComment} from "../types";
import {RouteComponentProps} from "react-router";
import {MemeService} from "../service/memeService";
import {PageNotFound} from "./PageNotFound";
import {MemeCard} from "./MemeCard";
import {NavigationBar} from "./NavigationBar";
import {CommentService} from "../service/commentService";
import {CommentCard} from "./CommentCard";
import Card from "react-bootstrap/Card";
import styles from "../styles/Comment.css"
import Button from "react-bootstrap/Button";

interface Props {
    memeID: string
}

interface State {
    meme: Meme | null,
    comments: Comment[],
    redirect: boolean,
    newCommentContent: string
}

export class MemeCommentsPage extends Component<RouteComponentProps<Props>, State> {
    private readonly memeID: string;

    constructor(props: any) {
        super(props);

        this.state = {
            meme: null,
            comments: [],
            redirect: false,
            newCommentContent: ""
        }

        this.memeID = this.props.match.params.memeID;

        this.handleNewCommentChange = this.handleNewCommentChange.bind(this);
        this.postComment = this.postComment.bind(this);
    }

    componentDidMount() {
        MemeService.getMemeByID(this.memeID)
            .then((ans: Response) => {
                if (ans.ok) {
                    const memePromise: Promise<Meme> = ans.json();
                    memePromise.then((meme: Meme) => {
                        this.setState({meme})
                    })
                    return;
                }

                if (ans.status === 401) {
                    this.setState({redirect: true});
                    return;
                }

                throw new Error(ans.statusText);
            });

        this.loadComments();
    }

    private loadComments() {
        CommentService.getCommentsFromMeme(this.memeID)
            .then((ans: Response) => {
                if (ans.ok) {
                    const commentPromise: Promise<Comment[]> = ans.json();
                    commentPromise.then((comments: Comment[]) => {
                        this.setState({comments});
                    })
                    return;
                }

                // if unauthorized redirect to login
                if (ans.status === 401) {
                    this.setState({redirect: true});
                }

                throw new Error(ans.statusText);
            });
    }

    private handleNewCommentChange(event: ChangeEvent<HTMLInputElement>) {
        this.setState({newCommentContent: event.target.value});
    }

    private postComment(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        console.log("POST COMMENT");

        const newComment: NewComment = {
           parentID: "",
           memeID: this.memeID,
           content: this.state.newCommentContent
        };

        CommentService.postComment(newComment).then(() => {});
        this.setState({newCommentContent: ""});
        this.loadComments();
    }

    render() {
        if (this.state.meme === null) {
            return (<PageNotFound/>);
        }

        const memeElement: JSX.Element =
            <MemeCard key={this.memeID}
                      meme={this.state.meme!}
                      showCommentsButton={false}/>;

        const allComments: JSX.Element[] = this.state.comments.map((comment: Comment) =>
            <CommentCard key={comment.commentID} comment={comment}/>);

        return (
            <div>
                <NavigationBar/>

                {memeElement}

                <div className={styles.commentCard}>
                    <Card className={"mt-3 mb-3"}>
                        <form onSubmit={this.postComment}>
                            <input type={"textarea"}
                                   className={styles.writeCommentField}
                                   value={this.state.newCommentContent}
                                   onChange={this.handleNewCommentChange}
                                   placeholder={"Write your comment..."}/>
                            <Button type={"submit"}>
                                Post
                            </Button>
                        </form>
                    </Card>
                </div>

                {allComments}
            </div>
        );
    }
}