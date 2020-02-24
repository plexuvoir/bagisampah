package com.example.bagisampah;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText editEmail;
    private Button btnReset;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        editEmail = findViewById(R.id.edit_reset_email);
        btnReset = findViewById(R.id.btn_reset_password);
        auth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(view -> {
            String email = editEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Masukkan email terlebih dahulu.", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassword.this, R.style.DialogStyle);
                    builder.setTitle("Reset Password Berhasil")
                            .setMessage("Silakan cek email Anda untuk melakukan reset password.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(ResetPassword.this, LoginActivity.class));
                                }
                            }).setCancelable(false)
                            .show();
                } else {
                    Toast.makeText(ResetPassword.this, "Gagal mengirim email.", Toast.LENGTH_SHORT).show();
                }
            });


        });
    }
}
