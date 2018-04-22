package io.github.tubb.explode.sample;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by tubingbing on 18/4/2.
 */

public interface UserService {
    @GET("/users")
    Observable<HttpResult<ArrayList<User>>> users();
    @GET("/users")
    Call<HttpResult<ArrayList<User>>> listUsers();
}
