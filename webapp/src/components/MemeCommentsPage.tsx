import React, {ChangeEvent, Component, FormEvent} from "react";
import {Comment, Meme, NewComment} from "../types";
import {Redirect, RouteComponentProps} from "react-router";
import {MemeService} from "../service/memeService";
import {PageNotFound} from "./PageNotFound";
import {MemeCard} from "./MemeCard";
import {NavigationBar} from "./NavigationBar";
import {CommentService} from "../service/commentService";
import {CommentCard} from "./CommentCard";
import styles from "../styles/Comment.css"
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Dropdown from "react-bootstrap/Dropdown";
import {Send} from "@material-ui/icons";

interface Props {
    memeID: string
}

interface State {
    meme: Meme | null,
    comments: Comment[],
    redirect: boolean,
    newCommentContent: string,
    sortByString: string,
    sortByNew: boolean
}

export class MemeCommentsPage extends Component<RouteComponentProps<Props>, State> {
    private readonly memeID: string;

    constructor(props: any) {
        super(props);

        this.state = {
            meme: null,
            comments: [],
            redirect: false,
            newCommentContent: "",
            sortByString: "new",
            sortByNew: true
        };

        this.memeID = this.props.match.params.memeID;

        this.handleNewCommentChange = this.handleNewCommentChange.bind(this);
        this.postComment = this.postComment.bind(this);
        this.handleSelect = this.handleSelect.bind(this);
    }

    componentDidMount() {
        MemeService.getMemeByID(this.memeID)
            .then((ans: Response) => {
                if (ans.ok) {
                    const memePromise: Promise<Meme> = ans.json();
                    memePromise.then((meme: Meme) => {
                        this.setState({meme})
                    })
                } else {
                    if (ans.status === 401) {
                        this.setState({redirect: true});
                        return;
                    } else {
                        throw new Error(ans.statusText);
                    }
                }
                this.loadComments();
            });
    }

    private loadComments() {
        let promiseResponse: Promise<Response>;
        if (this.state.sortByNew) {
            promiseResponse = CommentService.getCommentsFromMemeByNew(this.memeID);
        } else {
            promiseResponse = CommentService.getCommentsFromMemeByPoints(this.memeID);
        }

        promiseResponse.then((ans: Response) => {
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

    private handleSelect(filter: string) {
        if (filter === "new") {
            this.setState({
                sortByString: "new",
                sortByNew: true
            }, () => this.loadComments());
        } else {
            if (filter === "points") {
                this.setState({
                    sortByString: "points",
                    sortByNew: false
                }, () => this.loadComments());
            }
        }

    }

    private postComment(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();

        const newComment: NewComment = {
            parentID: "",
            memeID: this.memeID,
            content: this.state.newCommentContent
        };

        CommentService.postComment(newComment).then(() => {
            this.setState({newCommentContent: ""});
            this.loadComments();
        });
    }

    render() {
        if (this.state.redirect) {
            return (<Redirect to={"/login"}/>);
        }

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
            <div className={styles.body}>
                <NavigationBar/>

                <div className={styles.content}>
                    {memeElement}

                    <Form onSubmit={this.postComment}>
                        <Form.Control as={"textarea"} rows={3}
                                      className={"mt-3"}
                                      value={this.state.newCommentContent}
                                      onChange={this.handleNewCommentChange}
                                      placeholder={"Add a comment..."}
                                      required/>

                        <Button type={"submit"} className={"mt-2 float-right mb-3"}>
                            Post
                        </Button>
                    </Form>

                    {/* Align the floating add comment field with the sort by button */}
                    <div className={styles.space}/>

                    {/* Only render if at least one comment exists */}
                    {this.state.comments.length > 0 &&
                    <Dropdown className={"mt-4 mb-1 float-right"}>
                        <Dropdown.Toggle className={styles.sortByButton}
                                         variant={"outline-primary"}
                                         id={"dropdown-menu-align-right"}>
                            SORT BY {this.state.sortByString.toUpperCase()}
                        </Dropdown.Toggle>

                        <Dropdown.Menu>
                            <Dropdown.Item onSelect={() => this.handleSelect("new")}>
                                NEW
                            </Dropdown.Item>
                            <Dropdown.Item onSelect={() => this.handleSelect("points")}>
                                POINTS
                            </Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>}

                    {/* Align the floating add comment field with the comments */}
                    <div className={styles.space}/>

                    {allComments}
                </div>
            </div>
        );
    }
}