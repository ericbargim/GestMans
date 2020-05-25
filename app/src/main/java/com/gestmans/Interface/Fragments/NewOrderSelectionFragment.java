package com.gestmans.Interface.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gestmans.Business.FetchDataPHP;
import com.gestmans.Business.HelperClass;
import com.gestmans.Business.IOnBackPressed;
import com.gestmans.Business.ListViewDishesAdapter;
import com.gestmans.Business.ListViewTicketAdapter;
import com.gestmans.Business.Objects.Dish;
import com.gestmans.Business.OrdersUtilitiesClass;
import com.gestmans.Interface.Dialogs.LoadingDialog;
import com.gestmans.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class NewOrderSelectionFragment extends Fragment implements IOnBackPressed {

    private TextView tvTitle;
    private Spinner spDishType;
    private Button btnDismiss, btnCreateTicket;
    private ListView lvDishes, lvTicket;

    private ListViewTicketAdapter adapterTicket;

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

        adapterTicket = new ListViewTicketAdapter(getActivity(), new ArrayList<>());

        // Create loading dialog
        LoadingDialog ld = new LoadingDialog(getActivity());

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

        // Transform String to List and capitalize first letter
        List<String> listDishTypes = OrdersUtilitiesClass.stringToListCapitalize(data);

        // Put the list on the spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getActivity(), R.layout.spinner_text_selected, listDishTypes);
        adapterSpinner.setDropDownViewResource(R.layout.spinner_text_dropdown);
        spDishType.setAdapter(adapterSpinner);

        // When a spinner item is selected
        spDishType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Start loading dialog
                ld.startLoadingDialog();

                new Handler().post(() -> {
                    // Get the dishes of the selected dish type and add them to the list
                    String data;
                    String dishType = spDishType.getSelectedItem().toString().toLowerCase();
                    if (!dishType.equals("menu")) {
                        try {
                            // Get the dishes of the selected dish type
                            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), dishType);
                            data = new FetchDataPHP().execute("get_dish_names", dishType, "name_id").get();
                            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), data);

                            // Add the dishes to the list
                            String[] dishesComplete = data.split("-");
                            ArrayList<Dish> allDishes = new ArrayList<>();
                            for (int i = 0; i < dishesComplete.length; i++) {
                                String dish = dishesComplete[i];
                                String[] allDish = dish.split(",");
                                String dishName = allDish[0];
                                String dishId = allDish[1];
                                Dish dishObject = new Dish(dishId, dishName, 1);
                                allDishes.add(dishObject);
                            }
                            for (Dish dish: allDishes) {
                                Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), dish.toString());
                            }
                            ListViewDishesAdapter adapterDishes = new ListViewDishesAdapter(getActivity(), allDishes);
                            lvDishes.setAdapter(adapterDishes);

                            // Close loading dialog
                            ld.dismissDialog();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();

                            // Close loading dialog
                            ld.dismissDialog();
                        } catch (ArrayIndexOutOfBoundsException e) {
                            ArrayList<String> al = new ArrayList<>();
                            al.add("Error");
                            ArrayAdapter<String> adapterDishes = new ArrayAdapter<>(getActivity(), R.layout.adapter_tables, al);
                            lvDishes.setAdapter(adapterDishes);

                            // Close loading dialog
                            ld.dismissDialog();
                        }
                    } else {
                        try {
                            // Get the dishes of the selected dish type
                            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), dishType);
                            data = new FetchDataPHP().execute("get_menus", dishType).get();

                            // Add the dishes to the list
                            List<String> listDishes = Arrays.asList(data.split("-"));
                            listDishes = HelperClass.capitalizeLetters(listDishes);
                            ArrayAdapter<String> array = new ArrayAdapter<>(getActivity(), R.layout.adapter_tables, listDishes);
                            lvDishes.setAdapter(array);

                            // Close loading dialog
                            ld.dismissDialog();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();

                            // Close loading dialog
                            ld.dismissDialog();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // When an item is selected in ListViewDishes
        lvDishes.setOnItemClickListener((parent, view, position, id) -> {
            if (spDishType.getSelectedItem().toString().toLowerCase().equals("menu")) {
                String menu = lvDishes.getItemAtPosition(position).toString();
                Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), menu);

            } else {
                boolean isInArray = false;
                Dish dish = (Dish) parent.getAdapter().getItem(position);
                Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), dish.toString());
                ListAdapter adapter = lvTicket.getAdapter();
                try {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        Dish adapterDish = (Dish) adapter.getItem(i);
                        if (dish.getId().equals(adapterDish.getId())) {
                            isInArray = true;
                        }
                    }
                } catch (NullPointerException e) {
                    Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), "Exception caught. No items in array.");
                    e.printStackTrace();
                }
                if (!isInArray) {
                    Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), "Dish not in Array. Adding it.");
                    dish.setQuantity(1);
                    Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), "Resetting quantity.");
                    adapterTicket.add(dish);
                    adapterTicket.notifyDataSetChanged();
                    lvTicket.setAdapter(adapterTicket);
                }
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
        builder.setTitle(getString(R.string.NEW_ORDER))
                .setMessage(getString(R.string.NEW_ORDER_SELECTION_CONFIRM_DISMISS))
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
        tvTitle = fView.findViewById(R.id.tvTitleNewSelection);
        spDishType = fView.findViewById(R.id.spDishTypeNew);
        btnDismiss = fView.findViewById(R.id.btnDismissSelectionNew);
        lvDishes = fView.findViewById(R.id.lvDishesNew);
        lvTicket = fView.findViewById(R.id.lvTicketNew);
        btnCreateTicket = fView.findViewById(R.id.btnCreateTicket);
    }
}
