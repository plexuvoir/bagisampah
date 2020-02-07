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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilFragment extends Fragment {
    Button btn_logout;
    String namaUserString, nomorTeleponString, emailString;

    ImageView btnEditNama, btnEditNomor;
    TextView txtNama, txtNomorWhatsapp, txtMail;

    private FirebaseDatabase db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflate =inflater.inflate(R.layout.fragment_profil,null);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        txtMail = inflate.findViewById(R.id.txt_mail);
        txtNama = inflate.findViewById(R.id.txt_nama_user);
        txtNomorWhatsapp = inflate.findViewById(R.id.txt_nomor_whatsapp);
        btnEditNama = inflate.findViewById(R.id.btn_edit_nama);
        btnEditNomor = inflate.findViewById(R.id.btn_edit_nomor);

        db = FirebaseDatabase.getInstance();
        db.getReference("Users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String emailUser = dataSnapshot.child("email").getValue(String.class);
                emailString= emailUser;
                String namaUser = dataSnapshot.child("nama").getValue(String.class);
                namaUserString=namaUser;
                System.out.println(namaUserString);
                String nomorTelepon = dataSnapshot.child("nomorHP").getValue(String.class);
                nomorTeleponString= nomorTelepon;
                System.out.println(nomorTeleponString);
                txtMail.setText(emailString);
                txtNama.setText(namaUserString);
                txtNomorWhatsapp.setText(nomorTeleponString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn_logout = inflate.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnEditNama.setOnClickListener(view -> {
            BottomSheetDialogNama bottomSheetDialogNama = new BottomSheetDialogNama();
            bottomSheetDialogNama.show(getActivity().getSupportFragmentManager(), "bottomSheet");
        });

        btnEditNomor.setOnClickListener(view -> {
            BottomSheetDialogNomor bottomSheetDialogNomor = new BottomSheetDialogNomor();
            bottomSheetDialogNomor.show(getActivity().getSupportFragmentManager(), "bottomSheet");
        });


        return inflate;
    }

}
