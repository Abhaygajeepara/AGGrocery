package com.example.aggrocery;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.aggrocery.Common.CustomAlertDialog;
import com.example.aggrocery.Common.StringKeyboard;
import com.example.aggrocery.DBHelper.DBHelper;
import com.example.aggrocery.R;
import com.example.aggrocery.databinding.FragmentSalesBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SalesFragment extends Fragment {

    private FragmentSalesBinding salesBinding;
    private EditText salesDateEditText;
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    private Context mContext;

    private DBHelper databaseHelper;

    public SalesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        databaseHelper = new DBHelper(mContext); // Initialize your DatabaseHelper
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        salesBinding = FragmentSalesBinding.inflate(inflater, container, false);
        View view = salesBinding.getRoot();
        salesDateEditText = salesBinding.edtDateEditText;



        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the EditText with the selected date
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        salesDateEditText.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Show the DatePicker when the EditText is clicked
        salesDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        // Submit Button
        salesBinding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate and process sales
                validateAndProcessSales();
            }
        });

        // Cancel Button
        salesBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to home page
                // Add your navigation logic here
            }
        });

        return view;
    }

    private void validateAndProcessSales() {
        // Validate input fields
        String itemCode = salesBinding.edtItemCodeEditText.getText().toString().trim();
        String customerName = salesBinding.edtCustomerNameEditText.getText().toString().trim();
        String customerEmail = salesBinding.edtCustomerEmailEditText.getText().toString().trim();
        String qtySoldStr = salesBinding.edtQtySoldEditText.getText().toString().trim();
        String dateStr = salesBinding.edtDateEditText.getText().toString().trim();

        if (TextUtils.isEmpty(itemCode) || TextUtils.isEmpty(customerName) ||
                TextUtils.isEmpty(customerEmail) || TextUtils.isEmpty(qtySoldStr) ||
                TextUtils.isEmpty(dateStr)) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                    StringKeyboard.error_Code,
                    StringKeyboard.error_allFieldsRequired_message
            );
            customAlertDialog.show(requireActivity().getSupportFragmentManager(), StringKeyboard.error_CustomAlertDialog);
            return;
        }

        // Convert quantity sold to int
        int qtySold = Integer.parseInt(qtySoldStr);

        // Check if the selected date is a future date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date selectedDate = null;
        try {
            selectedDate = sdf.parse(dateStr);
            Date currentDate = new Date();
            if (selectedDate.after(currentDate)) {
                CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                        StringKeyboard.error_Code,
                        StringKeyboard.saleDateNotBeFutureDate
                );
                customAlertDialog.show(requireActivity().getSupportFragmentManager(),  StringKeyboard.error_CustomAlertDialog);
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Check if the item code exists in the stock
        boolean itemExistsInStock = databaseHelper.checkItemExistsInStock(itemCode);

        if (!itemExistsInStock) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                    StringKeyboard.error_Code,
                    StringKeyboard.ItemNotExistInDatabase
            );
            customAlertDialog.show(requireActivity().getSupportFragmentManager(), StringKeyboard.error_CustomAlertDialog);
            return;
        }

        // Check if the available quantity is sufficient for the quantity sold
        int availableQuantity = databaseHelper.getAvailableQuantity(itemCode); // Replace with your logic
        Log.d("availableQuantity",String.valueOf(availableQuantity));
        if (qtySold > availableQuantity) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                    StringKeyboard.error_Code,
                    StringKeyboard.SaleQtyMoreThanAvailbleQty
            );
            customAlertDialog.show(requireActivity().getSupportFragmentManager(),  StringKeyboard.error_CustomAlertDialog);
            return;
        }

        // If validation passes, update sales details and stock

        // Update sales details in the database
        boolean success = databaseHelper.addSalesDetails(itemCode, customerName, customerEmail, qtySold, selectedDate);

        if (success) {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                    StringKeyboard.error_Code,
                    StringKeyboard.success_CustomAlertDialog
            );
            customAlertDialog.show(requireActivity().getSupportFragmentManager(),  StringKeyboard.error_CustomAlertDialog);

            // Update the Stock table (replace with your actual logic)
            databaseHelper.updateStockQuantity(itemCode, availableQuantity - qtySold);

            // Clear input fields
            clearInputFields();
        } else {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                    StringKeyboard.error_Code,
                    StringKeyboard.fail_to_add_stock
            );
            customAlertDialog.show(requireActivity().getSupportFragmentManager(),  StringKeyboard.error_CustomAlertDialog);
        }
    }

    private void clearInputFields() {
        // Clear input fields
        salesBinding.edtItemCodeEditText.setText("");
        salesBinding.edtCustomerNameEditText.setText("");
        salesBinding.edtCustomerEmailEditText.setText("");
        salesBinding.edtQtySoldEditText.setText("");
        salesBinding.edtDateEditText.setText("");
    }
}
