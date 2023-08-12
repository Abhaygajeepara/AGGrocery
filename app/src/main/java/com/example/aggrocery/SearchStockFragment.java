package com.example.aggrocery;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggrocery.Common.CustomAlertDialog;
import com.example.aggrocery.Common.StringKeyboard;
import com.example.aggrocery.DBHelper.DBHelper;
import com.example.aggrocery.Models.StockModel;
import com.example.aggrocery.databinding.FragmentSearchStockBinding;

import java.util.ArrayList;
import java.util.List;


public class SearchStockFragment extends Fragment {


private FragmentSearchStockBinding searchStockBinding;

    private FragmentSearchStockBinding binding;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setVisibilityToOff();
        dbHelper = new DBHelper(requireContext());

        binding.cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), HomeActivity.class);
            startActivity(intent);
            // Finish the current activity (AddStockFragment)
            getActivity().finish();
        });

        binding.searchButtonItem.setOnClickListener(v -> {
            String itemCode = binding.edtItemCodeEditText.getText().toString().trim();
            if (!itemCode.isEmpty()) {
                StockModel stockItem = dbHelper.searchStockItem(itemCode);
                if (stockItem != null) {
                    setVisibilityToOff();
                    showItemDetails(stockItem);
                } else {
                    showNotFoundAlert();
                }
            } else {
                CustomAlertDialog alertDialog = new CustomAlertDialog(StringKeyboard.error_Code, StringKeyboard.error_enterItemCode);
                alertDialog.show(requireActivity().getSupportFragmentManager(), StringKeyboard.error_CustomAlertDialog);

            }
        });

        return view;
    }
    private void showItemDetails(StockModel stockItem) {
        binding.detailsLayout.setVisibility(View.VISIBLE);

        binding.itemCodeTextView.setText("Item Code: " + stockItem.getItemCode());
        binding.itemNameTextView.setText("Item Name: " + stockItem.getItemName());
        binding.qtyStockTextView.setText("Qty in Stock: " + stockItem.getQtyInStock());
        binding.priceTextView.setText("Price: $" + stockItem.getPrice());
    }

    private void showNotFoundAlert() {
        setVisibilityToOn();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setVisibilityToOn(){
        binding.noDataTextView.setVisibility(View.VISIBLE);
    }
    public void setVisibilityToOff(){
        binding.noDataTextView.setVisibility(View.GONE);
    }

}