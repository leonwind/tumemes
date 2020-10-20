import Button from "react-bootstrap/Button";
import React from "react";

export class VoteButtons {

    public static createVoteButtons(
        currVote: number,
        upvoteButtonIcon: JSX.Element,
        downvoteButtonIcon: JSX.Element,
        upvoteFunc: () => void,
        downvoteFunc: () => void):
        {upvote: JSX.Element, downvote: JSX.Element} {

        let upvoteButton: JSX.Element;
        let downvoteButton: JSX.Element;

        // if comment is upvoted
        if (currVote === 1) {
            upvoteButton =
                <Button variant={"success"} onClick={upvoteFunc}>
                    {upvoteButtonIcon}
                </Button>;

            downvoteButton =
                <Button variant={"outline-secondary"} onClick={downvoteFunc}>
                    {downvoteButtonIcon}
                </Button>
        }

        // if comment is downvoted
        else if (currVote === -1) {
            upvoteButton =
                <Button variant={"outline-secondary"} onClick={upvoteFunc}>
                    {upvoteButtonIcon}
                </Button>;

            downvoteButton =
                <Button variant={"danger"} onClick={downvoteFunc}>
                    {downvoteButtonIcon}
                </Button>
        }

        // if comment is not yet voted on
        else {
            upvoteButton =
                <Button variant={"outline-secondary"} onClick={upvoteFunc}>
                    {upvoteButtonIcon}
                </Button>;

            downvoteButton =
                <Button variant={"outline-secondary"} onClick={downvoteFunc}>
                    {downvoteButtonIcon}
                </Button>
        }

        return {
            upvote: upvoteButton,
            downvote: downvoteButton
        };
    }
}