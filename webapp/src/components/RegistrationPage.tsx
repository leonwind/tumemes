import 'bootstrap/dist/css/bootstrap.css';
import React, {ChangeEvent, Component, FormEvent} from "react";
import {AuthorizationService} from "../service/authorizationService";
import {NewUser} from "../types";
import styles from "../styles/Registration.css"
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import logo from "../../assets/logo.svg";
import Modal from "react-bootstrap/Modal";
import Spinner from "react-bootstrap/Spinner";
import {LinkCollection} from "./LinkCollection";
import {SecurePassword} from "./SecurePassword";
import history from "../customHistory"

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
    isLoading: boolean,
    show: boolean
}

const allowedDomains = new Set([
    "tum.de",
    "mytum.de"
]);

export class RegistrationPage extends Component<{}, State> {

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
            isLoading: false,
            show: false
        };

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleRepeatedPasswordChange = this.handleRepeatedPasswordChange.bind(this);
        this.handleShowModal = this.handleShowModal.bind(this);
        this.handleCloseModal = this.handleCloseModal.bind(this);
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

    private handleShowModal() {
        this.setState({
            show: true,
            isLoading: false
        });
    }

    private handleCloseModal() {
        this.setState({show: false});
        history.push("/login");
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

        if (this.state.username.includes('@')) {
            newErrors.username = "@ is not allowed in the username.";
            isValid = false;
        }

        const domain: string = RegistrationPage.getDomain(this.state.email);
        if (!allowedDomains.has(domain)) {
            newErrors.email = "Only @tum.de and @mytum.de emails are supported.";
            isValid = false;
        }

        if (!SecurePassword.isSecure(this.state.password)) {
            newErrors.password = "Password is too weak.";
            isValid = false;
        }

        if (this.state.password !== this.state.repeatedPassword) {
            newErrors.repeatedPassword = "Passwords do not match.";
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

        this.setState({isLoading: true});

        AuthorizationService.registerUser(newUser)
            .then((ans: Response) => {
                const dataPromise: Promise<string> = ans.text();
                if (ans.ok) {
                    this.handleShowModal();
                    return;
                }

                let newErrors = {
                    username: "",
                    email: "",
                    password: "",
                    repeatedPassword: "",
                    unexpected: ""
                };

                if (ans.status === 400) {
                    dataPromise.then((data: string) => {
                        if (data === "Username exists") {
                            newErrors.username = "Username already exists.";
                        } else if (data === "Email exists") {
                            newErrors.email = "Email already exists.";
                        } else {
                            newErrors.unexpected =
                                "An error occurred on the server. Please try again later.";
                            throw new Error(ans.statusText);
                        }

                        this.setState({
                            errors: newErrors,
                            isLoading: false
                        });
                    });
                } else {
                    newErrors.unexpected =
                        "An error occurred on the server. Please try again later.";
                    this.setState({
                        errors: newErrors,
                        isLoading: false
                    });
                    throw new Error(ans.statusText);
                }
                }
            );
    }

    render() {
        return (
            <div className={styles.body}>
                <Form onSubmit={this.handleSubmit}
                      className={styles.formRegistration}>

                    <div className={styles.logo}>
                        <img className={"mb-5"} src={logo} alt={"Logo"} width={"72"} height={"72"}/>
                    </div>

                    <h3 className={styles.headline}>
                        Create your TUMemes account
                    </h3>

                    <hr className={"m-4"}/>

                    <Form.Group>
                        <Form.Label htmlFor="inputUsername" srOnly>
                            Username
                        </Form.Label>
                        <Form.Control type="username" id="inputUsername"
                                      value={this.state.username}
                                      onChange={this.handleUsernameChange}
                                      placeholder="Username" required/>
                        <p className={styles.errorMessage}>
                            {this.state.errors["username"]}
                        </p>
                    </Form.Group>

                    <Form.Group>
                        <Form.Label htmlFor="inputEmail" srOnly>
                            Email address
                        </Form.Label>
                        <Form.Control type="email" id="email"
                                      value={this.state.email}
                                      onChange={this.handleEmailChange}
                                      placeholder="Email address" required/>
                        <p className={styles.errorMessage}>
                            {this.state.errors["email"]}
                        </p>

                        <Form.Text className={"text-muted"}>
                            Please register with your @tum.de or @mytum.de email
                            address.
                        </Form.Text>
                    </Form.Group>

                    <Form.Group>
                        <Form.Label htmlFor="inputPassword" srOnly>
                            Password
                        </Form.Label>
                        <Form.Control type="password" id="inputPassword"
                                      value={this.state.password}
                                      onChange={this.handlePasswordChange}
                                      placeholder="Password" required
                                      autoComplete={"new-password"}/>
                        <p className={styles.errorMessage}>
                            {this.state.errors["password"]}
                        </p>

                        <Form.Text className={"text-muted"}>
                            Password needs to be at least 8 characters
                            and contains one digit, one lowercase
                            and one uppercase character.
                        </Form.Text>
                    </Form.Group>

                    <Form.Group>
                        <Form.Label htmlFor="inputPasswordAgain" srOnly>
                            Confirm Password
                        </Form.Label>
                        <Form.Control type="password" id="inputPasswordAgain"
                                      value={this.state.repeatedPassword}
                                      onChange={this.handleRepeatedPasswordChange}
                                      placeholder="Confirm password" required
                                      autoComplete={"new-password"}/>
                        <p className={styles.errorMessage}>
                            {this.state.errors["repeatedPassword"]}
                        </p>
                    </Form.Group>

                    <p className={styles.errorMessage}>
                        {this.state.errors["unexpected"]}
                    </p>

                    <Form.Group>
                        <Form.Check type={"checkbox"}
                                    label={"By signing up, you agree to our terms and conditions."}
                                    required/>

                        <Button type="submit"
                                className={styles.registrationSubmitButton}
                                disabled={this.state.isLoading}>
                            {this.state.isLoading &&
                            <Spinner animation="border" role="status" size={"sm"}>
                                <span className="sr-only">Loading...</span>
                            </Spinner>} {" "}
                            Create account
                        </Button>
                    </Form.Group>

                    <p className={styles.loginLink}>
                        Already have an account? <a href={"/login"}>Sign in.</a>
                    </p>

                    <LinkCollection/>
                </Form>

                <Modal show={this.state.show} onHide={this.handleCloseModal} centered>
                    <Modal.Header closeButton>
                        <h4 className={styles.modalHeadline}>
                            Verify your account
                        </h4>
                    </Modal.Header>

                    <Modal.Body>
                        <p>
                            To verify your TUMemes account, please click on the link we sent you via email.
                            Note that the link will expire in one day.
                        </p>
                        <p>
                            If you configured that your TUM account automatically forwards your email
                            to your primary email account, it may take a while until it arrives.
                        </p>
                        <p>
                            However, you can also directly log in into your
                            <a href={"https://mail.tum.de"}> TUM Email account</a>.
                        </p>
                    </Modal.Body>

                    <Modal.Footer>
                        <Button onClick={this.handleCloseModal}>
                           Understood
                        </Button>
                    </Modal.Footer>
                </Modal>
            </div>
        );
    }
}
