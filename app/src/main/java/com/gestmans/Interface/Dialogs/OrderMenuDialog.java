package com.gestmans.Interface.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.gestmans.Business.FetchDataPHP;
import com.gestmans.Business.HelperClass;
import com.gestmans.Business.OrdersUtilitiesClass;
import com.gestmans.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class OrderMenuDialog extends AppCompatDialogFragment {

    private Spinner spDrink, spFirst, spSecond, spDessert;
    private AlertDialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_order_menu, null);
        spDrink = view.findViewById(R.id.spDrinkMenu);
        spFirst = view.findViewById(R.id.spFirstMenu);
        spSecond = view.findViewById(R.id.spSecondMenu);
        spDessert = view.findViewById(R.id.spDessertMenu);
        builder.setView(view)
                .setTitle(getString(R.string.ORDER_MENU_CREATE_MENU));
        setCancelable(false);

        dialog = builder.create();
        dialog.show();

        try {
            // Get the table sent by bundle
            String menu = getArguments().getString("menu");

            // Get the dishes of the selected menu
            String data = new FetchDataPHP().execute("get_dishes_menu", "menu", menu).get();
            Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), data);

            // Split the data string
            String[] allDishes = data.split("-");

            //TRANSFORMAR EN OBJETO Y TRATAR O VICEVERSA

            for (int i = 0; i < HelperClass.menuDishTypes.length; i++) {
                for (int j = 0; j < allDishes.length; j++) {
                    if (allDishes[j].equals(HelperClass.menuDishTypes[i])) {

                    }
                }
                // Transform String to List and capitalize first letter
                List<String> listDishTypes = OrdersUtilitiesClass.stringToListCapitalize(data);

                // Put the list on the spinner
                ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getActivity(), R.layout.spinner_text_selected, listDishTypes);
                adapterSpinner.setDropDownViewResource(R.layout.spinner_text_dropdown);
                spDrink.setAdapter(adapterSpinner);
            }

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
        }
        return dialog;
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
