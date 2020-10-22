import 'bootstrap/dist/css/bootstrap.css';
import React, {useState} from 'react'
import styles from "../styles/NavigationBar.css"
import Button from "react-bootstrap/Button";
import {useHistory} from 'react-router-dom';
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import {AddRounded, ExitToApp} from "@material-ui/icons";
import {Upload} from "./Upload";
import Modal from "react-bootstrap/Modal";

export const NavigationBar = () => {
    const [show, setShow]: [boolean, any] = useState(false);
    const history = useHistory();

    const handleShow = () => {
        setShow(true);
    }

    const handleClose = () => {
        setShow(false);
    }

    const logOut = ()  => {
        window.localStorage.clear();
        history.push("/login");
    }

    return (
        <>
            <Navbar className={styles.navigationBar} expand={"md"} variant={"dark"}>
                <Navbar.Brand href={"/"} className={styles.navBarBrand}>
                    TUMemes
                </Navbar.Brand>

                <Navbar.Toggle aria-controls={"basic-navbar-nav"}/>

                <Navbar.Collapse id={"basic-navbar-nav"}>
                    <Nav className={"ml-auto"}>
                        <ButtonGroup>
                        <Button className={styles.navBarButton} onClick={handleShow}>
                            <AddRounded/> {" "}
                            Upload
                        </Button>

                        <Button className={styles.navBarButton} onClick={logOut}>
                            <ExitToApp/> {" "}
                            Sign out
                        </Button>
                        </ButtonGroup>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>

            <Modal show={show} onHide={handleClose} centered>
                <Modal.Header closeButton>
                    <h4 className={styles.modalHeadline}>
                        Upload a meme
                    </h4>
                </Modal.Header>

                <Modal.Body>
                    <Upload/>
                </Modal.Body>
            </Modal>
        </>
    );
}