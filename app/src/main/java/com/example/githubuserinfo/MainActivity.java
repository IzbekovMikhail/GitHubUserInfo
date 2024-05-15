package com.example.githubuserinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText mLoginEdit;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginEdit = (EditText)findViewById(R.id.loginEdit);
        mTextView = (TextView)findViewById(R.id.textView);
    }

    public void onClick(View view) {
        String login = mLoginEdit.getText().toString();

        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
        final Call<User> call =
                gitHubService.getUser(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();

                    mTextView.setText("Аккаунт GitHub: " + user.getName() +
                            "\nСайт: " + user.getBlog() +
                            "\nКомпания: " + user.getCompany());
                } else {
                    int statusCode = response.code();

                    ResponseBody errorBody = response.errorBody();
                    try {
                        mTextView.setText(errorBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mTextView.setText("Что-то пошло не так: " + t.getMessage());
            }
        });
    }
}