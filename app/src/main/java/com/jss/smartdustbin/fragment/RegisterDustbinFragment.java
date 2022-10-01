/*
package com.jss.smartdustbin.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import info.androidhive.barcode.BarcodeReader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jss.smartdustbin.Activities.HomeActivity;
import com.jss.smartdustbin.Activities.UserHomeActivity;
import com.jss.smartdustbin.Models.DustbinRegistrationData;
import com.jss.smartdustbin.R;

import java.io.Serializable;

public class RegisterDustbinFragment extends Fragment {

    EditText editTextDustbinRegistrationId;
    Button btNext1;
    Button btPrevious1;
    DustbinRegistrationData dustbinRegistrationData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register_dustbin, container, false);
        if(getActivity().equals(UserHomeActivity.class)){
            ((UserHomeActivity) getActivity()).setActionBarTitle("Register Dustbin");
        } else if(getActivity().equals(HomeActivity.class)){
            ((HomeActivity) getActivity()).setActionBarTitle("Register Dustbin");
        }

        editTextDustbinRegistrationId = (EditText) view.findViewById(R.id.et_dustbin_registration_id);
        btNext1 = (Button) view.findViewById(R.id.bt_next1);
        btPrevious1 = (Button) view.findViewById(R.id.bt_previous1);


        dustbinRegistrationData = new DustbinRegistrationData();

        btNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RegisterDustbinFragment2();
                dustbinRegistrationData.setId(editTextDustbinRegistrationId.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("registrationDataObject", (Serializable) dustbinRegistrationData);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fl_home_activity, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        btPrevious1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Register Dustbin");

    }






}
*/
