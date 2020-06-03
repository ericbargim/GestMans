package com.gestmans.Business.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    private TextView tvName;
    private ImageButton btnSubtract;
    private TextView tvQuantity;
    private ImageButton btnAdd;
    private ImageButton btnRemove;

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

        // Find references on XML
        tvName = convertView.findViewById(R.id.tvNameTicket);
        btnSubtract = convertView.findViewById(R.id.btnSubtractTicket);
        tvQuantity = convertView.findViewById(R.id.tvQuantityTicket);
        btnAdd = convertView.findViewById(R.id.btnAddTicket);
        btnRemove = convertView.findViewById(R.id.btnRemoveTicket);

        // Set text on the fields
        tvName.setText(menu.getMenuName());
        tvQuantity.setText(Integer.toString(menu.getMenuQuantity()));

        // Add button listener
        btnAdd.setOnClickListener(v -> {
            menu.addToQuantity();
            tvQuantity.setText(Integer.toString(menu.getMenuQuantity()));
            notifyDataSetChanged();
        });

        // Add subtract listener
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
                    .setMessage(App.getContext().getString(R.string.ORDER_QUESTION_REMOVE_DISH, menu.getMenuName()))
                    .setPositiveButton(App.getContext().getString(R.string.YES), (dialog, which) -> {
                        Log.d(App.getContext().getString(R.string.LIST_VIEW_TICKET_ADAPTER_CLASS), "Positive clicked");
                        menus.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    })
                    .setNegativeButton(App.getContext().getString(R.string.NO), (dialog, which) -> {
                        Log.d(App.getContext().getString(R.string.LIST_VIEW_TICKET_ADAPTER_CLASS), "Negative clicked");
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
}
