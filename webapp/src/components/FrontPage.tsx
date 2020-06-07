import 'bootstrap/dist/css/bootstrap.css';
import React, {Component} from 'react'
import {MemeInfo} from "./Meme";
import {Meme} from "../types";
import {MemeService} from "../service/memeService";
import {NavigationBar} from "./NavigationBar";

interface State {
    memes: Meme[],
}


export class FrontPage extends Component<{}, State> {

    constructor(props: any) {
        super(props);
        this.state = {memes: []};
    }

    componentDidMount() {
        MemeService.getMemes().then((memes: Meme[]) => {
            this.setState({memes});
        });
    }

    render() {
        const allMemes = this.state.memes.map((meme: Meme) => <MemeInfo key={meme.memeID} meme={meme}/>);
        return (
            <div>
                <NavigationBar />
                {allMemes}
            </div>
        );
    }
}

