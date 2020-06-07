import 'bootstrap/dist/css/bootstrap.css';
import React from 'react'
import styles from "../styles/NavigationBar.css"
import {Link} from "react-router-dom";

export const NavigationBar = () => {
        return (
            <div className={styles.navigationBar}>
                <nav className={"navbar"}>
                    <span className="navbar-brand">TUMemes</span>
                    <ul className="nav navbar-nav navbar-right">
                        <Link className={styles.uploadLink} to={"/upload"}>Upload</Link>
                    </ul>
                </nav>
            </div>
        );
}