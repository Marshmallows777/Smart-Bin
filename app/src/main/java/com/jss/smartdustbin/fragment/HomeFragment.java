/*
package com.jss.smartdustbin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jss.smartdustbin.Activities.HomeActivity;
import com.jss.smartdustbin.Activities.LoginActivity;
import com.jss.smartdustbin.R;


public class HomeFragment extends Fragment {

    public static final String LOG_TAG = LoginActivity.class.getSimpleName();
    LinearLayout loginLinearLayout;
    LinearLayout registerDustbinLinearLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((HomeActivity) getActivity()).setActionBarTitle("Home");

        loginLinearLayout = view.findViewById(R.id.ll_login);
        registerDustbinLinearLayout = view.findViewById(R.id.ll_register_dustbin);

        loginLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivityIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginActivityIntent);

            }
        });

        registerDustbinLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment registerDustbinFragment = new RegisterDustbinFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_home_activity, registerDustbinFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }


        });
        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}

*/
