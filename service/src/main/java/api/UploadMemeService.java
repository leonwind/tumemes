package api;

import core.Meme;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

@Path("/memes")
public interface UploadMemeService {

    @Path("/upload")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
    Response uploadMeme(
            @FormDataParam("file") InputStream inputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("meme") Meme meme) throws IOException;

}
