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
  public RegisterResource(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerUser(User newUser) {
    if (userDAO.doesUsernameExist(newUser.getName())) {
      return Response.status(400).entity("Username does already exists").build();
    }
    if (userDAO.doesEmailExists(newUser.getEmail())) {
      return Response.status(400).entity("Email does already exists").build();
    }

    final int NUM_ITERATIONS = 65536; // 65536 = 2^16
    final int SALT_LENGTH = 16;
    final int KEY_LENGTH = 128;

    // generate salt
    byte[] salt = new byte[SALT_LENGTH];
    new Random().nextBytes(salt);

    KeySpec spec =
        new PBEKeySpec(newUser.getPassword().toCharArray(), salt, NUM_ITERATIONS, KEY_LENGTH);

    try {
      SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
      byte[] hash = f.generateSecret(spec).getEncoded();

      Base64.Encoder enc = Base64.getEncoder();

      userDAO.addNewUser(
          newUser.getName(),
          newUser.getEmail(),
          enc.encodeToString(hash),
          enc.encodeToString(salt));

      return Response.ok().build();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      return Response.status(400).build();
    }
  }
}
