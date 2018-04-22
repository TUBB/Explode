package io.github.tubb.explode;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * OkHttp cookie strategy
 * Created by tubingbing on 18/3/30.
 */

public interface CookieStrategy {
    void save(HttpUrl httpUrl, List<Cookie> list);
    List<Cookie> load(HttpUrl httpUrl);
}
