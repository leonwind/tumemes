import 'bootstrap/dist/css/bootstrap.css';
import React, {Component} from "react";
import {AuthorizationService} from "../service/authorizationService";

interface State {
    username: string,
    email: string,
    password: string,
    repeatedPassword: string
    disabled: boolean,
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
            disabled: true
        };

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleRepeatedPasswordChange = this.handleRepeatedPasswordChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    private handleUsernameChange(event: any) {
        this.setState({username: event.target.value});
        this.enableButton();
    }

    private handleEmailChange(event: any) {
        this.setState({email: event.target.value});
        this.enableButton();
    }

    private handlePasswordChange(event: any) {
        this.setState({password: event.target.value});
        this.enableButton();
    }

    private handleRepeatedPasswordChange(event: any) {
        this.setState({repeatedPassword: event.target.value});
        this.enableButton();
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

    private enableButton() {
        console.log(this.state);
        /*if (!Registration.isSecure(this.state.password) ||
            this.state.password !== this.state.repeatedPassword) {

            console.log("Password is weak");
            this.setState({disabled: true});
            return;
        }

        let domain: string = Registration.getDomain(this.state.email);
        if (!allowedDomains.has(domain)) {

            console.log("Domain wrong");
            this.setState({disabled: true});
            return;
        }*/

        this.setState({disabled: false});
    }

    private handleSubmit() {
        console.log("REGISTER USER:");
        console.log(this.state.username);
        console.log(this.state.email);
        console.log(this.state.password);
        AuthorizationService.registerUser({
            username: this.state.username,
            email: this.state.email,
            password: this.state.password
        }).then((ans: Response) => {console.log(ans)});
    }

    render() {
        return (
            <div>
                <form>
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input type="username" className="form-control" id="username"
                               value={this.state.username} onChange={this.handleUsernameChange}
                               aria-describedby="usernameHelp"
                               placeholder="Enter username"/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">Email address</label>
                        <input type="email" className="form-control" id="email"
                               value={this.state.email} onChange={this.handleEmailChange}
                               aria-describedby="emailHelp"
                               placeholder="Enter email"/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input type="password" className="form-control" id="password"
                               value={this.state.password} onChange={this.handlePasswordChange}
                               placeholder="Password"/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Repeat Password</label>
                        <input type="password" className="form-control" id="repeated_password"
                               value={this.state.repeatedPassword}
                               onChange={this.handleRepeatedPasswordChange}
                               placeholder="Repeat Password"/>
                    </div>

                    <button type="submit" className="btn btn-primary"
                            disabled={this.state.disabled}
                            onClick={this.handleSubmit}>Submit
                    </button>
                </form>
            </div>
        );
    }
}