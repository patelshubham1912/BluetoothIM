package com.example.shubham.bluetoothim;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Shubham on 8/6/2017.
 */

public class FragmentMessages extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Toast.makeText(this.getContext(),"Message",Toast.LENGTH_SHORT).show();
        View v = inflater.inflate(R.layout.fragment_messages, container, false);



        return v;
    }
}
