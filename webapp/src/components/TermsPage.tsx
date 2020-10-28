import React from 'react'
import navBarStyles from "../styles/NavigationBar.css";
import styles from "../styles/Terms.css"
import Navbar from "react-bootstrap/Navbar";
import {Email, GitHub, Phone} from "@material-ui/icons";

export const Terms = () => {
    return (
        <div className={styles.body}>
            <Navbar className={navBarStyles.navigationBar} expand={"md"} variant={"dark"}>
                <Navbar.Brand href={"/"} className={navBarStyles.navBarBrand}>
                    TUMemes
                </Navbar.Brand>
            </Navbar>

            <div className={styles.terms}>
                <h1 className={"mb-3 mt-3"}>Impressum</h1>

                <h3>Angaben gemäß § 5 TMG</h3>
                <p>
                    Leon Windheuser <br/>
                    Hans-Leipelt-Straße 6 <br/>
                    80805 München <br/> <br/>
                </p>

                <h3>Source-Code </h3>
                <p>
                    <GitHub fontSize={"small"}/>
                    <a href={"https://github.com/leonwind/tumemes"}>
                        {" "}
                        https://github.com/leonwind/tumemes
                    </a> <br/> <br/>
                </p>

                <h3>Kontakt</h3>
                <p>
                    <Phone fontSize={"small"}/> +49 179 4099622 <br/>
                    <Email fontSize={"small"}/> <a href={"mailto:contact@tumemes.de"}>contact@tumemes.de</a>
                    <br/> <br/>
                </p>

                <h3>Haftung für Inhalte</h3>
                <p>
                    Als Diensteanbieter sind wir gemäß § 7 Abs.1 TMG für eigene Inhalte auf diesen Seiten
                    nach den allgemeinen Gesetzen verantwortlich. Nach §§ 8 bis 10 TMG
                    sind wir als Diensteanbieter jedoch nicht verpflichtet, übermittelte oder
                    gespeicherte fremde Informationen zu überwachen oder nach Umständen zu forschen,
                    die auf eine rechtswidrige Tätigkeit hinweisen. <br/> <br/>

                    Verpflichtungen zur Entfernung oder Sperrung der Nutzung von Informationen
                    nach den allgemeinen Gesetzen bleiben hiervon unberührt.
                    Eine diesbezügliche Haftung ist jedoch erst ab dem Zeitpunkt der
                    Kenntnis einer konkreten Rechtsverletzung möglich.
                    Bei Bekanntwerden von entsprechenden
                    Rechtsverletzungen werden wir diese Inhalte umgehend entfernen.
                </p>

                <h3>Haftung für Links</h3>
                <p>
                    Unser Angebot enthält Links zu externen Websites Dritter, auf deren Inhalte wir
                    keinen Einfluss haben. Deshalb können wir für diese fremden Inhalte auch
                    keine Gewähr übernehmen. Für die Inhalte der verlinkten Seiten ist stets der
                    jeweilige Anbieter oder Betreiber der Seiten verantwortlich. Die verlinkten
                    Seiten wurden zum Zeitpunkt der Verlinkung auf mögliche Rechtsverstöße überprüft.
                    Rechtswidrige Inhalte waren zum Zeitpunkt der Verlinkung nicht erkennbar. <br/> <br/>

                    Eine permanente inhaltliche Kontrolle der verlinkten Seiten ist jedoch ohne
                    konkrete Anhaltspunkte einer Rechtsverletzung nicht zumutbar.
                    Bei Bekanntwerden von Rechtsverletzungen werden wir derartige Links umgehend entfernen.
                </p>

                <h3>Urheberrecht</h3>
                <p>
                    Soweit die Inhalte auf dieser Seite nicht vom Betreiber erstellt wurden,
                    werden die Urheberrechte Dritter beachtet.
                    Insbesondere werden Inhalte Dritter als solche gekennzeichnet.
                    Sollten Sie trotzdem auf eine Urheberrechtsverletzung aufmerksam werden,
                    bitten wir um einen entsprechenden Hinweis.
                    Bei Bekanntwerden von Rechtsverletzungen werden wir derartige Inhalte umgehend entfernen.
                </p>
            </div>
        </div>
    );
}