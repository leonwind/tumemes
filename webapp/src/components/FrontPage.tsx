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
import debounce from "lodash/debounce";

interface State {
    memes: Meme[],
    memeIDs: Set<string>,
    sortByNew: boolean,
    hasMore: boolean,
    redirect: boolean
}

export class FrontPage extends Component<{}, State> {

    constructor(props: any) {
        super(props);
        this.state = {
            memes: [],
            memeIDs: new Set(),
            sortByNew: true,
            hasMore: true,
            redirect: false
        };

        this.handleSortByNew = this.handleSortByNew.bind(this);
        this.handleSortByPoints = this.handleSortByPoints.bind(this);

        window.onscroll = debounce(() => {
            if (!this.state.hasMore) {
                return;
            }

            if (window.innerHeight + document.documentElement.scrollTop
                === document.documentElement.offsetHeight) {
                this.loadMemes();
            }
        }, 500);
    }

    private handleSortByNew() {
        this.setState({
                sortByNew: true,
                memes: [],
                memeIDs: new Set(),
                hasMore: true
            },
            () => {
                this.loadMemes()
            });
    }

    private handleSortByPoints() {
        this.setState({
                sortByNew: false,
                memes: [],
                memeIDs: new Set(),
                hasMore: true
            },
            () => {
                this.loadMemes()
            });
    }

    componentDidMount() {
        this.loadMemes();
    }

    private loadMemes() {
        console.log("LOAD MORE");
        let sortByParam: string = "";
        let limitParamValue: number = Number.MAX_SAFE_INTEGER;

        if (this.state.sortByNew) {
            sortByParam = "&sortBy=created";

            if (this.state.memes.length > 0) {
                limitParamValue = this.state.memes[this.state.memes.length - 1].created;
            }
        } else if (this.state.memes.length > 0) {
            limitParamValue = this.state.memes[this.state.memes.length - 1].voteCount;
        }

        MemeService.getMemes(limitParamValue, sortByParam)
            .then((ans: Response) => {
                if (ans.ok) {
                    const memesPromise: Promise<Meme[]> = ans.json();

                    memesPromise.then((memes: Meme[]) => {
                        if (memes.length === 0) {
                            this.setState({hasMore: false});
                            return;
                        }

                        let newMemes: Meme[] = this.state.memes;

                        memes.forEach((newMeme: Meme) => {
                            if (!this.state.memeIDs.has(newMeme.memeID)) {
                                this.state.memeIDs.add(newMeme.memeID);
                                newMemes.push(newMeme);
                            }
                        });
                        this.setState({memes: newMemes});
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

