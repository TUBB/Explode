package io.github.tubb.explode;

/**
 * Identify ExplodeRetrofit
 * Created by tubingbing on 18/3/30.
 */

final class ExplodeRetrofitKey {
    private String generatorId;
    private String baseUrl;

    ExplodeRetrofitKey(String generatorId, String baseUrl) {
        this.generatorId = generatorId;
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExplodeRetrofitKey that = (ExplodeRetrofitKey) o;

        if (!generatorId.equals(that.generatorId)) return false;
        return baseUrl.equals(that.baseUrl);
    }

    @Override
    public int hashCode() {
        int result = generatorId.hashCode();
        result = 31 * result + baseUrl.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s@%s", baseUrl, generatorId);
    }
}
