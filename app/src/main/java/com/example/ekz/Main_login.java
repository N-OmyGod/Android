package com.example.ekz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ekz.main.main_menu;
import com.example.ekz.models.LoginModel;
import com.example.ekz.models.LoginResponse;
import com.example.ekz.network.NetworkServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Main_login extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        setOnClick();
    }

    private void setOnClick() {
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);
        email = findViewById(R.id.editTextTextEmailAddress);
        pass = findViewById(R.id.editTextTextPassword);
        if(getLogin().length() > 1){ email.setText(getLogin());}
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getLogin().length() > 1){ email.setText(getLogin());}
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(getLogin().length() > 1){ email.setText(getLogin());}
    }


    private void signIn() {
        String mail = email.getText().toString();
        String password = pass.getText().toString();

        if (mail.contains("@") && mail.length() > 8 && password.length() > 0) {
            signRequest(new LoginModel(mail, password));
        } else
            Toast.makeText(getApplicationContext(), "Проверьте правильность введённых данных", Toast.LENGTH_SHORT).show();
    }

    private void signRequest(LoginModel loginModel) {
        NetworkServices.getInstance().getJSONApi().login(loginModel).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() / 100 == 2) {
                    Intent intent = new Intent(getApplicationContext(), main_menu.class);
                    intent.putExtra("userInfo", response.body());
                    saveLoginInfo(loginModel.getEmail(), loginModel.getPassword());
                    startActivity(intent);
                } else
                    Toast.makeText(Main_login.this, "Проверьте логин и пароль", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Main_login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button: {

                signIn();
            }
            break;
            case R.id.button2: {

            }
            break;
        }
    }

    private void saveLoginInfo(String mail, String pass) {
        SharedPreferences prefs = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString("login", mail);
        prefEdit.putString("pass", pass);
        prefEdit.apply();
    }

    private String getLogin(){
        return getSharedPreferences("loginInfo",MODE_PRIVATE).getString("login", "");
    }

}