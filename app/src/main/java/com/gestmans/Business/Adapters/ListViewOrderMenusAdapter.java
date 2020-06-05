package com.gestmans.Business.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.gestmans.Business.Utilities.App;
import com.gestmans.Business.Objects.Menu;
import com.gestmans.R;

import java.util.ArrayList;

public class ListViewOrderMenusAdapter extends ArrayAdapter<Menu> {
    private Context context;
    private ArrayList<Menu> menus;

    private TextView tvName, tvQuantity;
    private ImageButton btnSubtract, btnAdd, btnRemove;

    private TextView tvMenuNameInfo, tvMenuQuantityInfo, tvDishDrinkInfo,
            tvDishFirstInfo, tvDishSecondInfo, tvDishDessertInfo;
    private Button btnCloseInfo;

    public ListViewOrderMenusAdapter(Context context, ArrayList<Menu> menus) {
        super(context, 0, menus);
        this.menus = menus;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the dish of the selected position
        Menu menu = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_order_menus, parent, false);
        }

        // Reference the elements from the XML layout
        references(convertView);

        // Set text on the fields
        tvName.setText(menu.getMenuName());
        tvQuantity.setText(Integer.toString(menu.getMenuQuantity()));

        // If the menu name is clicked
        tvName.setOnClickListener(v -> {
            Log.d(App.getContext().getString(R.string.NEW_ORDER_SELECTION_FRAGMENT) + "Menu info", "Menu clicked.");

            // Build and show a quick menu info dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
            View view = (LayoutInflater.from(context)).inflate(R.layout.dialog_info_menu, null);
            referencesInfo(view);
            builder.setView(view)
                    .setCancelable(true);
            final AlertDialog dialog = builder.show();
            dialog.show();

            // Show the info of the menu
            tvMenuNameInfo.setText(menu.getMenuName());
            tvMenuQuantityInfo.setText(Integer.toString(menu.getMenuQuantity()));
            tvDishDrinkInfo.setText(menu.getDishDrink().getName());
            tvDishFirstInfo.setText(menu.getDishFirst().getName());
            tvDishSecondInfo.setText(menu.getDishSecond().getName());
            tvDishDessertInfo.setText(menu.getDishDessert().getName());

            // If the "OK" button is clicked, close the dialog
            btnCloseInfo.setOnClickListener(v1 -> dialog.dismiss());
        });

        // Add button listener
        btnAdd.setOnClickListener(v -> {
            menu.addToQuantity();
            tvQuantity.setText(Integer.toString(menu.getMenuQuantity()));
            notifyDataSetChanged();
        });

        // Subtract button listener
        btnSubtract.setOnClickListener(v -> {
            menu.removeFromQuantity();
            tvQuantity.setText(Integer.toString(menu.getMenuQuantity()));
            notifyDataSetChanged();
        });

        // Add remove listener
        btnRemove.setOnClickListener(v -> {
            // Create dialog to confirm deleting the selected dish
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
            builder.setTitle(App.getContext().getString(R.string.WARNING))
                    .setMessage(App.getContext().getString(R.string.ORDER_QUESTION_REMOVE_MENU, menu.getMenuName()))
                    .setPositiveButton(App.getContext().getString(R.string.YES), (dialog, which) -> {
                        Log.d(App.getContext().getString(R.string.LIST_VIEW_ORDER_MENUS_ADAPTER_CLASS) + "Remove menu", "Positive clicked");
                        menus.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    })
                    .setNegativeButton(App.getContext().getString(R.string.NO), (dialog, which) -> {
                        Log.d(App.getContext().getString(R.string.LIST_VIEW_ORDER_MENUS_ADAPTER_CLASS) + "Remove menu", "Negative clicked");
                        dialog.dismiss();
                    })
                    .create();
            final AlertDialog dialog = builder.show();

            // Change the buttons color
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.purpleGradient));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getColor(R.color.purpleGradient));

            // Show the dialog
            dialog.show();
        });

        return convertView;
    }

    private void references(@Nullable View convertView) {
        tvName = convertView.findViewById(R.id.tvNameTicket);
        btnSubtract = convertView.findViewById(R.id.btnSubtractTicket);
        tvQuantity = convertView.findViewById(R.id.tvQuantityTicket);
        btnAdd = convertView.findViewById(R.id.btnAddTicket);
        btnRemove = convertView.findViewById(R.id.btnRemoveTicket);
    }

    private void referencesInfo(View view) {
        tvMenuNameInfo = view.findViewById(R.id.tvMenuNameContentInfo);
        tvMenuQuantityInfo = view.findViewById(R.id.tvMenuQuantityContentInfo);
        tvDishDrinkInfo = view.findViewById(R.id.tvDishDrinkContentInfo);
        tvDishFirstInfo = view.findViewById(R.id.tvDishFirstContentInfo);
        tvDishSecondInfo = view.findViewById(R.id.tvDishSecondContentInfo);
        tvDishDessertInfo = view.findViewById(R.id.tvDishDessertContentInfo);
        btnCloseInfo = view.findViewById(R.id.btnCloseMenuInfo);
    }
}
