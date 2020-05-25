package com.gestmans.Business;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gestmans.Business.Objects.Dish;
import com.gestmans.R;

import java.util.ArrayList;

public class ListViewDishesAdapter extends ArrayAdapter<Dish> {
    public ListViewDishesAdapter(Context context, ArrayList<Dish> dishes) {
        super(context, 0, dishes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the dish of the selected position
        Dish dish = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_dishes, parent, false);
        }

        // Find references on XML
        TextView tvName = convertView.findViewById(R.id.tvNameDishes);

        // Set text on the fields
        tvName.setText(dish.getName());

        return convertView;
    }
}
