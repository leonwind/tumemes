package exceptions;

import enums.SupportedFiles;

import java.util.Arrays;

public class FileNotSupportedException extends Exception {

  public FileNotSupportedException() {
    super(String.format("Only %s files are supported for now.",
        Arrays.toString(SupportedFiles.values())));
  }
}
