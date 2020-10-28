import React, {Component} from "react"
import {FrontPage} from './components/FrontPage'
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import {LoginPage} from "./components/LoginPage";
import {Registration} from "./components/Registration";
import {MemeCommentsPage} from "./components/MemeCommentsPage";
import {Terms} from "./components/TermsPage";
import {PageNotFound} from "./components/PageNotFound";
import {ResetPasswordPage} from "./components/ResetPasswordPage";
import {ResendVerificationMailPage} from "./components/ResendVerificationMailPage";

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

                    <Route path={"/password_reset"} exact={true}>
                        <ResetPasswordPage/>
                    </Route>

                    <Route path={"/verification_resend"} exact={true}>
                        <ResendVerificationMailPage/>
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

