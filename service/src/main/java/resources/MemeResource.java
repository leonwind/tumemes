package resources;

import accessors.MemeDAO;
import accessors.MemeImageDAO;
import api.MemeService;
import core.Meme;
import core.NewMeme;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

public class MemeResource implements MemeService {

  private final MemeDAO memeDAO;

  public MemeResource(MemeDAO memeDAO) {
    this.memeDAO = memeDAO;
  }

  @Override
  public Response uploadMeme(
          @FormDataParam("file") InputStream inputStream,
          @FormDataParam("file") FormDataContentDisposition fileDetail,
          @FormDataParam("meme")NewMeme newMeme) {

    if (inputStream == null || fileDetail == null || newMeme == null) {
      return Response.status(400).entity("Uploaded object is null").build();
    }

    Meme meme = Meme.fromNewMeme(newMeme);

    try {
      MemeImageDAO.saveImage(inputStream, meme);
    } catch (IOException e) {
      e.printStackTrace();
      return Response.status(400).entity("Error while saving new image").build();
    }

    System.out.println("SAVED MEME SUCCESSFULLY");

    memeDAO.insert(
        meme.getMemeID().toString(),
        meme.getTitle(),
        meme.getAuthor(),
        meme.getVoteCount(),
        meme.getCreated());

    return Response.ok("Uploaded meme successfully").build();
  }
}
