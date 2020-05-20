package com.gestmans.Interface.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gestmans.Business.FetchDataPHP;
import com.gestmans.R;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewOrderSelectionFragment extends Fragment {

    private TextView tvTitle;
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

        // Reference the elements from the XML layout
        references(fView);

        // Get the sent argument (table)
        Bundle bundle = getArguments();
        String table = null;
        try {
            if (bundle.getString("table") != null) {
                table = bundle.getString("table");
                Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), table);
            }
        } catch (NullPointerException ex) {
            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), "Null table!!! Crash incoming!!!");
        }

        // Set one title or other depending if it's null
        if (table != null) {
            tvTitle.setText(getString(R.string.NEW_ORDER_SELECTION_TITLE, table));
        } else {
            tvTitle.setText(getString(R.string.NEW_ORDER_SELECTION_TITLE, getString(R.string.ORDER_TABLE_HOLDER)));
        }

        // Get the multiple dish types
        String data = "";
        try {
            data = new FetchDataPHP().execute("get_dish_type").get();
            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), data);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        // Put the received data on the spinner
        String[] dishTypes = data.split("-");
        List<String> al = Arrays.asList(dishTypes);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_text_selected, al);
        adapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        spDishType.setAdapter(adapter);

        // When a spinner item is selected
        spDishType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the dishes of the selected dish type and add them to the list
                String data;
                try {
                    data = new FetchDataPHP().execute("get_dishes", spDishType.getSelectedItem().toString().toLowerCase()).get();
                    List<String> al = Arrays.asList(data.split("-"));
                    ArrayAdapter<String> array = new ArrayAdapter<>(getActivity(), R.layout.listview_tables, al);
                    lvDishes.setAdapter(array);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // When an item is selected in ListViewDishes
        lvDishes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = lvDishes.getItemAtPosition(position).toString();
            }
        });

        // When dismiss button is clicked
        btnDismiss.setOnClickListener(v -> {
            // Create dialog to confirm the dismiss
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.NEW_ORDER))
                    .setMessage(getString(R.string.NEW_ORDER_SELECTION_CONFIRM_DISMISS))
                    .setNegativeButton(getString(R.string.NO), (dialog, which) -> dialog.cancel())
                    .setPositiveButton(getString(R.string.YES), (dialog, which) -> getActivity().onBackPressed())
                    .create();
            final AlertDialog dialog = builder.show();

            // Change the buttons color
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purpleGradient, null));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purpleGradient, null));

            // Show the dialog
            dialog.show();
        });
        return fView;
    }

    private void references(View fView) {
        tvTitle = fView.findViewById(R.id.tvTitleNewSelection);
        spDishType = fView.findViewById(R.id.spDishTypeNew);
        btnDismiss = fView.findViewById(R.id.btnDismissSelectionNew);
        lvDishes = fView.findViewById(R.id.lvDishesNew);
        lvTicket = fView.findViewById(R.id.lvTicketNew);
        btnCreateTicket = fView.findViewById(R.id.btnCreateTicket);
    }
}
