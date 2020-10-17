import React, {Component} from "react";
import {Comment} from "../types"


interface Props {
    comment: Comment
}

interface State {

}

export class CommentCard extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
    }

    render() {

        return (
            <div>
                {this.props.comment.content} by {this.props.comment.author}
            </div>
        );
    }
}