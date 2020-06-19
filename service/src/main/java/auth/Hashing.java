package auth;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

public class Hashing {
  private static final int NUM_ITERATIONS = 65536; // 63336 = 2^16
  private static final int SALT_LENGTH = 16;
  private static final int KEY_LENGTH = 128;

  private static byte[] convertStringToByteArray(String s) {
    return Base64.getDecoder().decode(s);
  }

  public static byte[] generateSalt() {
    byte[] salt = new byte[SALT_LENGTH];
    new Random().nextBytes(salt);
    return salt;
  }

  public static byte[] generateHash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt,
        NUM_ITERATIONS, KEY_LENGTH);

    SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
    return f.generateSecret(spec).getEncoded();
  }

  public static byte[] generateHash(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
    return generateHash(password, convertStringToByteArray(salt));
  }
}
