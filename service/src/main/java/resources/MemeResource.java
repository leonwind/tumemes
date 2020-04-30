package resources;

import api.MemeService;
import javax.ws.rs.core.Response;
import models.Meme;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.*;

public class MemeResource implements MemeService {

    private static final String MEME_FILE_LOCATION = "./../../resources/memes";

    @Override
    public Response uploadMeme(
            @FormDataParam("file") InputStream inputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("meme") Meme meme) throws IOException {

        saveImage(inputStream, fileDetail.getName());
        inputStream.close();

        return Response.ok(meme.toString()).build();
    }

    private void saveImage(InputStream newMeme, String memeID) {
        final String path = MEME_FILE_LOCATION + memeID;
        try {
            int read = 0;
            byte[] bytes = new byte[1024];

            OutputStream outputStream = new FileOutputStream(new File(path));
            while ((read = newMeme.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response listMemes() {
        return Response.ok().build();
    }
}
