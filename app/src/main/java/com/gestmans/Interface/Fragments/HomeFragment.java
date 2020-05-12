package com.gestmans.Interface.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gestmans.R;

public class HomeFragment extends Fragment {

    TextView tvWelcomeName;
    CardView cvNewOrder, cvEditOrder, cvLogOut;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fView = inflater.inflate(R.layout.fragment_home, container, false);
        references(fView);
        try {
            String name = getArguments().getString("name");
            tvWelcomeName.setText(getString(R.string.HOME_WELCOME_FILL, name));
        } catch (NullPointerException ex) {
            tvWelcomeName.setText(getString(R.string.HOME_WELCOME_FILL, "User"));
        }

        cvNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragmentViewLayout, new NewOrderTableFragment()).addToBackStack(null).commit();
            }
        });
        cvEditOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Log out")
                        .setMessage("Are you sure you want to log out?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        })
                        .create();

                final AlertDialog dialog = builder.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purpleGradient, null));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purpleGradient, null));
                dialog.show();
            }
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
