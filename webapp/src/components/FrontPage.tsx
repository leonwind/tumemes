import 'bootstrap/dist/css/bootstrap.css';
import React, {Component} from 'react'
import {MemeInfo} from "./Meme";
import {Meme} from "../types";
import {getMemes} from "../service/memeService";
import {Link} from "react-router-dom";


interface State {
    memes: Meme[],
}


export class FrontPage extends Component<{}, State> {

    constructor(props: any) {
        super(props);
        this.state = {memes: []};
    }

    componentDidMount() {
        getMemes().then((memes: Meme[]) => {
            this.setState({memes});
        });
    }

    render() {
        const allMemes = this.state.memes.map((meme: Meme) => <MemeInfo key={meme.memeID} meme={meme}/>);
        return (
            <div>
                <Link to={"/upload"}>Upload</Link>
                {allMemes}
            </div>
        );
    }
}

