import React, {Component} from "react";
import {RouteComponentProps} from "react-router";
import {FrontPage} from "./FrontPage";

interface State {

}

interface Props {
    username: string
}

export class MemesByUserPage extends Component<RouteComponentProps<Props>, State> {
    private readonly username: string;

    constructor(props: any) {
        super(props);

        this.state = {};

        this.username = this.props.match.params.username;
    }

    render() {
        return (
            <FrontPage user={this.username}/>
        );
    }

}