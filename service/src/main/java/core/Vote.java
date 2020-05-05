package core;

import java.util.UUID;

public class Vote {

    private final UUID memeID;
    private final String username;
    private final boolean upvoted;

    public Vote(UUID memeID, String username, boolean upvoted) {
        this.memeID = memeID;
        this.username = username;
        this.upvoted = upvoted;
    }

    public UUID getMemeID() {
        return memeID;
    }

    public String getUsername() {
        return username;
    }

    public boolean isUpvoted() {
        return upvoted;
    }
}
