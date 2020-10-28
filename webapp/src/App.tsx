import React, {Component} from "react"
import {FrontPage} from './components/FrontPage'
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import {LoginPage} from "./components/LoginPage";
import {Registration} from "./components/Registration";
import {MemeCommentsPage} from "./components/MemeCommentsPage";
import {Terms} from "./components/TermsPage";
import {PageNotFound} from "./components/PageNotFound";
import {RequestPasswordResetPage} from "./components/RequestPasswordResetPage";
import {RequestVerificationMailResendPage} from "./components/RequestVerificationMailResendPage";
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
                        <Registration/>
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
                        <RequestVerificationMailResendPage/>
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

