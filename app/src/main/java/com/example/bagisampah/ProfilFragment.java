package com.example.bagisampah;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilFragment extends Fragment {
    Button btn_logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflate =inflater.inflate(R.layout.fragment_profil,null);
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        btn_logout = inflate.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });


        return inflate;
    }
}
