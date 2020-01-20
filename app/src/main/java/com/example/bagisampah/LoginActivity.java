package com.example.bagisampah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText edit_email, edit_password;
    private Button btn_login, btn_google;
    private TextView txt_daftar;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference mDatabase;
    GoogleSignInClient mGoogleSignInClient;
    static final int GOOGLE_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        txt_daftar = findViewById(R.id.txt_daftar);
        btn_login = findViewById(R.id.btn_masuk);
        btn_google = findViewById(R.id.btn_google);
        FirebaseApp.initializeApp(LoginActivity.this);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

         GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                 .Builder()
                 .requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build();
         mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_email.getText().toString().matches("") || edit_password.getText().toString().matches("")){
                    Toast.makeText(LoginActivity.this, "Mohon isi Email/Password!", Toast.LENGTH_SHORT).show();
                } else{
                    auth.signInWithEmailAndPassword(edit_email.getText().toString(),edit_password.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        txt_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });


        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInGoogle();

            }
        });
    }

    private void addUserToDatabase(FirebaseUser user){
        if (user !=null){
            String nama=user.getDisplayName();
            String noHP=user.getPhoneNumber();
            for (UserInfo profile : user.getProviderData()) {
                if (nama == null && profile.getDisplayName()!=null){
                    nama = profile.getDisplayName();
                    noHP = profile.getPhoneNumber();
                }
            }
            Log.d("nama", nama);
            String email = user.getEmail();
            Log.d("nomor", noHP);
            HashMap<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("nama", nama);
            dataMap.put("email", email);
            dataMap.put("nomorHP", noHP);
            mDatabase.child("Users").child(auth.getCurrentUser().getUid().toString()).setValue(dataMap);
            Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

    }

    private void SignInGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account!=null){
                    firebaseAuthWithGoogle(account);
                }

            } catch (ApiException e){
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle:  "+account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        Log.d("TAG", "Signin Success");
                        FirebaseUser user = auth.getCurrentUser();
                        addUserToDatabase(user);
                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }

                });
    }
}
