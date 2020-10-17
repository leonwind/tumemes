import React, {Component} from "react";
import {Comment, Meme} from "../types";
import {RouteComponentProps} from "react-router";
import {MemeService} from "../service/memeService";
import {PageNotFound} from "./PageNotFound";
import {MemeCard} from "./Meme";
import {NavigationBar} from "./NavigationBar";
import {CommentService} from "../service/commentService";
import {CommentCard} from "./Comment";

interface Props {
    memeID: string
}

interface State {
    meme: Meme | null,
    comments: Comment[],
    redirect: boolean
}

export class MemeCommentsPage extends Component<RouteComponentProps<Props>, State> {
    private readonly memeID: string;

    constructor(props: any) {
        super(props);

        this.state = {
            meme: null,
            comments: [],
            redirect: false
        }

        this.memeID = this.props.match.params.memeID;
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

    render() {
        console.log(this.memeID);

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
                {allComments}
            </div>
        );
    }
}