import React, {Component} from "react"
import Memes from './components/memes'

class App extends Component {
    state = {
        memes: []
    }
    componentDidMount() {
        fetch("http://localhost:8080/memes")
            .then(res => res.json())
            .then((data) => {
                this.setState({memes: data})
        })
            .catch(console.log)
    }

    render() {
        return (
            <Memes memes={this.state.memes} />
        )
    }
}

export default App

