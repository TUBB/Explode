package io.github.tubb.explode;

/**
 * Convert Http response body to Entity
 * Created by tubingbing on 18/4/8.
 */

public interface ResponseConverter<R> {
    R convert(String response);
}
