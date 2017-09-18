package me.hwang.materialdesigntest.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.hwang.materialdesigntest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondContentFragment extends Fragment {


    public SecondContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second_content, container, false);
    }

}
