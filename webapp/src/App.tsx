import React, {Component} from "react"
import {FrontPage} from './components/FrontPage'
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import {LoginPage} from "./components/LoginPage";
import {RegistrationPage} from "./components/RegistrationPage";
import {MemeCommentsPage} from "./components/MemeCommentsPage";
import {Terms} from "./components/TermsPage";
import {PageNotFound} from "./components/PageNotFound";
import {RequestPasswordResetPage} from "./components/RequestPasswordResetPage";
import {RequestNewVerification} from "./components/RequestNewVerification";
import {PasswordResetPage} from "./components/PasswordResetPage";

export class App extends Component {

    render() {
        return (
            <Router>
                <Switch>
                    <Route path={"/login"} exact={true}>
                       <LoginPage/>
                    </Route>

                    <Route path={"/register"} exact={true}>
                        <RegistrationPage/>
                    </Route>

                    <Route path={"/meme/:memeID"} component={MemeCommentsPage}/>

                    <Route path={"/terms"} exact={true}>
                        <Terms/>
                    </Route>

                    <Route path={"/request_password_reset"} exact={true}>
                        <RequestPasswordResetPage/>
                    </Route>

                    <Route path={"/password_reset/:token"} component={PasswordResetPage}/>

                    <Route path={"/request_verification_resend"} exact={true}>
                        <RequestNewVerification/>
                    </Route>

                    <Route path={"/"} exact={true}>
                        <FrontPage/>
                    </Route>

                    <Route>
                        <PageNotFound/>
                    </Route>
                </Switch>
            </Router>
        )
    }
}

