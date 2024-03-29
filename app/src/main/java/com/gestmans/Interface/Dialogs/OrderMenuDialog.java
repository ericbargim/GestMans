package com.gestmans.Interface.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.gestmans.Business.Exceptions.ErorRetrievingInfoException;
import com.gestmans.Business.Utilities.FetchDataPHP;
import com.gestmans.Business.Objects.Dish;
import com.gestmans.Business.Objects.Menu;
import com.gestmans.Business.Adapters.SpinnersMenuAdapter;
import com.gestmans.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class OrderMenuDialog extends AppCompatDialogFragment {

    private Spinner spDrink, spFirst, spSecond, spDessert;
    private Button btnCreateMenu;
    private AlertDialog dialog;

    // Interface needed to send the menu back to the Fragment
    private OrderMenuDialog.OnMenuCreatedListener callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (OnMenuCreatedListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnMenuCreatedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Build and show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_order_menu, null);
        builder.setView(view)
                .setTitle(getString(R.string.ORDER_MENU_CREATE_MENU));
        setCancelable(false);
        dialog = builder.create();
        dialog.show();

        // Reference the elements from the XML layout
        references(view);

        try {
            // Get the table sent by bundle
            String menuName = getArguments().getString("menu");

            // Get the dishes of the selected menu
            String data = new FetchDataPHP().execute("get_dishes_menu", "menu", menuName).get();

            if (data.equals("error")) {
                throw new ErorRetrievingInfoException("Error getting dishes of menu");
            }

            // Split the data string
            String[] allDishes = data.split("-");

            // Create the Lists to store the dishes
            List<Dish> alDrinks = new ArrayList<>();
            List<Dish> alFirsts = new ArrayList<>();
            List<Dish> alSeconds = new ArrayList<>();
            List<Dish> alDesserts = new ArrayList<>();

            // Loop the received dishes
            for (int i = 0; i < allDishes.length; i++) {
                // Store the dish information on a Dish object
                String actualDish = allDishes[i];
                String[] actualDishParts = actualDish.split(",");
                String dishName = actualDishParts[0];
                String dishId = actualDishParts[1];
                String dishType = actualDishParts[2];
                Dish dishObject = new Dish(dishId, dishName, dishType, 1);
                Log.d(getString(R.string.ORDER_MENU_DIALOG) + "Check dish creation", dishObject.toString());

                // Depending on dishType, store the dish on it's List
                switch (dishObject.getDishType()) {
                    case "drink":
                        alDrinks.add(dishObject);
                        break;
                    case "first":
                        alFirsts.add(dishObject);
                        break;
                    case "second":
                        alSeconds.add(dishObject);
                        break;
                    case "dessert":
                        alDesserts.add(dishObject);
                        break;
                }
            }

            // Put the list on the spinner
            SpinnersMenuAdapter adapterSpinnerDrinks = new SpinnersMenuAdapter(getActivity(), R.layout.spinner_text_dropdown, alDrinks);
            adapterSpinnerDrinks.setDropDownViewResource(R.layout.spinner_text_dropdown);
            spDrink.setAdapter(adapterSpinnerDrinks);

            // Put the list on the spinner
            SpinnersMenuAdapter adapterSpinnerFirsts = new SpinnersMenuAdapter(getActivity(), R.layout.spinner_text_dropdown, alFirsts);
            adapterSpinnerFirsts.setDropDownViewResource(R.layout.spinner_text_dropdown);
            spFirst.setAdapter(adapterSpinnerFirsts);

            // Put the list on the spinner
            SpinnersMenuAdapter adapterSpinnerSeconds = new SpinnersMenuAdapter(getActivity(), R.layout.spinner_text_dropdown, alSeconds);
            adapterSpinnerSeconds.setDropDownViewResource(R.layout.spinner_text_dropdown);
            spSecond.setAdapter(adapterSpinnerSeconds);

            // Put the list on the spinner
            SpinnersMenuAdapter adapterSpinnerDesserts = new SpinnersMenuAdapter(getActivity(), R.layout.spinner_text_dropdown, alDesserts);
            adapterSpinnerDesserts.setDropDownViewResource(R.layout.spinner_text_dropdown);
            spDessert.setAdapter(adapterSpinnerDesserts);

            // When Create menu is clicked
            btnCreateMenu.setOnClickListener(v -> {
                // Get all the selected dishes
                Dish drink = (Dish) spDrink.getSelectedItem();
                Dish first = (Dish) spFirst.getSelectedItem();
                Dish second = (Dish) spSecond.getSelectedItem();
                Dish dessert = (Dish) spDessert.getSelectedItem();

                // Create the menu object
                Menu menu = new Menu(menuName, 1, drink, first, second, dessert);
                Log.d(getString(R.string.ORDER_MENU_DIALOG) + "Menu created", menu.toString());

                // Add the menu object to the ListViewMenu, calling the interface
                callback.onMenuCreatedSubmit(menu);
                dialog.dismiss();
            });

            // When back button is clicked
            dialog.setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() != KeyEvent.ACTION_DOWN)
                        return true;
                    else {
                        dismissDialog();
                        return true;
                    }
                }
                return true;
            });
        } catch (NullPointerException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (ErorRetrievingInfoException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
        return dialog;
    }

    private void references(View view) {
        spDrink = view.findViewById(R.id.spDrinkMenu);
        spFirst = view.findViewById(R.id.spFirstMenu);
        spSecond = view.findViewById(R.id.spSecondMenu);
        spDessert = view.findViewById(R.id.spDessertMenu);
        btnCreateMenu = view.findViewById(R.id.btnCreateMenu);
    }

    // The interface calls the onMenuCreatedSubmit method on the Fragment
    public interface OnMenuCreatedListener {
        public void onMenuCreatedSubmit(Menu menu);
    }

    private void dismissDialog() {
        // Create dialog to confirm the dismiss
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        builder.setTitle(getString(R.string.ORDER_MENU_CREATE_MENU))
                .setMessage(getString(R.string.ORDER_DETAILS_DISMISS_MESSAGE))
                .setNegativeButton(getString(R.string.NO), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(getString(R.string.YES), (dialog, which) -> this.dialog.dismiss())
                .create();
        final AlertDialog dialog = builder.show();

        // Change the buttons color
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purpleGradient, null));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purpleGradient, null));

        // Show the dialog
        dialog.show();
    }


}
