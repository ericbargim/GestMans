package com.gestmans.Interface.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.gestmans.R;

public class EditOrderSelectionFragment extends Fragment {

    private Spinner spDishType;
    private Button btnDismiss, btnCreateTicket;
    private ListView lvDishes, lvTicket;

    public EditOrderSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fView = inflater.inflate(R.layout.fragment_new_order_selection, container, false);
        references(fView);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.EDIT_ORDER))
                        .setMessage(getString(R.string.EDIT_ORDER_SELECTION_CONFIRM_DISMISS))
                        .setNegativeButton(getString(R.string.NO), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton(getString(R.string.YES), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().onBackPressed();
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
        spDishType = fView.findViewById(R.id.spDishTypeEdit);
        btnDismiss = fView.findViewById(R.id.btnDismissSelectionEdit);
        lvDishes = fView.findViewById(R.id.lvDishesEdit);
        lvTicket = fView.findViewById(R.id.lvTicketEdit);
        btnCreateTicket = fView.findViewById(R.id.btnModifyTicket);
    }
}
