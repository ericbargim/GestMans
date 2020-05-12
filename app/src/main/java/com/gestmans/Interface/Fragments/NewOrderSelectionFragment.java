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
public class NewOrderSelectionFragment extends Fragment {

    ListView lvDishes, lvTicket;

    public NewOrderSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fView = inflater.inflate(R.layout.fragment_new_order_selection, container, false);
        references(fView);
        return fView;
    }

    private void references(View fView) {
        lvDishes = fView.findViewById(R.id.lvDishes);
        lvTicket = fView.findViewById(R.id.lvTicket);
    }
}
