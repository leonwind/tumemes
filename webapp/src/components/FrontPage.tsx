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

interface State {
    memes: Meme[],
    sortByNew: boolean,
    redirect: boolean
}

export class FrontPage extends Component<{}, State> {

    constructor(props: any) {
        super(props);
        this.state = {
            memes: [],
            sortByNew: true,
            redirect: false
        };

        this.handleSortByNew = this.handleSortByNew.bind(this);
        this.handleSortByPoints = this.handleSortByPoints.bind(this);
    }

    private handleSortByNew() {
        this.setState({sortByNew: true},
            () => {this.loadMemes()});
    }

    private handleSortByPoints() {
        this.setState({sortByNew: false},
            () => {this.loadMemes()});
    }

    componentDidMount() {
       this.loadMemes();
    }

    private loadMemes() {
        let queryParam: string = "";
        if (this.state.sortByNew) {
            queryParam = "?sortBy=created";
        }

        MemeService.getMemes(queryParam)
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
                        <ToggleButtonGroup type="radio" name="sortBy" defaultValue={1}>
                            <ToggleButton value={1} onClick={this.handleSortByNew}>
                                <AccessTime/> {" "}
                                NEW
                            </ToggleButton>
                            <ToggleButton value={2} onClick={this.handleSortByPoints}>
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

