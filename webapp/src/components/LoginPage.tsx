import React, {ChangeEvent, Component, FormEvent} from "react";
import {User} from "../types";
import {AuthorizationService} from "../service/authorizationService";
import {Redirect} from "react-router-dom";
import Button from "react-bootstrap/Button";
import styles from "../styles/Login.css"
import Card from "react-bootstrap/Card";

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
                        this.setState({redirect: true});
                        return;
                    })
                }

                if (ans.status === 401) {
                    this.setState({error: "Incorrect username or password."});
                    this.setState({password: ""});
                    return;
                }

                this.setState({error: "An error occurred on the server. " +
                        "Please try again later."});
                throw new Error(ans.statusText);
            })
    }

    render() {
        // redirect to homepage if login was successful
        if (this.state.redirect) {
            return (<Redirect to={"/"}/>);
        }

        return (
            <div className={styles.loginCard}>
                <Card>
                    <form onSubmit={this.handleSubmit}>
                        <Card.Header as={"h5"} className={styles.loginCardHeader}>
                            Sign in to TUMemes
                        </Card.Header>

                        <Card.Body className={styles.loginCardFormText}>
                            <Card.Text>
                                <label htmlFor="username">
                                    Username or email address
                                </label>
                                <input type="username" className="form-control" id="username"
                                       value={this.state.username}
                                       onChange={this.handleUsernameChange}
                                       placeholder="" required/>
                            </Card.Text>

                            <Card.Text>
                                <label htmlFor="password">
                                    Password
                                </label>
                                <input type="password" className="form-control" id="password"
                                       value={this.state.password}
                                       onChange={this.handlePasswordChange}
                                       placeholder="" required/>
                            </Card.Text>

                            <Card.Text className={styles.loginErrorMessage}>
                                {this.state.error}
                            </Card.Text>

                            <Button type="submit" className={styles.loginSubmitButton}>
                                Sign in
                            </Button>
                        </Card.Body>
                    </form>

                    <Card.Footer className={styles.createAccountLink}>
                        New here? <a href={"/register"}>Create an account.</a>
                    </Card.Footer>
                </Card>
            </div>
        );
    }
}