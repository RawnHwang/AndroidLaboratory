package me.hwang.materialdesigntest.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import me.hwang.materialdesigntest.CollapseActivity;
import me.hwang.materialdesigntest.R;
import me.hwang.materialdesigntest.util.CoordinatorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstContentFragment extends Fragment {


    private Button btnTestCollapse;
    private Button btnTestCoordinator;

    public FirstContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_first_content, container, false);

        btnTestCollapse = view.findViewById(R.id.btn_test_collapse);
        btnTestCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CollapseActivity.class);
                getContext().startActivity(intent);
            }
        });

        btnTestCoordinator = view.findViewById(R.id.btn_test_coordinator);
        btnTestCoordinator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CoordinatorActivity.class);
                getContext().startActivity(intent);
            }
        });

        return view;
    }

}
