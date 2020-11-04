import React, {Component} from "react"
import {FrontPage} from './components/FrontPage'
import {Route, Switch} from "react-router-dom";
import {LoginPage} from "./components/LoginPage";
import {RegistrationPage} from "./components/RegistrationPage";
import {MemeCommentsPage} from "./components/MemeCommentsPage";
import {Terms} from "./components/TermsPage";
import {PageNotFound} from "./components/PageNotFound";
import {RequestPasswordResetPage} from "./components/RequestPasswordResetPage";
import {RequestNewVerification} from "./components/RequestNewVerification";
import {PasswordResetPage} from "./components/PasswordResetPage";
import {ValidateAccountPage} from "./components/ValidateAccountPage";
import {MemesByUserPage} from "./components/MemesByUserPage";
import {Router} from "react-router";
import history from "./customHistory";


export class App extends Component {

    render() {
        return (
            <Router history={history}>
            <Switch>
                <Route path={"/"} component={FrontPage} exact={true}/>

                <Route path={"/login"} component={LoginPage} exact={true}/>

                <Route path={"/register"} component={RegistrationPage} exact={true}/>

                <Route path={"/meme/:memeID"} component={MemeCommentsPage}/>

                <Route path={"/user/:username"} component={MemesByUserPage}/>

                <Route path={"/terms"} component={Terms} exact={true}/>

                <Route path={"/request_password_reset"} component={RequestPasswordResetPage}
                       exact={true}/>

                <Route path={"/request_verification_resend"} component={RequestNewVerification}
                       exact={true}/>

                <Route path={"/password_reset/:token"} component={PasswordResetPage}/>

                <Route path={"/verification/:token"} component={ValidateAccountPage}/>

                <Route path={"*"} component={PageNotFound}/>
            </Switch>
            </Router>
        )
    }
}

