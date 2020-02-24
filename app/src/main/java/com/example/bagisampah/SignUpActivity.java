package com.example.bagisampah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    Button daftar;
    EditText edit_nama, edit_email, edit_password, edit_confirm_password, edit_no_hp;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //getSupportActionBar().hide();
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        daftar = findViewById(R.id.btn_daftar);
        edit_nama = findViewById(R.id.edit_nama);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        edit_confirm_password = findViewById(R.id.edit_retype_password);
        edit_no_hp = findViewById(R.id.edit_nomor);
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_password.getText().toString().equals(edit_confirm_password.getText().toString())){
                    String emailString = edit_email.getText().toString().trim();
                    String passwordString = edit_password.getText().toString().trim();
                    auth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String namaString = edit_nama.getText().toString().trim();
                            String emailString = edit_email.getText().toString().trim();
                            String nomorHPString = edit_no_hp.getText().toString().trim();
                            HashMap<String, Object> dataMap = new HashMap<String, Object>();
                            dataMap.put("nama", namaString);
                            dataMap.put("email", emailString);
                            dataMap.put("nomorHP", nomorHPString);
                            mDatabase.child("Users").child(auth.getCurrentUser().getUid().toString()).setValue(dataMap);
                            Toast.makeText(SignUpActivity.this, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show();
                            if (firebaseUser!=null){
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else {

                }

            }
        });
    }
}
