package com.gestmans.Interface.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gestmans.Business.fetchDataPHP;
import com.gestmans.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditOrderTablesFragment extends Fragment {

    private ListView lvTablesEdit;

    public EditOrderTablesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fView = inflater.inflate(R.layout.fragment_edit_order_tables, container, false);
        references(fView);
        List<String> al = new ArrayList<>();
        String json = "";
        try {
            json = new fetchDataPHP().execute("unavailable_room_tables").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (!json.equals("error")) {
            String[] numTables = json.split("-");
            for (String numTable : numTables) {
                al.add("Table " + numTable);
            }
            if (al.isEmpty()) al.add(getString(R.string.ORDER_TABLES_NO_TABLES));
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.listview_tables, al);
            lvTablesEdit.setAdapter(arrayAdapter);
            if (!al.get(0).equals(getString(R.string.ORDER_TABLES_NO_TABLES))) {
                lvTablesEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity(), lvTablesEdit.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            al.add(getString(R.string.ORDER_TABLES_NO_TABLES));
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.listview_tables, al);
            lvTablesEdit.setAdapter(arrayAdapter);
        }
        return fView;
    }

    private void references(View fView) {
        lvTablesEdit = fView.findViewById(R.id.lvTablesEdit);
    }
}
