export class SecurePassword {

    private static isDigit(char: string): boolean {
        return char >= '0' && char <= '9';
    }

    public static isSecure(password: string): boolean {
        if (password.length < 8) {
            return false;
        }

        let containsDigit: boolean = false;
        let containsLowerCase: boolean = false;
        let containsUpperCase: boolean = false;

        for (let i = 0; i < password.length; i++) {
            let curr: string = password.charAt(i);

            if (SecurePassword.isDigit(curr)) {
                containsDigit = true;
                continue;
            }

            if (curr === curr.toLowerCase()) {
                containsLowerCase = true;
                continue;
            }

            if (curr === curr.toUpperCase()) {
                containsUpperCase = true;
            }

            // if everything satisfied, return true before finishing loop
            if (containsDigit && containsLowerCase && containsUpperCase) {
                return true;
            }
        }
        return containsDigit && containsLowerCase && containsUpperCase;
    }
}