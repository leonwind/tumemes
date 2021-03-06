package accessors;

import core.Meme;
import enums.SupportedFiles;
import exceptions.FileExceedsLimitExceptions;
import exceptions.FileNotSupportedException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;

public class MemeImageDAO {

  private static String MEME_FILE_LOCATION;
  // Store all images as .jpg
  // Real image extension does not matter since all browsers only care about
  // the header and not the image extension
  private static final String IMAGE_EXTENSION = ".jpg";
  // Maximum file size is limited to 7 MiB (7 * 1024 * 1024 Bytes)
  private static final long MAX_FILE_SIZE = 7340032;

  public static void setMemeFileLocation(Path path) {
    MEME_FILE_LOCATION = path.toString() + "/";
  }

  public static void saveImage(InputStream image, FormDataContentDisposition fileDetail, Meme meme)
      throws Exception {
    if (!isFileExtensionSupported(fileDetail)) {
      throw new FileNotSupportedException();
    }

    long numberBytesRead = 0;
    boolean isExceeded = false;

    int read = 0;
    byte[] bytes = new byte[1024];

    File savedImage = new File(MEME_FILE_LOCATION + meme.getMemeID() + IMAGE_EXTENSION);
    try (OutputStream outputStream = new FileOutputStream(savedImage)) {
      while ((read = image.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);

        numberBytesRead += read;
        if (numberBytesRead > MAX_FILE_SIZE) {
          isExceeded = true;
          break;
        }
      }
      outputStream.flush();
    } finally {
      if (isExceeded) {
        savedImage.delete();
        throw new FileExceedsLimitExceptions();
      }
    }
  }

  private static boolean isFileExtensionSupported(FormDataContentDisposition fileDetail) {
    String fileName = fileDetail.getFileName();
    String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
    return Arrays.stream(SupportedFiles.values())
        .anyMatch(file -> file.name().equals(fileExtension));
  }

  public static String getMemeImagePath(UUID memeID) {
    return MEME_FILE_LOCATION + memeID + IMAGE_EXTENSION;
  }
}
