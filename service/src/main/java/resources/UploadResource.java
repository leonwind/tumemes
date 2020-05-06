package resources;

import accessors.MemeDAO;
import accessors.MemeImageDAO;
import api.UploadService;
import core.Meme;
import core.NewMeme;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

public class UploadResource implements UploadService {

    private final MemeDAO memeDAO;

    public UploadResource(MemeDAO memeDAO) {
        this.memeDAO = memeDAO;
    }

    @Override
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
    public Response uploadMeme(
            @FormDataParam("file") InputStream inputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("meme") NewMeme newMeme) {

        if (inputStream == null || fileDetail == null || newMeme == null) {
            return Response.status(400).entity("Uploaded object is null").build();
        }

        Meme meme = Meme.fromNewMeme(newMeme);
        System.out.println(meme.toString());
        try {
            MemeImageDAO.saveImage(inputStream, meme);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(400).entity("Error while saving new image").build();
        }

        memeDAO.insert(
                meme.getMemeID().toString(),
                meme.getTitle(),
                meme.getAuthor(),
                meme.getCreated());

        return Response.ok("Uploaded meme successfully").build();
    }
}
