import 'bootstrap/dist/css/bootstrap.css';
import React, {ChangeEvent, Component, FormEvent} from "react";
import {AuthorizationService} from "../service/authorizationService";
import {NewUser} from "../types";
import {Redirect} from "react-router-dom";
import styles from "../styles/Registration.css"
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";

interface State {
    username: string,
    email: string,
    password: string,
    repeatedPassword: string,
    errors: {
        username: string,
        email: string,
        password: string,
        repeatedPassword: string,
        unexpected: string
    },
    redirect: boolean
}

const allowedDomains = new Set([
    "tum.de",
    "mytum.de"
]);

export class Registration extends Component<{}, State> {

    constructor(props: any) {
        super(props);
        this.state = {
            username: "", email: "",
            password: "", repeatedPassword: "",
            errors: {
                username: "",
                email: "",
                password: "",
                repeatedPassword: "",
                unexpected: ""
            },
            redirect: false
        };

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleRepeatedPasswordChange = this.handleRepeatedPasswordChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    private handleUsernameChange(event: ChangeEvent<HTMLInputElement>) {
        this.setState({username: event.target.value});
    }

    private handleEmailChange(event: ChangeEvent<HTMLInputElement>) {
        this.setState({email: event.target.value});
    }

    private handlePasswordChange(event: ChangeEvent<HTMLInputElement>) {
        this.setState({password: event.target.value});
    }

    private handleRepeatedPasswordChange(event: ChangeEvent<HTMLInputElement>) {
        this.setState({repeatedPassword: event.target.value});
    }

    private static isDigit(char: string): boolean {
        return char >= '0' && char <= '9';
    }

    private static isSecure(password: string): boolean {
        if (password.length < 8) {
            return false;
        }

        let containsDigit: boolean = false;
        let containsLowerCase: boolean = false;
        let containsUpperCase: boolean = false;

        for (let i = 0; i < password.length; i++) {
            let curr: string = password.charAt(i);

            if (Registration.isDigit(curr)) {
                containsDigit = true;
                continue;
            }

            if (curr === curr.toLowerCase()) {
                containsLowerCase = true;
                continue;
            }

            if (curr === curr.toUpperCase()) {
                containsUpperCase = true;
            }

            // if everything satisfied, return true before finishing loop
            if (containsDigit && containsLowerCase && containsUpperCase) {
                return true;
            }
        }
        return containsDigit && containsLowerCase && containsUpperCase;
    }

    private static getDomain(email: string): string {
        return email.substr(email.indexOf('@') + 1);
    }

    private areCredentialsValid(): boolean {
        let newErrors = {
            username: "",
            email: "",
            password: "",
            repeatedPassword: "",
            unexpected: ""
        };

        let isValid: boolean = true;

        if (!Registration.isSecure(this.state.password)) {
            console.log("Password weak");
            newErrors.password = "Password is too weak";
            isValid = false;
        }

        if (this.state.password !== this.state.repeatedPassword) {
            console.log("Password and repeated password do not match");
            newErrors.repeatedPassword = "Passwords do not match";
            isValid = false;
        }

        const domain: string = Registration.getDomain(this.state.email);
        if (!allowedDomains.has(domain)) {
            console.log("Domain wrong");
            newErrors.email = "Only @tum.de and @mytum.de emails are supported";
            isValid = false;
        }

        this.setState({errors: newErrors});
        return isValid;
    }

    private handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();

        if (!this.areCredentialsValid()) {
            return;
        }

        const newUser: NewUser = {
            username: this.state.username,
            email: this.state.email,
            password: this.state.password
        }

        AuthorizationService.registerUser(newUser)
            .then((ans: Response) => {
                const dataPromise: Promise<string> = ans.text();
                if (ans.ok) {
                    dataPromise.then((data: string) => {
                        window.localStorage.setItem("access_token", data);
                        this.setState({redirect: true});
                        return;
                    })
                }

                dataPromise.then((data: string) => {
                    let newErrors = {
                        username: "",
                        email: "",
                        password: "",
                        repeatedPassword: "",
                        unexpected: ""
                    };

                    if (data === "Username exists") {
                        newErrors.username = "Username already exists.";
                    }
                    else if (data === "Email exists") {
                            newErrors.email = "Email already exists.";
                    }
                    else {
                        newErrors.unexpected = "An error occurred on the server." +
                            "Please try again later.";
                        throw new Error(ans.statusText);
                    }
                    this.setState({errors: newErrors});
                });
            })
    }

    render() {
        // redirect to homepage if registration was successful
        if (this.state.redirect) {
            console.log("REDIRECT");
            return (<Redirect to={"/"}/>);
        }

        return (
            <div className={styles.registrationCard}>
                <Card>
                    <Card.Header as={"h5"} className={styles.registrationCardHeader}>
                        Create your TUMemes account
                    </Card.Header>

                    <Card.Body className={styles.registrationCardFormText}>
                        <form onSubmit={this.handleSubmit}>

                            <Card.Text>
                                <label htmlFor="username">
                                    Username
                                </label>
                                <input type="username" className="form-control" id="username"
                                       value={this.state.username}
                                       onChange={this.handleUsernameChange}
                                       placeholder="" required/>
                                <span style={{color: "red"}}>
                                    {this.state.errors["username"]}
                                </span>
                            </Card.Text>

                            <Card.Text>
                                <label htmlFor="email">
                                    Email address
                                </label>
                                <input type="email" className="form-control" id="email"
                                       value={this.state.email}
                                       onChange={this.handleEmailChange}
                                       placeholder="" required/>
                                <span style={{color: "red"}}>
                                    {this.state.errors["email"]}
                                </span>
                            </Card.Text>

                            <Card.Text>
                                <label htmlFor="password">
                                    Password
                                </label>
                                <input type="password" className="form-control" id="password"
                                       value={this.state.password}
                                       onChange={this.handlePasswordChange}
                                       placeholder="" required/>
                                <span style={{color: "red"}}>
                                    {this.state.errors["password"]}
                                </span>
                            </Card.Text>
                            <Card.Text className={"text-muted"}>
                                <small>
                                Password needs to be at least 8 characters
                                and contains one digit, one lowercase
                                and one uppercase character.
                                </small>
                            </Card.Text>

                            <Card.Text>
                                <label htmlFor="password">
                                    Repeat Password
                                </label>
                                <input type="password" className="form-control" id="repeated_password"
                                       value={this.state.repeatedPassword}
                                       onChange={this.handleRepeatedPasswordChange}
                                       placeholder="" required/>
                                <span style={{color: "red"}}>
                                    {this.state.errors["repeatedPassword"]}
                                </span>
                            </Card.Text>

                            <Button type="submit" className={styles.registrationSubmitButton}>
                               Create account
                            </Button>

                        </form>
                    </Card.Body>

                    <span style={{color: "red"}}>
                        {this.state.errors["unexpected"]}
                    </span>

                    <Card.Footer className={styles.registrationLogInLink}>
                        Have an account? <a href={"/login"}>Sign in.</a>
                    </Card.Footer>
                </Card>
            </div>
        );
    }
}

