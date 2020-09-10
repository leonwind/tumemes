package enums;

public enum AllowedEmailDomains {
  tum ("tum.de"),
  mytum ("mytum.de");

  String domain;

  AllowedEmailDomains(String domain) {
    this.domain = domain;
  }

  @Override
  public String toString() {
    return domain;
  }
}
