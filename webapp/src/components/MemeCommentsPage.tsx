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
        let promiseResponse: Promise<Response>;
        if (this.state.sortByNew) {
            console.log("SORT BY NEW");
            promiseResponse = CommentService.getCommentsFromMemeByNew(this.memeID);
        } else {
            console.log("SORT BY POINTS");
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
            this.setState({sortByString: "new"});
            this.setState({sortByNew: true});
        } else {
            if (filter === "points") {
                this.setState({sortByString: "points"});
                this.setState({sortByNew: false});
            }
        }

        this.loadComments();
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

                    <div>
                        <Form onSubmit={this.postComment}>
                            <Form.Control as={"textarea"} rows={3}
                                          className={"mt-3"}
                                          value={this.state.newCommentContent}
                                          onChange={this.handleNewCommentChange}
                                          placeholder={"Add a comment..."}/>

                            <Button type={"submit"} className={"mt-2 float-right"}>
                                Post
                            </Button>
                        </Form>
                    </div>

                    <div className={styles.space}/>

                    <Dropdown className={"mt-4 float-right"}>
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
                    </Dropdown>

                    <div className={styles.space}/>

                    {allComments}
                </div>
            </div>
        );
    }
}