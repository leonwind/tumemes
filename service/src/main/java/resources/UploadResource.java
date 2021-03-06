package resources;

import accessors.MemeDAO;
import accessors.MemeImageDAO;
import api.UploadService;
import core.Meme;
import core.NewMeme;
import core.User;
import exceptions.FileExceedsLimitExceptions;
import exceptions.FileNotSupportedException;
import io.dropwizard.auth.Auth;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public class UploadResource implements UploadService {

  private static final Logger log = LoggerFactory.getLogger(UploadResource.class);
  private final MemeDAO memeDAO;

  public UploadResource(MemeDAO memeDAO) {
    this.memeDAO = memeDAO;
  }

  @Override
  @POST
  @Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
  public Response uploadMeme(
      @Auth User user,
      @FormDataParam("file") InputStream inputStream,
      @FormDataParam("file") FormDataContentDisposition fileDetail,
      @FormDataParam("meme") FormDataBodyPart newMemeJSON) {

    log.info("Upload new meme");

    // Some clients are unable to set individual content-type for each type
    // correct. Thus we need to set the content-type for newMeme to json by hand
    newMemeJSON.setMediaType(MediaType.APPLICATION_JSON_TYPE);
    NewMeme newMeme = newMemeJSON.getValueAs(NewMeme.class);

    if (inputStream == null || fileDetail == null || newMeme == null) {
      System.out.println(newMeme);
      System.out.println(inputStream);
      System.out.println(fileDetail);
      return Response.status(400).entity("Uploaded object is null").build();
    }

    Meme meme = Meme.fromNewMeme(newMeme, user.getName());
    System.out.println(meme);

    try {
      MemeImageDAO.saveImage(inputStream, fileDetail, meme);
    } catch (FileExceedsLimitExceptions | FileNotSupportedException ex) {
      return Response.status(400).entity(ex.getMessage()).build();
    } catch (Exception ex) {
      ex.printStackTrace();
      return Response.status(400).entity("Error while saving image").build();
    }

    memeDAO.insert(
        meme.getMemeID().toString(), meme.getTitle(), meme.getAuthor(), meme.getCreated());

    return Response.ok("Uploaded meme successfully").build();
  }
}
