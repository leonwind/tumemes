package accessors;

import core.Meme;

import java.io.*;
import java.util.UUID;

public class MemeImageDAO {
    private static final String MEME_FILE_LOCATION = "memeImages/";

    public static void saveImage(InputStream image, Meme meme) throws IOException {
        int read = 0;
        byte[] bytes = new byte[1024];

        File savedImage = new File(MEME_FILE_LOCATION + meme.getMemeID().toString());

        OutputStream outputStream = new FileOutputStream(savedImage);

        while ((read = image.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }

        outputStream.flush();
        outputStream.close();
    }

    public static String getMemeImagePath(UUID memeID) {
       return MEME_FILE_LOCATION + memeID;
    }

}
