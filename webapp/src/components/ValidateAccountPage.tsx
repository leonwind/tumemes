import React, {Component} from "react";
import {RouteComponentProps} from "react-router";
import {AccountService} from "../service/accountService";
import history from "../customHistory";

interface Props {
    token: string
}

interface State {
    response: string
}

export class ValidateAccountPage extends Component<RouteComponentProps<Props>, State> {
    private readonly token: string;

    constructor(props: any) {
        super(props);

        this.state = {
            response: ""
        };

        this.token = this.props.match.params.token;
    }

    componentDidMount() {
        AccountService.validateAccount(this.token)
            .then((ans: Response) => {
                if (ans.ok) {
                    history.push("/login");
                } else {
                    const ansPromise: Promise<string> = ans.text();
                    ansPromise.then((data: string) => {
                        this.setState({response: data});
                    });
                }
            });
    }

    render() {
        return (
            <div>
                {this.state.response}
            </div>
        );
    }
}