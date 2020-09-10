package exceptions;

import enums.AllowedEmailDomains;

import java.util.Arrays;

public class DomainNotSupportedException extends Exception {

  public DomainNotSupportedException() {
    super(String.format("Only %s email domains are allowed.",
        Arrays.toString(AllowedEmailDomains.values())));
  }
}
