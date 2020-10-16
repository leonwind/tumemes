import {Component} from "react";
import {Meme} from "../types";

interface State {
    meme: Meme
}

export class MemeCommentsPage extends Component<{}, State> {

    constructor(props: any) {
        super(props);
    }

    componentDidMount() {
    }
}