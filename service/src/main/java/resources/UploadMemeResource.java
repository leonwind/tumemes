package resources;

import api.UploadMemeService;
import core.Meme;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.core.Response;
import java.io.*;

public class UploadMemeResource implements UploadMemeService {

    private final Jdbi memeDAO;

    public UploadMemeResource(Jdbi memeDAO) {
        this.memeDAO = memeDAO;
    }

    @Override
    public Response uploadMeme(
            @FormDataParam("meme1") InputStream inputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("meme") Meme meme) {

        if (inputStream == null || fileDetail == null || meme == null) {
            return Response.status(400)
                    .entity("Uploaded object is null")
                    .build();
        }

        try {
            meme.saveMemeImage(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(400)
                    .entity("Error while saving new image")
                    .build();
        }
        return Response.ok("Uploaded meme successfully").build();
    }
}
