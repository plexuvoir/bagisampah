package com.example.bagisampah.View;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.bagisampah.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class BottomSheetDialogNama extends BottomSheetDialogFragment {
//    private BottomSheetListener mListener;
    private FirebaseDatabase db;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    EditText editNama;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout_nama, container, false);
        db = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        TextView txtSimpan = v.findViewById(R.id.txt_simpan);
        TextView txtBatal = v.findViewById(R.id.txt_batal);
        editNama = v.findViewById(R.id.edit_nama);


        txtSimpan.setOnClickListener(view -> {
//            mListener.onButtonClicked("button 1 clicked");
            if (editNama.getText().toString().equalsIgnoreCase("")){
                editNama.setError("Tidak boleh kosong");
            } else if (editNama.getText().toString().matches("^\\s*$")){
                editNama.setError("Tidak boleh kosong");
            } else {
                if (editNama.getText()!=null){
                    db.getReference("Users").child(auth.getCurrentUser().getUid()).child("nama").setValue(editNama.getText().toString());

                }
                dismiss();
            }



        });

        txtBatal.setOnClickListener(view -> {
            dismiss();
        });

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
                FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return v;
    }

//    public interface BottomSheetListener {
//        void onButtonClicked (String text);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            mListener = (BottomSheetListener) context;
//        } catch (ClassCastException e){
//            throw new ClassCastException(context.toString()+" must implement BottomSheetListener");
//        }
//
//    }
}
