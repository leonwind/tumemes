import React, {Component} from "react"
import {FrontPage} from './components/FrontPage'
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import {Upload} from "./components/Upload";
import {LoginPage} from "./components/LoginPage";
import {Registration} from "./components/Registration";

export class App extends Component {

    render() {
        return (
            <Router>
                <Switch>
                    <Route path="/login" exact={true}>
                       <LoginPage/>
                    </Route>

                    <Route path="/register" exact={true}>
                        <Registration/>
                    </Route>

                    <Route path="/upload" exact={true}>
                        <Upload/>
                    </Route>

                    <Route path="/">
                        <FrontPage/>
                    </Route>
                </Switch>
            </Router>
        )
    }
}
