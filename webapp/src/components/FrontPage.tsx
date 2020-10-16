import 'bootstrap/dist/css/bootstrap.css';
import React, {Component} from 'react'
import {MemeCard} from "./Meme";
import {Meme} from "../types";
import {MemeService} from "../service/memeService";
import {NavigationBar} from "./NavigationBar";
import {Redirect} from "react-router";

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
                console.log(ans.status);
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
            })
    }

    render() {
        if (this.state.redirect) {
            return (<Redirect to={"/login"}/>);
        }

        const allMemes = this.state.memes.map((meme: Meme) => <MemeCard key={meme.memeID} meme={meme}/>);
        return (
            <div>
                <NavigationBar/>
                {allMemes}
            </div>
        );
    }
}

