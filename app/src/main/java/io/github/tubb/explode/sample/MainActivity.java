package io.github.tubb.explode.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import io.github.tubb.explode.CookieEncrypt;
import io.github.tubb.explode.Explode;
import io.github.tubb.explode.ExplodeRetrofit;
import io.github.tubb.explode.ResponseConverter;

import java.util.ArrayList;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Explode";
    private TextView tvMsg;
    private long fetchUsersTaskId;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMsg = findViewById(R.id.tv_msg);
        // 这里是测试地址，api项目可以查看https://github.com/TUBB/explode_api
        ExplodeRetrofit retrofit = Explode.instance().get("http://10.0.2.177:3000");
        userService = retrofit.create(UserService.class);
        executeOkHttpRequest();
//        executeCall();
        executeRxJava();
    }

    private void executeRxJava() {
        fetchUsersTaskId = Explode.instance().execute(userService.users(), new ExplodeHttpCallback<ArrayList<User>>() {
            @Override
            public void onSuccess(ArrayList<User> data) {
                String usersStr = data.toString();
                tvMsg.setText(usersStr);
                showErrorToast(usersStr);
            }

            @Override
            public void onError(Throwable throwable) {
                String error = throwable.getMessage();
                tvMsg.setText(error);
                showErrorToast(error);
            }
        });
    }

    private void executeCall() {
        fetchUsersTaskId = Explode.instance().execute(userService.listUsers(), new ExplodeHttpCallback<ArrayList<User>>() {
            @Override
            public void onSuccess(ArrayList<User> data) {
                String usersStr = data.toString();
                tvMsg.setText(usersStr);
                showErrorToast(usersStr);
            }

            @Override
            public void onError(Throwable throwable) {
                String error = throwable.getMessage();
                tvMsg.setText(error);
                showErrorToast(error);
            }
        });
    }

    private void executeOkHttpRequest() {
        Request request = userService.listUsers().request();
        fetchUsersTaskId = Explode.instance().execute(request, new ResponseConverter<HttpResult<ArrayList<User>>>() {
            @Override
            public HttpResult<ArrayList<User>> convert(String response) {
                return JSON.parseObject(response, new TypeReference<HttpResult<ArrayList<User>>>(){});
            }
        }, new ExplodeHttpCallback<ArrayList<User>>() {
            @Override
            public void onSuccess(ArrayList<User> data) {
                String usersStr = data.toString();
                tvMsg.setText(usersStr);
                showErrorToast(usersStr);
            }

            @Override
            public void onError(Throwable throwable) {
                String error = throwable.getMessage();
                tvMsg.setText(error);
                showErrorToast(error);
            }
        });
    }

    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Explode.instance().cancel(fetchUsersTaskId);
    }
}
