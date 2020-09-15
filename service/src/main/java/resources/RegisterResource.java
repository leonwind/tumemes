package resources;

import accessors.UserDAO;
import api.RegisterService;
import auth.Hashing;
import core.NewUser;
import enums.AllowedEmailDomains;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

public class RegisterResource implements RegisterService {

  private final UserDAO userDAO;
  private final String secretKey;

  public RegisterResource(UserDAO userDAO, String secretKey) {
    this.userDAO = userDAO;
    this.secretKey = secretKey;
  }

  private static boolean isEmailDomainSupported(String email) {
    String domain = email.substring(email.indexOf('@') + 1);
    return Arrays.stream(AllowedEmailDomains.values())
        .anyMatch(allowedDomain -> allowedDomain.toString().equals(domain));
  }

  private static boolean isSecure(String password) {
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

  private String createToken(String subject) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    long currMillis = System.currentTimeMillis();
    Date currDate = new Date(currMillis);

    // one week until token gets expired
    // time in milli seconds
    final long ttl_millis = 604800000;
    Date expirationDate = new Date(currMillis + ttl_millis);

    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(this.secretKey);

    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    JwtBuilder builder =
        Jwts.builder()
            .setIssuedAt(currDate)
            .setExpiration(expirationDate)
            .setSubject(subject)
            .signWith(signatureAlgorithm, signingKey);
    return builder.compact();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerUser(NewUser newUser) {
    System.out.println("REGISTER NEW USER");
    System.out.println(newUser);

    if (userDAO.doesUsernameExist(newUser.getName())) {
      System.out.println("Username exits");
      return Response.status(400).entity("Username does already exists").build();
    }

    if (!isEmailDomainSupported(newUser.getEmail())) {
      System.out.println("Email not TUM");
      return Response.status(400).entity("Email is not from TUM.").build();
    }

    if (userDAO.doesEmailExists(newUser.getEmail())) {
      System.out.println("Email exists");
      return Response.status(400).entity("Email does already exists").build();
    }

    if (!isSecure(newUser.getPassword())) {
      System.out.println("Password weak");
      return Response.status(400)
          .entity(
              "Password should be at least 8 characters and contains one digit, "
                  + "one lowercase and one uppercase character.")
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

      String token = createToken(newUser.getEmail());
      System.out.print("All good. Build token");
      return Response.ok(token).build();

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      return Response.status(400).entity("Error occurred while hashing your password.").build();
    }
  }
}
