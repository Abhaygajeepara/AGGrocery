package com.example.aggrocery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aggrocery.Common.CustomAlertDialog;
import com.example.aggrocery.Common.StringKeyboard;
import com.example.aggrocery.DBHelper.DBHelper;
import com.example.aggrocery.Models.StockModel;
import com.example.aggrocery.databinding.FragmentAddstockBinding;

public class AddStockFragment extends Fragment {

    private FragmentAddstockBinding addBinding;
    private DBHelper dbHelper;
    private Context mContext;
    public AddStockFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        addBinding = FragmentAddstockBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();

        dbHelper = new DBHelper(mContext);

        addBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    saveItemToStock();

                }
            }
        });

        addBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                // Finish the current activity (AddStockFragment)
                getActivity().finish();
            }
        });

        return view;
    }

    private boolean validateInput() {
        String itemName = addBinding.edtItemNameEditText.getText().toString().trim();
        String qtyInStockString = addBinding.edtQtyEditText.getText().toString().trim();
        String priceString = addBinding.edtPriceEditText.getText().toString().trim();

        if (itemName.isEmpty() || qtyInStockString.isEmpty() || priceString.isEmpty()) {
            CustomAlertDialog alertDialog = new CustomAlertDialog(StringKeyboard.error_Code, StringKeyboard.error_allFieldsRequired_message);
            alertDialog.show(requireActivity().getSupportFragmentManager(), StringKeyboard.error_CustomAlertDialog);
            return false;
        }
        int qtyInStock = Integer.parseInt(qtyInStockString);
        double price = Double.parseDouble(priceString);

        // Validate non-negative quantity and price
        if (qtyInStock < 0 || price < 0) {
            CustomAlertDialog alertDialog = new CustomAlertDialog(StringKeyboard.error_Code, StringKeyboard.error_price_non_negative);
            alertDialog.show(requireActivity().getSupportFragmentManager(), StringKeyboard.error_CustomAlertDialog);
            return false;
        }
        RadioGroup radioGroup = addBinding.returnableRadioGroup;
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            CustomAlertDialog alertDialog = new CustomAlertDialog(StringKeyboard.error_Code, StringKeyboard.error_returnAbleFields);
            alertDialog.show(requireActivity().getSupportFragmentManager(), StringKeyboard.error_CustomAlertDialog);
            return false;
        }

        return true;
    }

    private void saveItemToStock() {
        try {
        String itemName = addBinding.edtItemNameEditText.getText().toString().trim();
        int qtyInStock = Integer.parseInt(addBinding.edtQtyEditText.getText().toString().trim());
        double price = Double.parseDouble(addBinding.edtPriceEditText.getText().toString().trim());
            dbHelper = new DBHelper(mContext);
        RadioGroup radioGroup = addBinding.returnableRadioGroup;
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = addBinding.getRoot().findViewById(selectedId);
        boolean isReturnable = radioButton.getText().toString().equals("Returnable");




        StockModel stockModel = new StockModel(itemName, qtyInStock, price, isReturnable);

        long result = dbHelper.insertStockItem(stockModel);


        if (result != -1) {
            // Display a short-duration Toast message
            Toast.makeText(mContext, StringKeyboard.addStockSuccessfully, Toast.LENGTH_SHORT).show();





            clearInputFields();
        } else {
            CustomAlertDialog alertDialog = new CustomAlertDialog(StringKeyboard.error_Code, StringKeyboard.failToAddStock);
            alertDialog.show(requireActivity().getSupportFragmentManager(), StringKeyboard.error_CustomAlertDialog);
        }
    }catch(Exception e){

            CustomAlertDialog alertDialog = new CustomAlertDialog(StringKeyboard.error_Code, "An error occurred while adding item to stock."+e.toString());
            alertDialog.show(requireActivity().getSupportFragmentManager(), StringKeyboard.error_CustomAlertDialog);
        }
    }

    private void clearInputFields() {
        addBinding.edtItemNameEditText.setText("");
        addBinding.edtQtyEditText.setText("");
        addBinding.edtPriceEditText.setText("");
        addBinding.returnableRadioGroup.clearCheck();
    }

}
