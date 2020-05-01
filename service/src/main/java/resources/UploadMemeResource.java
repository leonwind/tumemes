package resources;

import accessors.MemeDAO;
import api.UploadMemeService;
import models.Meme;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.core.Response;
import java.io.*;

public class UploadMemeResource implements UploadMemeService {

    private static final String MEME_FILE_LOCATION = "src/main/resources/memes/";

    private final MemeDAO memeDAO;

    public UploadMemeResource(MemeDAO memeDAO) {
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
            saveImage(inputStream, fileDetail.getFileName());
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(400)
                    .entity("Error while saving new image")
                    .build();
        }

        return Response.ok("Uploaded meme successfully").build();
    }

    private void saveImage(InputStream newMeme, String memeID) throws IOException {
        final String path = MEME_FILE_LOCATION + memeID;
        int read = 0;
        byte[] bytes = new byte[1024];

        OutputStream outputStream = new FileOutputStream(new File(path));
        while ((read = newMeme.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        outputStream.flush();
        outputStream.close();
    }
}
