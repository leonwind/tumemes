package resources;

import accessors.UserDAO;
import api.RegisterService;
import core.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

public class RegisterResource implements RegisterService {

  private final UserDAO userDAO;
  private final Random random;
  private final int iterations = 65536; // 65536 = 2^16
  private final int saltLen = 16;
  private final int keyLen = 128;

  public RegisterResource(UserDAO userDAO) {
    this.userDAO = userDAO;
    random = new Random();
  }

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerUser(User newUser) {
    String saltEncoded, hashEncoded;

    if (userDAO.doesUsernameExist(newUser.getName())
        || userDAO.doesEmailExists(newUser.getEmail())) {
      return Response.status(400).entity("Username or Email does already " + "exist").build();
    }
    byte[] salt = new byte[saltLen];
    random.nextBytes(salt);
    KeySpec spec = new PBEKeySpec(newUser.getPassword().toCharArray(), salt,
        iterations, keyLen);
    try {
      SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
      byte[] hash = f.generateSecret(spec).getEncoded();

      Base64.Encoder enc = Base64.getEncoder();
      saltEncoded = enc.encodeToString(salt);
      hashEncoded = enc.encodeToString(hash);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
    }


    return Response.ok().build();
  }
}
