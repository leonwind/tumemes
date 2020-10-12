import React, {ChangeEvent, Component, FormEvent} from "react";
import {User} from "../types";
import {AuthorizationService} from "../service/authorizationService";
import {Redirect} from "react-router-dom";

interface State {
    username: string,
    password: string,
    error: string,
    redirect: boolean
}

export class LoginPage extends Component<{}, State> {

    constructor(props: any) {
        super(props);
        this.state = {
            username: "",
            password: "",
            error: "",
            redirect: false
        };

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    private handleUsernameChange(event: ChangeEvent<HTMLInputElement>) {
        this.setState({username: event.target.value});
    }

    private handlePasswordChange(event: ChangeEvent<HTMLInputElement>) {
        this.setState({password: event.target.value});
    }

    private handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();

        const user: User = {
            username: this.state.username,
            password: this.state.password
        }

        AuthorizationService.loginUser(user)
            .then((ans: Response) => {
                const dataPromise: Promise<string> = ans.text();
                if (ans.ok) {
                    dataPromise.then((data: string) => {
                        window.localStorage.setItem("access_token", data);
                    })
                    this.setState({redirect: true});
                    return;
                }

                if (ans.status === 401) {
                    this.setState({error: "Username or password wrong"});
                    return;
                }

                this.setState({error: "An error occurred on the server. " +
                        "Please try again later"});
            })
    }

    render() {
        // redirect to homepage if login was successful
        if (this.state.redirect) {
            console.log("REDIRECT");
            return (<Redirect to={"/"}/>);
        }

        return (
            <div>
                <form onSubmit={this.handleSubmit}>
                    <label htmlFor="username">Username</label>
                    <input type="username" className="form-control" id="username"
                           value={this.state.username} onChange={this.handleUsernameChange}
                           placeholder="Enter username" required/>

                    <label htmlFor="password">Password</label>
                    <input type="password" className="form-control" id="password"
                           value={this.state.password} onChange={this.handlePasswordChange}
                           placeholder="Password" required/>

                    <button type="submit" className="btn btn-primary">
                        Submit
                    </button>

                    <span style={{color: "red"}}>
                        {this.state.error}
                    </span>
                </form>
            </div>
        );
    }
}