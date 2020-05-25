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
import com.gestmans.Business.IOnBackPressed;
import com.gestmans.Business.OrdersUtilitiesClass;
import com.gestmans.R;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditOrderSelectionFragment extends Fragment implements IOnBackPressed {

    private TextView tvTitle;
    private Spinner spDishType;
    private Button btnDismiss, btnCreateTicket;
    private ListView lvDishes, lvTicket;

    public EditOrderSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fView = inflater.inflate(R.layout.fragment_edit_order_selection, container, false);

        // Reference the elements from the XML layout
        references(fView);

        // Get the sent argument (table)
        Bundle bundle = getArguments();
        String table = null;
        try {
            if (bundle.getString("table") != null) {
                table = bundle.getString("table");
                Log.d(getString(R.string.EDIT_ORDER_SELECTION_FRAGMENT), table);
            }
        } catch (NullPointerException ex) {
            Log.d(getString(R.string.EDIT_ORDER_SELECTION_FRAGMENT), "Null table!!! Crash incoming!!!");
        }

        // Set one title or other depending if it's null
        if (table != null) {
            tvTitle.setText(getString(R.string.EDIT_ORDER_SELECTION_TITLE, table));
        } else {
            tvTitle.setText(getString(R.string.EDIT_ORDER_SELECTION_TITLE, getString(R.string.ORDER_TABLE_HOLDER)));
        }

        // Get the multiple dish types
        String data = "";
        try {
            data = new FetchDataPHP().execute("get_dish_type").get();
            Log.d(getString(R.string.EDIT_ORDER_SELECTION_FRAGMENT), data);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        // Transform String to List and capitalize first letter
        List<String> listDishTypes = OrdersUtilitiesClass.stringToListCapitalize(data);

        // Put the list on the spinner
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_text_selected, listDishTypes);
        adapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        spDishType.setAdapter(adapter);

        // When a spinner item is selected
        spDishType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the dishes of the selected dish type and add them to the list
                String data;
                try {
                    // Get the dishes of the selected dish type
                    String dishType = spDishType.getSelectedItem().toString().toLowerCase();
                    Log.d(getString(R.string.EDIT_ORDER_SELECTION_FRAGMENT), dishType);
                    data = new FetchDataPHP().execute("get_dishes", spDishType.getSelectedItem().toString().toLowerCase()).get();

                    // Add the dishes to the list
                    List<String> al = Arrays.asList(data.split("-"));
                    ArrayAdapter<String> array = new ArrayAdapter<>(getActivity(), R.layout.adapter_tables, al);
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
            dismissDialog();
        });
        return fView;
    }

    private void dismissDialog() {
        // Create dialog to confirm the dismiss
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        builder.setTitle(getString(R.string.EDIT_ORDER))
                .setMessage(getString(R.string.EDIT_ORDER_SELECTION_CONFIRM_DISMISS))
                .setNegativeButton(getString(R.string.NO), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.YES), (dialog, which) -> getFragmentManager().popBackStack())
                .create();
        final AlertDialog dialog = builder.show();

        // Change the buttons color
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purpleGradient, null));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purpleGradient, null));

        // Show the dialog
        dialog.show();
    }

    @Override
    public boolean onBackPressed() {
        dismissDialog();
        return true;
    }

    private void references(View fView) {
        tvTitle = fView.findViewById(R.id.tvTitleEditSelection);
        spDishType = fView.findViewById(R.id.spDishTypeEdit);
        btnDismiss = fView.findViewById(R.id.btnDismissSelectionEdit);
        lvDishes = fView.findViewById(R.id.lvDishesEdit);
        lvTicket = fView.findViewById(R.id.lvTicketEdit);
        btnCreateTicket = fView.findViewById(R.id.btnModifyTicket);
    }
}
