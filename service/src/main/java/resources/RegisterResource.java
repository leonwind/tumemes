package resources;

import accessors.UserDAO;
import api.RegisterService;
import auth.Hashing;
import core.NewUser;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class RegisterResource implements RegisterService {

  private final UserDAO userDAO;

  public RegisterResource(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  private boolean isSecure(String password) {
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

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerUser(NewUser newUser) {
    if (userDAO.doesUsernameExist(newUser.getName())) {
      return Response.status(400).entity("Username does already exists").build();
    }
    if (userDAO.doesEmailExists(newUser.getEmail())) {
      return Response.status(400).entity("Email does already exists").build();
    }

    if (newUser.getPassword().length() < 8) {
      return Response.status(400)
          .entity("Password should at least be 8 " + "characters long.")
          .build();
    }

    if (!isSecure(newUser.getPassword())) {
      return Response.status(400)
          .entity(
              "Password should contain at least one digit, one lowercase and one uppercase character.")
          .build();
    }

    byte[] salt = Hashing.generateSalt();

    try {
      byte[] hash = Hashing.generateHash(newUser.getPassword(), salt);

      Base64.Encoder enc = Base64.getEncoder();

      userDAO.addNewUser(
          newUser.getName(),
          newUser.getEmail(),
          enc.encodeToString(hash),
          enc.encodeToString(salt));

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      return Response.status(400)
          .entity("Error occurred while hashing your password.")
          .build();
    }
    return Response.ok("Added new user").build();
  }
}
