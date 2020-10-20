package core;

import java.util.UUID;

public class MemeVote {

    private final UUID memeID;
    /**
     * vote == 1 is upvote
     * vote == 0 is no vote
     * vote == -1 is downvote
     */
    private final int vote;
    private final String username;

    public MemeVote(UUID memeID, int vote, String username) {
        this.memeID = memeID;
        this.vote = vote;
        this.username = username;
    }

    public UUID getMemeID() {
        return memeID;
    }

    public int getVote() {
        return vote;
    }
}
