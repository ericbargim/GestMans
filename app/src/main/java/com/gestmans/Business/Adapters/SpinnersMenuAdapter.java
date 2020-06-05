package com.gestmans.Business.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gestmans.Business.Utilities.App;
import com.gestmans.Business.Objects.Dish;
import com.gestmans.R;

import java.util.List;

public class SpinnersMenuAdapter extends ArrayAdapter<Dish> {
    private Context context;
    private List<Dish> dishes;

    public SpinnersMenuAdapter(Context context, int textViewResourceId, List<Dish> dishes) {
        super(context, textViewResourceId, dishes);
        this.context = context;
        this.dishes = dishes;
    }

    @Override
    public int getCount(){
        return dishes.size();
    }

    @Override
    public Dish getItem(int position){
        return dishes.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Create a dynamic TextView getting the view, and change the color
        TextView tvName = (TextView) super.getView(position, convertView, parent);
        tvName.setTextColor(ContextCompat.getColor(App.getContext(), R.color.purpleGradient));

        // Get the current item using the dishes array and the current position
        tvName.setText(dishes.get(position).getName());

        return tvName;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        // When the spinner is on "choose" mode, we use the same view
        TextView tvName = (TextView) super.getDropDownView(position, convertView, parent);
        tvName.setTextColor(ContextCompat.getColor(App.getContext(), R.color.purpleGradient));

        // For each position of the spinner, set the name of the dishes respectively
        tvName.setText(dishes.get(position).getName());

        return tvName;
    }
}
