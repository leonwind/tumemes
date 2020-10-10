import 'bootstrap/dist/css/bootstrap.css';
import React, {Component, FormEvent} from "react";
import {AuthorizationService} from "../service/authorizationService";
import {NewUser} from "../types";

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
    }
}

const allowedDomains = new Set([
    "tum.de",
    "mytum.de"]);

export class Registration extends Component<{}, State> {

    constructor(props: any) {
        super(props);
        this.state = {
            username: "", email: "",
            password: "", repeatedPassword: "",
            errors: {
                username: "", email: "",
                password: "", repeatedPassword: "",
                unexpected: ""
            }
        };

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleRepeatedPasswordChange = this.handleRepeatedPasswordChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    private handleUsernameChange(event: any) {
        this.setState({username: event.target.value});
    }

    private handleEmailChange(event: any) {
        this.setState({email: event.target.value});
    }

    private handlePasswordChange(event: any) {
        this.setState({password: event.target.value});
    }

    private handleRepeatedPasswordChange(event: any) {
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
        let newErrors = {username: "", email: "",
            password: "", repeatedPassword: "",
            unexpected: ""};

        let isValid: boolean = true;

        if (!Registration.isSecure(this.state.password)) {
            console.log("Password weak");
            newErrors.password = "Password weak";
            isValid = false;
        }

        if (this.state.password !== this.state.repeatedPassword) {
            console.log("Password and repeated password do not match");
            newErrors.repeatedPassword = "Password and repeated password do not match";
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

        console.log("REGISTER USER:");
        console.log(this.state.username);
        console.log(this.state.email);
        console.log(this.state.password);

        const newUser: NewUser = {
            username: this.state.username,
            email: this.state.email,
            password: this.state.password
        }

        AuthorizationService.registerUser(newUser)
            .then((ans: Response) => {
                const dataPromise: Promise<string> = ans.text();
                if (!ans.ok) {
                    dataPromise.then((data: string) => {
                        let newErrors = {username: "", email: "",
                            password: "", repeatedPassword: "",
                            unexpected: ""};

                        if (data === "Username exists") {
                            newErrors.username = data;
                        }
                        else if (data === "Email exists") {
                                newErrors.email = data;
                        }
                        else {
                            newErrors.unexpected = data;
                        }
                        this.setState({errors: newErrors});
                    });
                } else {
                    dataPromise.then((data: string) => {
                        window.localStorage.setItem("access_token", data);
                        console.log(window.localStorage.getItem("access_token"));
                    })
                }
            })
    }

    render() {
        return (
            <div>
                <form onSubmit={this.handleSubmit}>
                    <label htmlFor="username">Username</label>
                    <input type="username" className="form-control" id="username"
                           value={this.state.username} onChange={this.handleUsernameChange}
                           placeholder="Enter username" required/>
                    <span style={{color: "red"}}>
                        {this.state.errors["username"]}
                    </span>

                    <label htmlFor="email">Email address</label>
                    <input type="email" className="form-control" id="email"
                           value={this.state.email} onChange={this.handleEmailChange}
                           placeholder="Enter email" required/>
                    <span style={{color: "red"}}>
                        {this.state.errors["email"]}
                    </span>

                    <label htmlFor="password">Password</label>
                    <input type="password" className="form-control" id="password"
                           value={this.state.password} onChange={this.handlePasswordChange}
                           placeholder="Password" required/>
                    <span style={{color: "red"}}>
                        {this.state.errors["password"]}
                    </span>


                    <label htmlFor="password">Repeat Password</label>
                    <input type="password" className="form-control" id="repeated_password"
                           value={this.state.repeatedPassword}
                           onChange={this.handleRepeatedPasswordChange}
                           placeholder="Repeat Password" required/>
                    <span style={{color: "red"}}>
                        {this.state.errors["repeatedPassword"]}
                    </span>

                    <button type="submit" className="btn btn-primary">
                        Submit
                    </button>
                </form>
            </div>
        );
    }
}