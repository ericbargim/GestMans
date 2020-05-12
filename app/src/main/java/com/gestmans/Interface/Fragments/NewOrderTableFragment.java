package com.gestmans.Interface.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gestmans.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrderTableFragment extends Fragment {

    ListView lvTables;

    public NewOrderTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fView = inflater.inflate(R.layout.fragment_new_order_table, container, false);
        references(fView);
        return fView;
    }

    private void references(View fView) {
        lvTables = fView.findViewById(R.id.lvTables);
    }
}
