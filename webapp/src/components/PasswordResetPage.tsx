import React, {Component} from "react";
import {RouteComponentProps} from "react-router";

interface Props {
    token: string
}

interface State {
    newPassword: string,
    newPasswordConfirmation: string
}

export class PasswordResetPage extends Component<RouteComponentProps<Props>, State> {
    private readonly token: string;

    constructor(props: any) {
        super(props);

        this.state = {
            newPassword: "",
            newPasswordConfirmation: ""
        };

        this.token = this.props.match.params.token;
    }

    render() {
        return (
            <div>
                {this.token}
            </div>
        );
    }
}