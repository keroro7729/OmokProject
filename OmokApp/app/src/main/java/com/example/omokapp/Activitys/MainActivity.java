package com.example.omokapp.Activitys;

import android.os.Bundle;
import android.util.Log;

import com.example.omokapp.DTO.DeviceInfo;
import com.example.omokapp.DTO.LoginAttemptResponse;
import com.example.omokapp.DTO.LoginRequest;
import com.example.omokapp.DTO.LoginResponse;
import com.example.omokapp.Network.ApiManager;
import com.example.omokapp.R;
import com.example.omokapp.Secure.SecurePreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.omokapp.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //ApiManager apiManager = new ApiManager(getResources().getString(R.string.server_ip_port));
        test();
    }

    private void test(){
        // register, login test
        String register = "register", loginA = "login-attempt";
        String login = "login", username = "testuser123", password = "testME!!123";

        ApiManager apiManager = new ApiManager(getResources().getString(R.string.base_url));
        SecurePreferences database = SecurePreferences.getInstance(this);

        Call<String> call = apiManager.getService().register(username, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Log.d(register, response.body());
                }
                else{
                    Log.e(register, "error on request:"+call.request());
                    Log.e(register, "response code: "+response.code());
                    Log.e(register, "response message: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(register, "error on request:"+call.request());
                Log.e(register, "network error: "+t.getMessage());
            }
        });

        Call<LoginAttemptResponse> call2 = apiManager.getService().loginAttempt(username);
        call2.enqueue(new Callback<LoginAttemptResponse>() {
            @Override
            public void onResponse(Call<LoginAttemptResponse> call, Response<LoginAttemptResponse> response) {
                if(response.isSuccessful()){
                    LoginAttemptResponse body = response.body();
                    Log.d(loginA, "response: "+body.toString());
                    database.saveSecretKey(body.getKeyData());
                    database.saveString("my-user-id", String.valueOf(body.getUserId()));
                }
                else{
                    Log.e(loginA, "error on request:"+call.request());
                    Log.e(loginA, "response code: "+response.code());
                    Log.e(loginA, "response message: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginAttemptResponse> call, Throwable t) {
                Log.e(loginA, "error on request:"+call.request());
                Log.e(loginA, "network error: "+t.getMessage());
            }
        });

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        Long userId = Long.parseLong(database.getString("my-user-id"));
        if(userId == null) return;
        request.setUserId(userId);
        request.setDeviceInfo(DeviceInfo.create(this));
        Log.d("login request: ", request.toString());

        Call<LoginResponse> call3 = apiManager.getService().login(request);
        call3.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    Log.d(login, "response: "+response.body().toString());
                }
                else{
                    Log.e(login, "error on request:"+call.request());
                    Log.e(login, "response code: "+response.code());
                    Log.e(login, "response message: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(login, "error on request:"+call.request());
                Log.e(login, "network error: "+t.getMessage());
            }
        });
    }
}