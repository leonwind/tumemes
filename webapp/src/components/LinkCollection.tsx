import styles from "../styles/LinkCollection.css";
import React from "react";

export const LinkCollection = () => {
    return (
        <>
            <hr className={"m-4"}/>
            <div className={styles.linkCollection}>
                <a href={"mailto:contact@tumemes.de"}>Contact</a> {" · "}
                <a href={"terms"}>Terms</a> {" · "}
                <a href={"https://github.com/leonwind/tumemes"}>Code</a>
            </div>
        </>
    )
}