import React, {ChangeEvent, Component, FormEvent} from "react";
import {User} from "../types";
import {AuthorizationService} from "../service/authorizationService";
import {Redirect} from "react-router-dom";
import Button from "react-bootstrap/Button";
import styles from "../styles/Login.css"
import Form from "react-bootstrap/Form";
import logo from "../../assets/logo.svg";
import {LinkCollection} from "./LinkCollection";

interface State {
    username: string,
    password: string,
    error: string,
    redirect: boolean,
    resetPassword: boolean
}

export class LoginPage extends Component<{}, State> {

    constructor(props: any) {
        super(props);
        this.state = {
            username: "",
            password: "",
            error: "",
            redirect: false,
            // if true show reset password link,
            // otherwise show resend validation link
            resetPassword: true
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
                        this.setState({redirect: true}, () => {
                            window.localStorage.setItem("access_token", data);
                            window.localStorage.setItem("username", this.state.username);
                        });
                        return;
                    })
                }

                if (ans.status === 401) {
                    dataPromise.then((data: string) => {
                        if (data === "Account not validated") {
                            this.setState({
                                error: "Please validate your account.",
                                password: "",
                                resetPassword: false
                            });
                        } else {
                            this.setState({
                                error: "Incorrect username or password",
                                password: "",
                                resetPassword: true
                            });
                        }
                    });
                    return;
                }

                this.setState({
                    error: "An error occurred on the server. " +
                        "Please try again later."
                });
                throw new Error(ans.statusText);
            })
    }

    render() {
        // redirect to homepage if login was successful
        if (this.state.redirect) {
            return (<Redirect to={"/"}/>);
        }

        return (
            <div className={styles.body}>
                <Form onSubmit={this.handleSubmit}
                      className={styles.formLogIn}
                      autoComplete={"on"}>

                    <img className={"mb-5"} src={logo} alt={"Logo"} width={"72"} height={"72"}/>

                    <h3 className={styles.headline}>
                        Sign in to TUMemes
                    </h3>

                    <hr className={"m-4"}/>

                    <Form.Label htmlFor={"inputUsername"} srOnly>Username</Form.Label>
                    <Form.Control className={styles.formControlInput}
                                  type={"username"} id={"inputUsername"}
                                  value={this.state.username}
                                  onChange={this.handleUsernameChange}
                                  placeholder={"Email or username"}
                                  required/>

                    <Form.Label htmlFor={"inputPassword"} srOnly>Password</Form.Label>
                    <Form.Control className={styles.formControlInput}
                                  type={"password"} id={"inputPassword"}
                                  value={this.state.password}
                                  onChange={this.handlePasswordChange}
                                  placeholder={"Password"}
                                  required/>

                    <p className={styles.errorMessage}>
                        {this.state.error}
                    </p>

                    {this.state.resetPassword &&
                    <a href={"/request_password_reset"}>
                        Forgot password?
                    </a>}

                    {!this.state.resetPassword &&
                    <a href={"/request_verification_resend"}>
                        Resend account verification email?
                    </a>}

                    <Button type="submit" className={styles.submitButton}>
                        Sign in
                    </Button>

                    <p className={styles.createAccountLink}>
                        New here? <a href={"/register"}>Create an account.</a>
                    </p>

                    <LinkCollection/>
                </Form>
            </div>
        );
    }
}