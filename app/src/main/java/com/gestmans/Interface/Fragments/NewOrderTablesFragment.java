package com.gestmans.Interface.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gestmans.Business.FetchDataPHP;
import com.gestmans.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewOrderTablesFragment extends Fragment {

    private ListView lvTablesNew;

    public NewOrderTablesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fView = inflater.inflate(R.layout.fragment_new_order_tables, container, false);

        // Reference the elements from the XML layout
        references(fView);
        List<String> al = new ArrayList<>();
        String json = "";

        try {
            // Get the available room tables
            json = new FetchDataPHP().execute("available_room_tables").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the returned values is not error
        if (!json.equals("error")) {
            // Format the table names and add it to the List
            String[] numTables = json.split("-");
            for (String numTable : numTables) {
                al.add("Table " + numTable);
            }

            // If the list is empty, add "empty" value value to the list
            if (al.isEmpty()) al.add(getString(R.string.ORDER_TABLES_NO_TABLES));

            // Adapt the array to the ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.listview_tables, al);
            lvTablesNew.setAdapter(adapter);

            // Check if the first element is not the "error" message
            if (!al.get(0).equals(getString(R.string.ORDER_TABLES_NO_TABLES))) {

                // When item in ListView is clicked
                lvTablesNew.setOnItemClickListener((parent, view, position, id) -> {
                    Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), lvTablesNew.getItemAtPosition(position).toString());

                    // Load the fragment giving it the table selected
                    NewOrderSelectionFragment fragment = new NewOrderSelectionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("table", lvTablesNew.getItemAtPosition(position).toString());
                    fragment.setArguments(bundle);

                    Log.d(getString(R.string.NEW_ORDER_SELECTION_FRAGMENT), lvTablesNew.getItemAtPosition(position).toString());

                    // Go to the fragment
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.fragmentViewLayout, fragment).addToBackStack(null).commit();
                });
            }

            // If the returned value is an error
        } else {
            // Add error value to the list
            al.add(getString(R.string.ORDER_TABLES_ERROR_TABLES));

            // Adapt the list to the ListView
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.listview_tables, al);
            lvTablesNew.setAdapter(arrayAdapter);
        }
        return fView;
    }

    private void references(View fView) {
        lvTablesNew = fView.findViewById(R.id.lvTablesNew);
    }
}
