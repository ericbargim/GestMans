package com.gestmans.Interface.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.gestmans.Business.Utilities.DataClass;
import com.gestmans.R;

public class OrderNotesDialog extends AppCompatDialogFragment {

    private EditText etOrderDetail;
    private Button btnSaveOrderDetail;
    private AlertDialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_order_notes, null);
        etOrderDetail = view.findViewById(R.id.etOrderDetail);
        btnSaveOrderDetail = view.findViewById(R.id.btnSaveOrderDetail);
        builder.setView(view)
                .setTitle(getString(R.string.ORDER_NOTES));
        setCancelable(false);

        dialog = builder.create();
        dialog.show();
        if (DataClass.orderNotes != null) {
            etOrderDetail.setText(DataClass.orderNotes);
        }
        btnSaveOrderDetail.setOnClickListener(v -> {
            try {
                if (String.valueOf(etOrderDetail.getText()).equals("")) {
                    throw new NullPointerException();
                }
                DataClass.orderNotes = String.valueOf(etOrderDetail.getText());
                dialog.dismiss();
            } catch (NullPointerException e) {
                Toast.makeText(getActivity(), "Please, enter a valid order note.", Toast.LENGTH_SHORT).show();
            }

        });

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
        return dialog;
    }

    private void dismissDialog() {
        // Create dialog to confirm the dismiss
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        builder.setTitle(getString(R.string.ORDER_NOTES))
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
