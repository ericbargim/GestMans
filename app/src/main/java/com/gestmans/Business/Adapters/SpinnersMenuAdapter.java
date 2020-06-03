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
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView tvName = (TextView) super.getView(position, convertView, parent);
        tvName.setTextColor(ContextCompat.getColor(App.getContext(), R.color.purpleGradient));
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        tvName.setText(dishes.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return tvName;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView tvName = (TextView) super.getDropDownView(position, convertView, parent);
        tvName.setTextColor(ContextCompat.getColor(App.getContext(), R.color.purpleGradient));
        tvName.setText(dishes.get(position).getName());

        return tvName;
    }
}
