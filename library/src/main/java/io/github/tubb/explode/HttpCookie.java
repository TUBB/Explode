package io.github.tubb.explode;

/**
 * Created by tubingbing on 18/4/10.
 */

final class HttpCookie {
    /**
     String name = (String) in.readObject();
     String value = (String) in.readObject();
     long expiresAt = in.readLong();
     String domain = (String) in.readObject();
     String path = (String) in.readObject();
     boolean secure = in.readBoolean();
     boolean httpOnly = in.readBoolean();
     boolean hostOnly = in.readBoolean();
     */
    private String name;
    private String value;
    private long expiresAt;
    private String domain;
    private String path;
    private boolean secure;
    private boolean httpOnly;
    private boolean hostOnly;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public boolean isHostOnly() {
        return hostOnly;
    }

    public void setHostOnly(boolean hostOnly) {
        this.hostOnly = hostOnly;
    }
}
