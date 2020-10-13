import 'bootstrap/dist/css/bootstrap.css';
import React from 'react'
import styles from "../styles/NavigationBar.css"
import Button from "react-bootstrap/Button";
import {useHistory} from 'react-router-dom';


export const NavigationBar = () => {
    const history = useHistory();

    const logOut = ()  => {
        window.localStorage.clear();
        history.push("/login");
    }

    const upload = () => {
        history.push("/upload");
    }

    return (
        <div className={styles.navigationBar}>
            <nav className={"navbar"}>
                <span className={"navbar-brand"}>TUMemes</span>

                <Button variant={"primary"} className={styles.navBarButton} onClick={logOut}>
                   Log out
                </Button>

                <Button variant={"primary"} className={styles.navBarButton} onClick={upload}>
                    Upload
                </Button>
            </nav>
        </div>
    );
}