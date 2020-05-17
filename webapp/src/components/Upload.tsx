import React, {Component} from "react";
import 'bootstrap/dist/css/bootstrap.css';
import {uploadMeme} from "../service/memeService";

interface State {
    title: string,
    image: null,
}

export class Upload extends Component<{}, State> {

    constructor(props: any) {
        super(props);
        this.state = {title: "", image: null};

        this.handleTitleChange = this.handleTitleChange.bind(this);
        this.handleImageChange = this.handleImageChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleTitleChange(event: any) {
        this.setState({title: event.target.value})
    }

    handleImageChange(event: any) {
        this.setState({image: event.target.files[0]})
    }

    handleSubmit(event: any) {
        uploadMeme({title: this.state.title, image: this.state.image})
    }

    render() {
        return (
            <div>
                <label>
                    Title:
                    <input type="text" value={this.state.title} onChange={this.handleTitleChange}/>

                    <input type="file" name="file" onChange={this.handleImageChange}/>
                </label>
                <button onClick={this.handleSubmit}>Submit</button>
            </div>
        );
    }
}
