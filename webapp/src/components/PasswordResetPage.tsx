import React, {ChangeEvent, Component, FormEvent} from "react";
import {RouteComponentProps} from "react-router";
import styles from "../styles/SendEmail.css"
import Form from "react-bootstrap/Form";
import logo from "../../assets/logo.svg";
import Button from "react-bootstrap/Button";
import {LinkCollection} from "./LinkCollection";
import {SecurePassword} from "./SecurePassword";
import {PasswordReset} from "../types";
import {AccountService} from "../service/accountService";

interface Props {
    token: string
}

interface State {
    password: string,
    passwordConfirmation: string,
    passwordWeakError: string,
    passwordsNoMatchError: string
}

export class PasswordResetPage extends Component<RouteComponentProps<Props>, State> {
    private readonly token: string;

    constructor(props: any) {
        super(props);

        this.state = {
            password: "",
            passwordConfirmation: "",
            passwordWeakError: "",
            passwordsNoMatchError: ""
        };

        this.token = this.props.match.params.token;
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handlePasswordConfirmationChange = this.handlePasswordConfirmationChange.bind(this)
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    private handlePasswordChange(event: ChangeEvent<HTMLInputElement>) {
        this.setState({password: event.target.value});
    }

    private handlePasswordConfirmationChange(event: ChangeEvent<HTMLInputElement>) {
        this.setState({passwordConfirmation: event.target.value});
    }

    private handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();

        if (!SecurePassword.isSecure(this.state.password)) {
            this.setState({
                passwordWeakError: "Password is too weak.",
                passwordsNoMatchError: ""
            });
            return;
        }

        if (this.state.password !== this.state.passwordConfirmation) {
            this.setState({
                passwordWeakError: "",
                passwordsNoMatchError: "Passwords do not match"
            });
            return;
        }

        const passwordReset: PasswordReset = {
            token: this.token,
            newPassword: this.state.password
        };

        AccountService.resetPassword(passwordReset);
    }

    render() {
        return (
            <div className={styles.body}>
                <Form onSubmit={this.handleSubmit} className={styles.form}>
                    <a href={"/"}>
                        <img className={"mb-5"} src={logo} alt={"Logo"} width={"72"} height={"72"}/>
                    </a>

                    <h3 className={styles.headline}>
                        Change your password
                    </h3>

                    <hr className={"m-4"}/>

                    <p className={styles.infoText}>
                        Your new password also needs to be at least 8 characters
                        and contains one digit, one lowercase
                        and one uppercase character.
                    </p>

                    <Form.Group>
                        <Form.Label htmlFor={"password"} srOnly>
                            Enter your new password
                        </Form.Label>
                        <Form.Control type="password" id="password"
                                      value={this.state.password}
                                      onChange={this.handlePasswordChange}
                                      placeholder="Enter your new password" required/>
                        <p className={styles.errorMessage}>
                            {this.state.passwordWeakError}
                        </p>
                    </Form.Group>

                    <Form.Group>
                        <Form.Label htmlFor={"password"} srOnly>
                            Confirm your new password
                        </Form.Label>
                        <Form.Control type="password" id="password"
                                      value={this.state.passwordConfirmation}
                                      onChange={this.handlePasswordConfirmationChange}
                                      placeholder="Confirm your new password" required/>

                        <p className={styles.errorMessage}>
                            {this.state.passwordsNoMatchError}
                        </p>
                    </Form.Group>

                    <Button type={"submit"} className={styles.submitButton}>
                        Change your password
                    </Button>

                    <LinkCollection/>
                </Form>
            </div>
        );
    }
}