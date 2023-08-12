package com.example.aggrocery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggrocery.DBHelper.DBHelper;
import com.example.aggrocery.Models.StockModel;
import com.example.aggrocery.databinding.FragmentListStockBinding;

import java.util.List;

public class ListStockFragment extends Fragment {

    private FragmentListStockBinding fragmentListStockBinding;
    private RecyclerView recyclerView;
    private StockListAdapter adapter;
    private List<StockModel> stockList;
    DBHelper dbHelper;
    public ListStockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentListStockBinding = FragmentListStockBinding.inflate(inflater, container, false);
        View view = fragmentListStockBinding.getRoot();

        recyclerView = fragmentListStockBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Replace this with your logic to retrieve the stock list from the database
        dbHelper = new DBHelper(requireContext());

        stockList = dbHelper.getAllStockItems();

        if (stockList.isEmpty()) {
            // If no data found, display a message
            fragmentListStockBinding.noDataTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // Data found, display the RecyclerView
            fragmentListStockBinding.noDataTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new StockListAdapter(stockList);
            recyclerView.setAdapter(adapter);
        }



        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentListStockBinding = null;
    }
}