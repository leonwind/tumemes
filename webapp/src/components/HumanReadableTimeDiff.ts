export class HumanReadableTimeDiff {

    /**
     * Return correct singular or plural case
     */
    private static createCorrectGrammaticalNumber(interval: number, timePeriod: string): string {
        let ans: string = interval + " " + timePeriod;
        if (interval > 1) {
            ans += "s";
        }
        return ans;
    }

    /**
     * Calculate human readable time difference between a past event
     * (timeHappened) and the current time
     * @param timeHappened: timestamp when the event happened (e.g uploaded meme)
     */
    public static calculateTimeDiff(timeHappened: number): string {
        const seconds: number = Math.floor(
            (new Date().getTime() - timeHappened) / 1000);

        // 31536000 = one year in seconds
        let interval: number = Math.floor(seconds / 31536000);
        if (interval >= 1) {
            return this.createCorrectGrammaticalNumber(interval, "year");
        }

        // 2592000 = one month in seconds
        interval = Math.floor(seconds / 2592000);
        if (interval >= 1) {
            return this.createCorrectGrammaticalNumber(interval, "month");
        }

        // 604800 = one week in seconds
        interval = Math.floor(seconds / 604800);
        if (interval >= 1) {
            return this.createCorrectGrammaticalNumber(interval, "week");
        }

        // 86400 = one day in seconds
        interval = Math.floor(seconds / 86400);
        if (interval >= 1) {
            return this.createCorrectGrammaticalNumber(interval, "day");
        }

        // 3600 = one hour in seconds
        interval = Math.floor(seconds / 3600);
        if (interval >= 1) {
            return this.createCorrectGrammaticalNumber(interval, "hour");
        }

        // 60 = one minute in seconds
        interval = Math.floor(seconds / 60);
        if (interval >= 1) {
            return this.createCorrectGrammaticalNumber(interval, "minute");
        }

        return this.createCorrectGrammaticalNumber(Math.floor(seconds), "second");
    }
}