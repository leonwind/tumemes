import Button from "react-bootstrap/Button";
import React from "react";

export class VoteButtons {

    /**
     * Render the upvote and downvote buttons
     * with the correct color depending on the current
     * vote on the object (either meme or comment / reply)
     */
    public static createVoteButtons(
        currVote: number,
        upvoteButtonIcon: JSX.Element,
        downvoteButtonIcon: JSX.Element,
        upvoteFunc: () => void,
        downvoteFunc: () => void,
        stylesUpvote: string = "",
        stylesNoVote: string = "",
        stylesDownvote: string = ""): { upvote: JSX.Element, downvote: JSX.Element } {

        let upvoteButton: JSX.Element;
        let downvoteButton: JSX.Element;

        // if object is upvoted
        if (currVote === 1) {
            upvoteButton =
                <Button variant={"success"}
                        className={stylesUpvote}
                        onClick={upvoteFunc}
                        size={"sm"}>
                    {upvoteButtonIcon}
                </Button>;

            downvoteButton =
                <Button variant={"outline-secondary"}
                        className={stylesNoVote}
                        onClick={downvoteFunc}
                        size={"sm"}>
                    {downvoteButtonIcon}
                </Button>
        }

        // if object is downvoted
        else if (currVote === -1) {
            upvoteButton =
                <Button variant={"outline-secondary"}
                        className={stylesNoVote}
                        onClick={upvoteFunc}
                        size={"sm"}>
                    {upvoteButtonIcon}
                </Button>;

            downvoteButton =
                <Button variant={"danger"}
                        className={stylesDownvote}
                        onClick={downvoteFunc}
                        size={"sm"}>
                    {downvoteButtonIcon}
                </Button>
        }

        // if object is not yet voted on
        else {
            upvoteButton =
                <Button variant={"outline-secondary"}
                        className={stylesNoVote}
                        onClick={upvoteFunc}
                        size={"sm"}>
                    {upvoteButtonIcon}
                </Button>;

            downvoteButton =
                <Button variant={"outline-secondary"}
                        className={stylesNoVote}
                        onClick={downvoteFunc}
                        size={"sm"}>
                    {downvoteButtonIcon}
                </Button>
        }

        return {
            upvote: upvoteButton,
            downvote: downvoteButton
        };
    }
}