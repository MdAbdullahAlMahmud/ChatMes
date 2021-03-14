package com.mkrlabs.chatmes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.googlecode.mp4parser.authoring.Edit;

public class LoginActivity extends AppCompatActivity {

    TextView signUp;
    EditText emailEdt,passEdt;
    CardView loginButton;
    ProgressDialog dialog;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtils.systembartransparent(this);
        setContentView(R.layout.activity_login);
        init();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdt.getText().toString().trim();
                String pass = passEdt.getText().toString().trim();
                if (email.isEmpty()){
                    emailEdt.setError("required");
                    return;
                }
                if (pass.isEmpty()){
                    passEdt.setError("required");
                    return;
                }
                checkLoginCredential(email,pass);

            }
        });
    }

    private void checkLoginCredential(String email, String pass) {
        dialog.show();
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        signUp=findViewById(R.id.signUpTV);
        emailEdt=findViewById(R.id.loginEmailEdt);
        passEdt=findViewById(R.id.loginPassEdt);
        loginButton=findViewById(R.id.loginCardViewButton);
        mAuth =FirebaseAuth.getInstance();
        dialog= new ProgressDialog(this);
        dialog.setTitle("Login");
        dialog.setMessage("please wait ...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}