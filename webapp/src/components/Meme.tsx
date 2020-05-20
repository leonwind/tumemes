import 'bootstrap/dist/css/bootstrap.css';
import React, {Component} from 'react'
import {Meme} from "../types";
import {MemeService} from "../service/memeService";

interface Props {
    meme: Meme,
}

interface State {
    /**
     *  currVote represents if meme got voted by active user
     * 0 => not voted yet
     * 1 => upvoted
     * -1 => downvoted
     */
    currVote: number,
}

export class MemeInfo extends Component<Props, State> {

   constructor(props: Props) {
       super(props);
       this.state = {currVote: 0};

       this.upvote = this.upvote.bind(this);
       this.downvote = this.downvote.bind(this);
   }

   private upvote() {
      if (this.state.currVote === 1) {
          return;
      }
      MemeService.upvoteMeme(this.props.meme.memeID)
          .then(() => {
              this.props.meme.voteCount++;
              this.setState({currVote: 1});
          });
   }

   private downvote() {
        if (this.state.currVote === -1) {
            return;
        }
        MemeService.downvoteMeme(this.props.meme.memeID)
            .then(() => {
                this.props.meme.voteCount--;
                this.setState({currVote: -1});
            });
   }

   render() {
       return (
           <div>
               <div className={"card"} style={{width: "50%"}}>
                   <h5 className={"card-title"}>{this.props.meme.title}</h5>
                   <h6 className={"card-subtitle mb-2 text-muted"}>Posted by {this.props.meme.author}</h6>
                   <img className="card-img-bottom" src={"http://localhost:8080/" + this.props.meme.imagePath}
                        alt={"Excellent meme"}/>
                   <div className="card-body">
                       {this.props.meme.voteCount} points
                       <button onClick={this.upvote}>Upvote</button>
                       <button onClick={this.downvote}>Downvote</button>
                   </div>
               </div>
           </div>
       );
   }
}
