import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

public class sendFileClient {

  @Test
  public void testGetIt() {

    final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
    WebTarget t = client.target("http://localhost:8080/upload");

    String fileName = "meme1.jpg";

    File f = new File(fileName);

    FileDataBodyPart filePart = new FileDataBodyPart("file", f);
    filePart.setContentDisposition(
        FormDataContentDisposition.name("file").fileName(fileName).build());

    String meme = "{\"title\": \"newMeme\", \"author\": \"leon\"}";

    MultiPart multipartEntity =
        new FormDataMultiPart()
            .field("meme", meme, MediaType.APPLICATION_JSON_TYPE)
            .bodyPart(filePart);

    Response response =
        t.request().post(Entity.entity(multipartEntity, multipartEntity.getMediaType()));
    System.out.println(response.getStatus());
    System.out.println(response.readEntity(String.class));

    response.close();
  }
}
