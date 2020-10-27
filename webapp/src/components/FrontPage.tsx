import 'bootstrap/dist/css/bootstrap.css';
import React, {Component} from 'react'
import {MemeCard} from "./MemeCard";
import {Meme} from "../types";
import {MemeService} from "../service/memeService";
import {NavigationBar} from "./NavigationBar";
import {Redirect} from "react-router";
import styles from "../styles/FrontPage.css"
import ToggleButtonGroup from "react-bootstrap/ToggleButtonGroup";
import ToggleButton from "react-bootstrap/ToggleButton";
import Card from "react-bootstrap/Card";
import {AccessTime, TrendingUp} from "@material-ui/icons";
import ForwardIcon from "@material-ui/icons/Forward";

interface State {
    memes: Meme[],
    redirect: boolean
}

export class FrontPage extends Component<{}, State> {

    constructor(props: any) {
        super(props);
        this.state = {
            memes: [],
            redirect: false
        };
    }

    componentDidMount() {
        MemeService.getMemes()
            .then((ans: Response) => {
                if (ans.ok) {
                    const memesPromise: Promise<Meme[]> = ans.json();
                    memesPromise.then((memes: Meme[]) => {
                        this.setState({memes});
                    })
                    return;
                }

                // if unauthorized redirect to login
                if (ans.status === 401) {
                    this.setState({redirect: true});
                    return;
                }

                throw new Error(ans.statusText);
            });
    }

    render() {
        if (this.state.redirect) {
            return (<Redirect to={"/login"}/>);
        }

        const allMemes: JSX.Element[] = this.state.memes.map((meme: Meme) =>
            <MemeCard key={meme.memeID} meme={meme} showCommentsButton={true}/>);

        return (
            <div className={styles.body}>
                <NavigationBar/>

                <div className={styles.sortByCard}>
                    <Card className={"mt-4"}>
                        <ToggleButtonGroup type="radio" name="options" defaultValue={1}>
                            <ToggleButton value={1}>
                                <AccessTime/> {" "}
                                NEW
                            </ToggleButton>
                            <ToggleButton value={2}>
                                <TrendingUp/> {" "}
                                POINTS
                            </ToggleButton>
                        </ToggleButtonGroup>
                    </Card>
                </div>

                {allMemes}
            </div>
        );
    }
}

