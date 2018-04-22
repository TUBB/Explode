package io.github.tubb.explode;

import android.support.annotation.Nullable;

import okhttp3.Cookie;

/**
 * Created by tubingbing on 18/4/10.
 */

interface SDHttpCookie {
    @Nullable String ser(@Nullable Cookie cookie);
    @Nullable Cookie des(@Nullable String cookieStr);
}
