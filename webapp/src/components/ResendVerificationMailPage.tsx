import React, {ChangeEvent, Component, FormEvent} from "react";
import styles from "../styles/SendEmail.css"
import Form from "react-bootstrap/Form";
import logo from "../../assets/logo.svg";
import Button from "react-bootstrap/Button";
import {LinkCollection} from "./LinkCollection";

interface State {
    email: string,
    redirect: boolean
}

export class ResendVerificationMailPage extends Component<{}, State> {

    constructor(props: any) {
        super(props);
        this.state = {
            email: "",
            redirect: false
        };

        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    private handleEmailChange(event: ChangeEvent<HTMLInputElement>) {
        this.setState({email: event.target.value});
    }

    private handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
    }

    render() {
        return (
            <div className={styles.body}>
                <Form onSubmit={this.handleSubmit} className={styles.form}>
                    <img className={"mb-5"} src={logo} alt={"Logo"} width={"72"} height={"72"}/>

                    <h3 className={styles.headline}>
                        Send new verification email
                    </h3>

                    <hr className={"m-4"}/>

                    <p className={styles.infoText}>
                        Enter the email of your account and we will send you a new link
                        to validate your account.
                    </p>

                    <Form.Label htmlFor={"inputEmail"} srOnly>
                        Enter your email address
                    </Form.Label>
                    <Form.Control type="email" id="email"
                                  value={this.state.email}
                                  onChange={this.handleEmailChange}
                                  placeholder="Enter your email" required/>

                    <Button type={"submit"} className={styles.submitButton}>
                        Resend verification email
                    </Button>

                    <LinkCollection/>
                </Form>
            </div>
        );
    }
}