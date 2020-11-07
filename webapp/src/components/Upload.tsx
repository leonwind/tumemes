import React, {Component, FormEvent} from "react";
import 'bootstrap/dist/css/bootstrap.css';
import {MemeService} from "../service/memeService";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import styles from "../styles/Upload.css"

interface State {
    title: string,
    image: null,
    errorMessage: string
}

interface Props {
   closeModal: () => void
}

export class Upload extends Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            title: "",
            image: null,
            errorMessage: ""
        };

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

    private handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();

        MemeService.uploadMeme({
            title: this.state.title, image: this.state.image
        }).then((ans: Response) => {
           if (ans.ok) {
               this.props.closeModal();
               return;
           }

           ans.text()
               .then((data: string) => {
                    this.setState({errorMessage: data});
               })
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

                <p className={styles.errorMessage}>
                    {this.state.errorMessage}
                </p>

                <Button className={"float-right"} type={"submit"}>
                   Post
                </Button>
            </Form>
        );
    }
}
