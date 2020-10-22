import React, {Component} from "react";
import 'bootstrap/dist/css/bootstrap.css';
import {MemeService} from "../service/memeService";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

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

    private handleTitleChange(event: any) {
        this.setState({title: event.target.value});
    }

    private handleImageChange(event: any) {
        this.setState({image: event.target.files[0]});
    }

    private handleSubmit() {
        MemeService.uploadMeme({
            title: this.state.title, image: this.state.image
        }).then(() => {
        });

    }

    render() {
        return (
            <Form onSubmit={this.handleSubmit}>
                <Form.Group>
                    <Form.Label htmlFor={"inputTitle"} srOnly>Title</Form.Label>
                    <Form.Control type={"text"} id={"inputTitle"}
                                  value={this.state.title}
                                  onChange={this.handleTitleChange}
                                  placeholder={"Title"}
                                  required/>
                </Form.Group>

                <Form.Group>
                    <Form.File id={"inputFile"}
                               onChange={this.handleImageChange}
                               required/>
                </Form.Group>

                <Button className={"float-right"} type={"submit"}>
                   Post
                </Button>
            </Form>
        );
    }
}
