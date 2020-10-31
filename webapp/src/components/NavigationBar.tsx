import 'bootstrap/dist/css/bootstrap.css';
import React, {useState} from 'react'
import styles from "../styles/NavigationBar.css"
import Button from "react-bootstrap/Button";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import {AddRounded, ExitToApp, InsertPhoto, Lock, Person} from "@material-ui/icons";
import {Upload} from "./Upload";
import Modal from "react-bootstrap/Modal";
import Dropdown from "react-bootstrap/Dropdown";
import history from "../customHistory";

export const NavigationBar = () => {
    const [show, setShow]: [boolean, any] = useState(false);

    const handleShow = () => {
        setShow(true);
    }

    const handleClose = () => {
        setShow(false);
    }

    const logOut = () => {
        window.localStorage.clear();
        history.push("/login");
    }

    const visitUsersMemes = () => {
        history.push("/user/" + window.localStorage.getItem("username"));
    }

    const resetPassword = () => {
        history.push("/password_reset/" + window.localStorage.getItem("access_token"));
    }

    const visitTerms = () => {
        history.push("/terms");
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

                            <Dropdown className={"ml-1"} alignRight>
                                <Dropdown.Toggle className={styles.navBarButton}
                                                 id={"dropdown-menu-align-right"}>
                                    <Person/> {" "}
                                    {window.localStorage.getItem("username")}
                                </Dropdown.Toggle>

                                <Dropdown.Menu>
                                    <Dropdown.Item onSelect={visitUsersMemes}>
                                        <InsertPhoto/> {" "}
                                        Your memes
                                    </Dropdown.Item>

                                    <Dropdown.Item onSelect={resetPassword}>
                                        <Lock/> {" "}
                                        Change your password
                                    </Dropdown.Item>

                                    <Dropdown.Item onSelect={logOut}>
                                        <ExitToApp/> {" "}
                                        Sign out
                                    </Dropdown.Item>

                                    <Dropdown.Divider/>
                                    <Dropdown.Item onSelect={visitTerms}>
                                        Terms
                                    </Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
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