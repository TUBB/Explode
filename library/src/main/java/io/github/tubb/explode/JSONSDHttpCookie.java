package io.github.tubb.explode;

import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;

import okhttp3.Cookie;

import static io.github.tubb.explode.CheckUtils.isEmpty;
import static io.github.tubb.explode.CheckUtils.isNull;

/**
 * Created by tubingbing on 18/4/10.
 */

final class JSONSDHttpCookie implements SDHttpCookie {
    @Override
    public String ser(Cookie cookie) {
        if (isNull(cookie)) return "";
        HttpCookie httpCookie = new HttpCookie();
        httpCookie.setName(cookie.name());
        httpCookie.setValue(cookie.value());
        httpCookie.setExpiresAt(cookie.expiresAt());
        httpCookie.setDomain(cookie.domain());
        httpCookie.setPath(cookie.path());
        httpCookie.setSecure(cookie.secure());
        httpCookie.setHttpOnly(cookie.httpOnly());
        httpCookie.setHostOnly(cookie.hostOnly());
        return JSON.toJSONString(httpCookie);
    }

    @Nullable
    @Override
    public Cookie des(String cookieStr) {
        if (isEmpty(cookieStr)) return null;
        HttpCookie httpCookie = JSON.parseObject(cookieStr, HttpCookie.class);
        Cookie.Builder builder = new Cookie.Builder();
        builder = builder.name(httpCookie.getName());
        builder = builder.value(httpCookie.getValue());
        builder = builder.expiresAt(httpCookie.getExpiresAt());
        builder = httpCookie.isHostOnly() ? builder.hostOnlyDomain(httpCookie.getDomain()) : builder.domain(httpCookie.getDomain());
        builder = builder.path(httpCookie.getPath());
        builder = httpCookie.isSecure() ? builder.secure() : builder;
        builder = httpCookie.isHttpOnly() ? builder.httpOnly() : builder;
        return builder.build();
    }
}
