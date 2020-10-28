package auth;

public class SecurePassword {

  public static boolean isSecure(String password) {
    if (password.length() < 8) {
      return false;
    }

    boolean containsDigit = false;
    boolean containsLowerCase = false;
    boolean containsUpperCase = false;

    for (int i = 0; i < password.length(); i++) {
      char curr = password.charAt(i);

      if (Character.isDigit(curr)) {
        containsDigit = true;
        continue;
      }

      if (Character.isLowerCase(curr)) {
        containsLowerCase = true;
        continue;
      }

      if (Character.isUpperCase(curr)) {
        containsUpperCase = true;
      }
    }
    return containsDigit && containsLowerCase && containsUpperCase;
  }
}
