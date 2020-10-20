import 'bootstrap/dist/css/bootstrap.css';
import React from 'react'
import styles from "../styles/NavigationBar.css"
import Button from "react-bootstrap/Button";
import {useHistory} from 'react-router-dom';
import Navbar from "react-bootstrap/Navbar";


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
            <Navbar className={"navbar"}>
                <Navbar.Brand href={"/"} className={styles.navBarBrand}>TUMemes</Navbar.Brand>

                <Button className={styles.navBarButton} onClick={logOut}>
                   Log out
                </Button>

                <Button className={styles.navBarButton} onClick={upload}>
                    Upload
                </Button>
            </Navbar>
        </div>
    );
}