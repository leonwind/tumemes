package exceptions;

import java.io.IOException;

public class FileExceedsLimitExceptions extends IOException {

  public FileExceedsLimitExceptions() {
    super("File exceeds 1 MiB limit.");
  }
}
