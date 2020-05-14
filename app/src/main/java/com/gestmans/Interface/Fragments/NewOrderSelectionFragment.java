package com.gestmans.Interface.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.gestmans.Business.fetchDataPHP;
import com.gestmans.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewOrderSelectionFragment extends Fragment {

    private Spinner spDishType;
    private Button btnDismiss, btnCreateTicket;
    private ListView lvDishes, lvTicket;

    public NewOrderSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fView = inflater.inflate(R.layout.fragment_new_order_selection, container, false);
        references(fView);
        String data = "";
        try {
            data = new fetchDataPHP().execute("get_dish_type").get();
            System.out.println(data);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        String[] dishTypes = data.split("-");
        List<String> al = new ArrayList<>();
        al = Arrays.asList(dishTypes);
        ArrayAdapter<String> adapterInit;
        adapterInit = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, al);
        adapterInit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDishType.setAdapter(adapterInit);
        spDishType.setPrompt(getString(R.string.ORDER_SELECTION_SPINNER_HINT));
        spDishType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.NEW_ORDER))
                        .setMessage(getString(R.string.NEW_ORDER_SELECTION_CONFIRM_DISMISS))
                        .setNegativeButton(getString(R.string.NO), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton(getString(R.string.YES), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().onBackPressed();
                            }
                        })
                        .create();

                final AlertDialog dialog = builder.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purpleGradient, null));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purpleGradient, null));
                dialog.show();
            }
        });
        return fView;
    }

    private void references(View fView) {
        spDishType = fView.findViewById(R.id.spDishTypeNew);
        btnDismiss = fView.findViewById(R.id.btnDismissSelectionNew);
        lvDishes = fView.findViewById(R.id.lvDishesNew);
        lvTicket = fView.findViewById(R.id.lvTicketNew);
        btnCreateTicket = fView.findViewById(R.id.btnCreateTicket);
    }
}
