package com.gestmans.Interface.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gestmans.Business.Utilities.DataClass;
import com.gestmans.R;

public class HomeFragment extends Fragment {

    private TextView tvWelcomeName;
    private CardView cvNewOrder, cvEditOrder, cvLogOut;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fView = inflater.inflate(R.layout.fragment_home, container, false);

        // Reference the elements from the XML layout
        references(fView);

        // Set TextView welcome text
        tvWelcomeName.setText(getString(R.string.HOME_WELCOME_FILL, DataClass.username));

        // If NewOrder CardView is clicked
        cvNewOrder.setOnClickListener(v -> {
            // Go to NewOrderFragment
            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.fragmentViewLayout, new NewOrderTablesFragment()).addToBackStack(null).commit();
        });

        // If EditOrder CardView is clicked
        cvEditOrder.setOnClickListener(v -> {
            // Go to EditOrderFragment
            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.fragmentViewLayout, new EditOrderTablesFragment()).addToBackStack(null).commit();
        });

        // If LogOut CardView is clicked
        cvLogOut.setOnClickListener(v -> {
            // Create confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
            builder.setTitle(getString(R.string.LOG_OUT))
                    .setMessage(getString(R.string.HOME_CONFIRM_LOG_OUT))
                    .setNegativeButton(getString(R.string.NO), (dialog, which) -> {
                        // If "No" is clicked, cancel the dialog
                        dialog.cancel();
                    })
                    .setPositiveButton(getString(R.string.YES), (dialog, which) -> {
                        // Set the username null and finish the activity AppMainActivity
                        DataClass.username = null;
                        getActivity().finish();
                    })
                    .create();
            final AlertDialog dialog = builder.show();

            // Change the buttons color
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purpleGradient, null));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purpleGradient, null));

            // Show the dialog
            dialog.show();
        });
        return fView;
    }

    private void references(View fView) {
        tvWelcomeName = fView.findViewById(R.id.tvWelcomeName);
        cvNewOrder = fView.findViewById(R.id.cvNewOrder);
        cvEditOrder = fView.findViewById(R.id.cvEditOrder);
        cvLogOut = fView.findViewById(R.id.cvLogOut);
    }
}
