import React, {Component} from "react";
import {Comment} from "../types";
import styles from "../styles/Comment.css";
import {Card} from "react-bootstrap";
import {HumanReadableTimeDiff} from "./HumanReadableTimeDiff";

interface Props {
    reply: Comment
}

interface State {
}

export class ReplyCard extends Component<Props, State> {
    private readonly timeDiff: string;

   constructor(props: Props) {
       super(props);

       this.timeDiff = HumanReadableTimeDiff.calculateTimeDiff(this.props.reply.created);
   }

   render() {
       return (
           <div className={styles.replyCard}>
               <Card className={"mt-1"}>
                   <Card.Title className={"text-muted"}>
                       {this.props.reply.author} {" · "} {this.timeDiff} ago
                   </Card.Title>

                   {this.props.reply.content}
               </Card>
           </div>
       );
   }
}