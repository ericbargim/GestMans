package com.gestmans.Interface.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gestmans.Business.Adapters.ListViewOrderMenusAdapter;
import com.gestmans.Business.Exceptions.ErorRetrievingInfoException;
import com.gestmans.Business.Objects.Menu;
import com.gestmans.Business.Utilities.DataClass;
import com.gestmans.Business.Utilities.FetchDataPHP;
import com.gestmans.Business.Utilities.HelperClass;
import com.gestmans.Business.Interfaces.IOnBackPressed;
import com.gestmans.Business.Adapters.ListViewDishesAdapter;
import com.gestmans.Business.Adapters.ListViewOrderDishesAdapter;
import com.gestmans.Business.Objects.Dish;
import com.gestmans.Interface.Dialogs.OrderMenuDialog;
import com.gestmans.Interface.Dialogs.OrderNotesDialog;
import com.gestmans.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewOrderSelectionFragment extends Fragment implements IOnBackPressed, OrderMenuDialog.OnMenuCreatedListener {

    private TextView tvTitle;
    private Spinner spDishType;
    private Button btnDismiss, btnOrderNotes, btnCreateOrder;
    private ListView lvDishes, lvOrderDishes, lvOrderMenus;
    private RadioGroup rgDishMenu;
    private RadioButton rbDishes, rbMenus;

    private ListViewOrderDishesAdapter adapterOrderDishes;
    private ListViewOrderMenusAdapter adapterOrderMenus;

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

        // Create the ListViewOrderDishAdapter and ListViewOrderMenuAdapter objects
        adapterOrderDishes = new ListViewOrderDishesAdapter(getActivity(), new ArrayList<>());
        adapterOrderMenus = new ListViewOrderMenusAdapter(getActivity(), new ArrayList<>());

        Bundle bundle;
        final String[] table = {null};
        try {
            // Get the sent argument (table)
            bundle = getArguments();
            if (bundle.getString("table") != null) {
                table[0] = bundle.getString("table");
                Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Table", table[0]);
            }

            // Set table number on title or go back
            if (table[0] != null) {
                tvTitle.setText(getString(R.string.NEW_ORDER_SELECTION_TITLE, table[0]));
            } else {
                throw new ErorRetrievingInfoException("Error retrieving the table");
            }

            // Get the multiple dish types
            final String[] data = {""};
            try {
                data[0] = new FetchDataPHP().execute("get_dish_type").get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            // Transform String to List and capitalize first letter
            List<String> listDishTypes = HelperClass.stringToListCapitalizeLetters(data[0]);

            // Put the list on the spinner
            ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getActivity(), R.layout.spinner_text_selected, listDishTypes);
            adapterSpinner.setDropDownViewResource(R.layout.spinner_text_dropdown);
            spDishType.setAdapter(adapterSpinner);

            // Check if selected table has a booking by an hour and a half
            try {
                data[0] = new FetchDataPHP().execute("booking_alert", table[0].split(" ")[1]).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            // If an error occurs getting the PHP data
            if (data[0].equals("error")) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }

            // If PHP returns 1
            else if (!data[0].equals("0")) {
                HelperClass.createDialogMessageNeutral(getString(R.string.WARNING), getString(R.string.ORDER_MESSAGE_BOOKING, data[0]), getString(R.string.OK), getContext());
            }

            // When a spinner item is selected
            spDishType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Get the dishes of the selected dish type and add them to the list
                    String data;
                    String dishTypeSelected = spDishType.getSelectedItem().toString().toLowerCase();
                    if (!dishTypeSelected.equals("menu")) {
                        try {
                            // Get the dishes of the selected dish type
                            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Dish type selected", dishTypeSelected);
                            data = new FetchDataPHP().execute("get_dish_names", "no_menu", dishTypeSelected).get();

                            if (!(data.equals("empty") || data.equals("error"))) {
                                // Add the dishes to the list
                                String[] dishesComplete = data.split("-");
                                ArrayList<Dish> allDishes = new ArrayList<>();
                                for (int i = 0; i < dishesComplete.length; i++) {
                                    String dish = dishesComplete[i];
                                    String[] allDish = dish.split(",");
                                    String dishName = allDish[0];
                                    String dishId = allDish[1];
                                    String dishType = allDish[2];
                                    Dish dishObject = new Dish(dishId, dishName, dishType, 1);
                                    Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Check dish creation", dishObject.toString());
                                    allDishes.add(dishObject);
                                }

                                // Check dishes successfully added to List
                                for (Dish dish : allDishes) {
                                    Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Check dishes list", dish.toString());
                                }

                                // Add the List to the ListView with an ArrayAdapter
                                ListViewDishesAdapter adapterDishes = new ListViewDishesAdapter(getActivity(), allDishes);
                                lvDishes.setAdapter(adapterDishes);
                            } else if (data.equals("empty")) {
                                // If it's empty, only add one item "No dishes" to ListView
                                ArrayList<String> al = new ArrayList<>();
                                al.add("No dishes on this dish type.");
                                ArrayAdapter<String> adapterDishes = new ArrayAdapter<>(getActivity(), R.layout.adapter_tables, al);
                                lvDishes.setAdapter(adapterDishes);
                            }
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();

                            // If an error occurs, only add one item "error" to ListView
                            ArrayList<String> al = new ArrayList<>();
                            al.add("Error");
                            ArrayAdapter<String> adapterDishes = new ArrayAdapter<>(getActivity(), R.layout.adapter_tables, al);
                            lvDishes.setAdapter(adapterDishes);
                        }
                    } else {
                        try {
                            // Get the dishes of the selected dish type
                            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Dish type selected", dishTypeSelected);
                            data = new FetchDataPHP().execute("get_menus", dishTypeSelected).get();

                            // Add the dishes to the list
                            List<String> listDishes = Arrays.asList(data.split("-"));
                            listDishes = HelperClass.capitalizeLetters(listDishes);
                            ArrayAdapter<String> array = new ArrayAdapter<>(getActivity(), R.layout.adapter_tables, listDishes);
                            lvDishes.setAdapter(array);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // When an item is selected in ListViewDishes
            lvDishes.setOnItemClickListener((parent, view, position, id) -> {
                // First check if the spinner selected item is menu
                if (spDishType.getSelectedItem().toString().toLowerCase().equals("menu")) {
                    // Get the name of the selected menu
                    String menu = lvDishes.getItemAtPosition(position).toString();
                    Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Menu selected", menu);

                    // Create the OrderMenuDialogFragment
                    OrderMenuDialog dialog = new OrderMenuDialog();

                    // Create a bundle to send the menu number
                    Bundle bundleMenu = new Bundle();
                    bundleMenu.putString("menu", menu);

                    // Add the bundle to the arguments
                    dialog.setArguments(bundleMenu);

                    // Set this fragment as target to return the menu
                    dialog.setTargetFragment(this, 0);

                    // Show the DialogFragment
                    if (getFragmentManager() != null) {
                        dialog.show(getFragmentManager(), "dialog");
                    }
                }

                // If the dishType has no dishes, don't do anything
                else if (lvDishes.getItemAtPosition(position).toString().equals("No dishes on this dish type.")) {
                    Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Menu selected", "Selected when there are no dishes");
                }

                // If an error occurred while retrieving dishes
                else if (lvDishes.getItemAtPosition(position).toString().equals("Error")) {
                    Toast.makeText(getActivity(), "Error formatting the dishes", Toast.LENGTH_SHORT).show();
                }

                // If not, check if is already in the array (lvOrder)
                else {
                    boolean isInArray = false;
                    Dish dish = (Dish) parent.getAdapter().getItem(position);
                    Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Dish selected", dish.toString());
                    try {
                        for (int i = 0; i < adapterOrderDishes.getCount(); i++) {
                            Dish adapterDish = (Dish) adapterOrderDishes.getItem(i);
                            if (dish.getId().equals(adapterDish.getId())) {
                                isInArray = true;

                                // Add one to the clicked dish
                                adapterDish.addToQuantity();
                            }
                        }
                    } catch (NullPointerException e) {
                        Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Dish in array", "Exception caught. No items in array.");
                        e.printStackTrace();
                    }

                    // If is not in the array, add it
                    if (!isInArray) {
                        // Reset quantity of the dish and add it to the ArrayAdapter
                        Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Dish in array", "Dish not in Array. Resetting quantity");
                        dish.setQuantity(1);
                        adapterOrderDishes.add(dish);
                    }

                    // Notify the ArrayAdapter of the changes made and update the ListViewOrderDishes
                    adapterOrderDishes.notifyDataSetChanged();
                    lvOrderDishes.setAdapter(adapterOrderDishes);
                }
            });

            rgDishMenu.setOnCheckedChangeListener((group, checkedId) -> {
                switch (checkedId) {
                    case R.id.rbDishesNew:
                        Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "RadioGroup clicked", "Visibility: Dishes.");
                        lvOrderDishes.setVisibility(View.VISIBLE);
                        lvOrderMenus.setVisibility(View.GONE);
                        break;
                    case R.id.rbMenusNew:
                        Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "RadioGroup clicked", "Visibility: Menus.");
                        lvOrderMenus.setVisibility(View.VISIBLE);
                        lvOrderDishes.setVisibility(View.GONE);
                        break;
                }
            });

            // Check the RadioButtonDishes as we created the OnCheckedChanged
            rbDishes.setChecked(true);

            // If order notes button is clicked
            btnOrderNotes.setOnClickListener(v -> {
                // Show the OrderNotesDialogFragment
                OrderNotesDialog dialog = new OrderNotesDialog();
                if (getFragmentManager() != null) {
                    dialog.show(getFragmentManager(), "dialog");
                }
            });

            // If create order button is clicked
            btnCreateOrder.setOnClickListener(v -> {
                // First, check if there are items on the order
                if (!(adapterOrderDishes.isEmpty() && adapterOrderMenus.isEmpty())) {
                    String returningJson;
                    String numTable, employee, orderNotes;

                    try {
                        // Create all the needed JSON Objects and JSON Arrays
                        JSONObject jsonObjectAll = new JSONObject();
                        JSONObject jsonItems = new JSONObject();

                        // Add the table number
                        numTable = table[0].split(" ")[1];
                        jsonItems.put("table", numTable);
                        Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Table number", jsonItems.toString());

                        // Add the employee who made the order
                        employee = DataClass.username;
                        jsonItems.put("employee", employee);
                        Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Employee", jsonItems.toString());

                        // Add the order notes (if it has)
                        try {
                            if (!DataClass.orderNotes.equals("")) {
                                orderNotes = DataClass.orderNotes;
                            } else {
                                orderNotes = "";
                            }
                        } catch (NullPointerException e) {
                            orderNotes = "null";
                        }
                        jsonItems.put("notes", orderNotes);
                        Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Notes", jsonItems.toString());

                        // If ListViewOrderDishes has items
                        if (!adapterOrderDishes.isEmpty()) {
                            // Loop the array searching for possible dishes
                            for (int i = 0; i < HelperClass.orderDishTypes.length; i++) {
                                // Check if there are dishes of the selected dish type in order
                                if (HelperClass.checkDishTypeExistsInOrder(adapterOrderDishes, HelperClass.orderDishTypes[i])) {
                                    // Add the dish type and the returning filled array
                                    jsonItems.put(HelperClass.orderDishTypes[i], HelperClass.dishesToJSON(adapterOrderDishes, HelperClass.orderDishTypes[i]));
                                }

                                // If not, create an empty array
                                else {
                                    // Add the dish type and the returning empty array
                                    jsonItems.put(HelperClass.orderDishTypes[i], HelperClass.emptyJSON());
                                }
                                Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Dishes", jsonItems.toString());
                            }
                        }

                        // If ListViewOrderDishes does not have items
                        else {
                            for (int i = 0; i < HelperClass.orderDishTypes.length; i++) {
                                // Add the dish type and the returning empty array
                                jsonItems.put(HelperClass.orderDishTypes[i], HelperClass.emptyJSON());
                            }
                            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Dishes", jsonItems.toString());
                        }

                        // If ListViewOrderMenus has items
                        if (!adapterOrderMenus.isEmpty()) {
                            // Transform all menus to JSON and add it to the JSONObject "items"
                            jsonItems.put("menus", HelperClass.menusToJSON(adapterOrderMenus));
                            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Menus", jsonItems.toString());
                        }

                        // If ListViewOrderMenus does not have items
                        else {
                            // Add the menu and the returning empty array
                            jsonItems.put("menus", HelperClass.emptyJSON());
                            Log.d(getString(R.string.EDIT_ORDER_SELECTION_FRAGMENT) + "Menus", jsonItems.toString());
                        }

                        // Finally, create an "order" object with the created JSONArray
                        jsonObjectAll.put("order", jsonItems);
                        returningJson = jsonObjectAll.toString();
                        Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Returning JSON", returningJson);

                        try {
                            // Send the JSON to process the order
                            data[0] = new FetchDataPHP().execute("send_order", returningJson, "new").get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }

                        // If the returned value is error
                        if (data[0].equals("error")) {
                            throw new ErorRetrievingInfoException(getString(R.string.ORDER_CREATION_ERROR_SENDING_ORDER_MESSAGE));
                        }

                        // If the returned value is 0
                        else if (data[0].equals("0")) {
                            // Show a dialog with an error receiving message
                            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Order", getString(R.string.ORDER_CREATION_ERROR_INSERTING_ORDER_MESSAGE));
                            HelperClass.createDialogMessageNeutral(getString(R.string.ERROR),
                                    getString(R.string.ORDER_CREATION_ERROR_INSERTING_ORDER_MESSAGE),
                                    getString(R.string.OK),
                                    getContext());
                        }

                        // If is different than 0 or error (will be 1)
                        else {
                            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Order", "Rows inserted. Order created.");

                            // Show a confirmation Toast message
                            Toast.makeText(getActivity(), "Order successfully created", Toast.LENGTH_SHORT).show();

                            // Clear the order notes
                            DataClass.orderNotes = null;

                            // Go back to Home
                            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ErorRetrievingInfoException e) {
                        // Show a dialog with an error sending message
                        e.printStackTrace();
                        HelperClass.createDialogMessageNeutral(getString(R.string.ERROR),
                                e.getMessage(),
                                getString(R.string.OK),
                                getContext());
                    }
                }

                // If order has no items, pop a Toast message
                else {
                    Toast.makeText(getActivity(), "Please, fill the order", Toast.LENGTH_SHORT).show();
                }
            });

            // When dismiss button is clicked
            btnDismiss.setOnClickListener(v -> dismissDialog());
        } catch (NullPointerException ex) {
            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Table", "Null table!!! Crash incoming!!!");
            Toast.makeText(getActivity(), "Error while retrieving the table", Toast.LENGTH_SHORT).show();
            getFragmentManager().popBackStack();
        } catch (ErorRetrievingInfoException e) {
            Log.d(getString(R.string.EDIT_ORDER_SELECTION_FRAGMENT) + "RetrievingException", e.getMessage());
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            getFragmentManager().popBackStack();
        }
        return fView;
    }

    private void dismissDialog() {
        // Create dialog to confirm the dismiss
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        builder.setTitle(getString(R.string.NEW_ORDER))
                .setMessage(getString(R.string.NEW_ORDER_SELECTION_CONFIRM_DISMISS))
                .setNegativeButton(getString(R.string.NO), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.YES), (dialog, which) -> {
                    DataClass.orderNotes = null;
                    getFragmentManager().popBackStack();
                })
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
        // If back is pressed, show the dismiss dialog
        dismissDialog();
        return true;
    }

    @Override
    public void onMenuCreatedSubmit(Menu menu) {
        if (!adapterOrderMenus.isEmpty()) {
            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Adapter order menus", "AdapterMenu is not empty.");
            int index = HelperClass.checkIfMenuExists(adapterOrderMenus, menu);
            if (index != -1) {
                Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Adapter order menus", "Menu already exist. Adding one to quantity.");
                adapterOrderMenus.getItem(index).addToQuantity();
                adapterOrderMenus.notifyDataSetChanged();
                lvOrderMenus.setAdapter(adapterOrderMenus);
            } else {
                Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Adapter order menus", "Menu does not exist. Adding it.");
                adapterOrderMenus.add(menu);
                adapterOrderMenus.notifyDataSetChanged();
                lvOrderMenus.setAdapter(adapterOrderMenus);
            }
        } else {
            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Adapter order menus", "AdapterMenu is empty.");
            adapterOrderMenus.add(menu);
            adapterOrderMenus.notifyDataSetChanged();
            lvOrderMenus.setAdapter(adapterOrderMenus);
        }
    }

    private void references(View fView) {
        tvTitle = fView.findViewById(R.id.tvTitleSelectionNew);
        spDishType = fView.findViewById(R.id.spDishTypeNew);
        btnDismiss = fView.findViewById(R.id.btnDismissSelectionNew);
        lvDishes = fView.findViewById(R.id.lvDishesNew);
        lvOrderDishes = fView.findViewById(R.id.lvOrderDishesNew);
        lvOrderMenus = fView.findViewById(R.id.lvOrderMenusNew);
        rgDishMenu = fView.findViewById(R.id.rgDishMenuNew);
        rbDishes = fView.findViewById(R.id.rbDishesNew);
        rbMenus = fView.findViewById(R.id.rbMenusNew);
        btnOrderNotes = fView.findViewById(R.id.btnOrderNotesNew);
        btnCreateOrder = fView.findViewById(R.id.btnCreateOrderNew);
    }
}
