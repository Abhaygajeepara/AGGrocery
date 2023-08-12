package com.example.aggrocery;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.aggrocery.Common.CustomAlertDialog;
import com.example.aggrocery.Common.StringKeyboard;
import com.example.aggrocery.DBHelper.DBHelper;
import com.example.aggrocery.databinding.FragmentPurchaseBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PurchaseFragment extends Fragment {

    private FragmentPurchaseBinding purchaseBinding;
    private Context mContext;
    private DBHelper databaseHelper;
    private DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormat;
    public PurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        purchaseBinding = FragmentPurchaseBinding.inflate(inflater, container, false);
        View view = purchaseBinding.getRoot();

        // Initialize the database helper
        databaseHelper = new DBHelper(mContext);

        // Initialize DatePickerDialog
        setupDatePicker();

        // Set onClickListener for Purchase Date EditText
        purchaseBinding.edtPurchaseDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        // Set onClickListener for Submit Button
        purchaseBinding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    savePurchaseDetails();
                }
            }
        });

        // Set onClickListener for Cancel Button
        purchaseBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the home page or perform any required action
                // For example: getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        dateFormat  = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());
                        purchaseBinding.edtPurchaseDateEditText.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);
    }

    private boolean validateInput() {
        String itemCode = purchaseBinding.edtItemCodeEditText.getText().toString().trim();
        String qtyPurchasedString = purchaseBinding.edtQtyPurchasedEditText.getText().toString().trim();
        String purchaseDate = purchaseBinding.edtPurchaseDateEditText.getText().toString().trim();

        if (itemCode.isEmpty() || qtyPurchasedString.isEmpty() || purchaseDate.isEmpty()) {

            CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                    StringKeyboard.error_Code,
                    StringKeyboard.error_allFieldsRequired_message
            );
            customAlertDialog.show(requireActivity().getSupportFragmentManager(),  StringKeyboard.error_CustomAlertDialog);

            return false;
        }

        int qtyPurchased = Integer.parseInt(qtyPurchasedString);
        if (qtyPurchased <= 0) {

            CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                    StringKeyboard.error_Code,
                    StringKeyboard.quantity_mustGreaterThanZero
            );
            customAlertDialog.show(requireActivity().getSupportFragmentManager(),  StringKeyboard.error_CustomAlertDialog);
            return false;
        }

        Date selectedDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            selectedDate = dateFormat.parse(purchaseDate);
            Date currentDate = new Date();
            if (selectedDate.after(currentDate)) {
                CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                        StringKeyboard.error_Code,
                        StringKeyboard.purchaseDateNotBeFutureDate
                );
                customAlertDialog.show(requireActivity().getSupportFragmentManager(),  StringKeyboard.error_CustomAlertDialog);
return  false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void savePurchaseDetails() {
        String itemCode = purchaseBinding.edtItemCodeEditText.getText().toString().trim();
        int qtyPurchased = Integer.parseInt(purchaseBinding.edtQtyPurchasedEditText.getText().toString().trim());
        String purchaseDateString = purchaseBinding.edtPurchaseDateEditText.getText().toString().trim();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date purchaseDate;

        try {
            purchaseDate = dateFormat.parse(purchaseDateString);
        } catch (Exception e) {
            Toast.makeText(mContext, "Invalid purchase date format.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.checkItemExistsInStock(itemCode)) {
            int availableQuantity = databaseHelper.getAvailableQuantity(itemCode);

            if (availableQuantity > 0 && qtyPurchased <= availableQuantity) {
                boolean success = databaseHelper.addPurchaseDetails(itemCode, qtyPurchased, purchaseDate);
                if (success) {
                    boolean updateSuccess = databaseHelper.updateStockQuantity(itemCode, availableQuantity + qtyPurchased);
                    if (updateSuccess) {
                        CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                                StringKeyboard.error_Code,
                                StringKeyboard.purchase_updated
                        );
                        customAlertDialog.show(requireActivity().getSupportFragmentManager(),  StringKeyboard.error_CustomAlertDialog);

                        clearInputFields();
                    } else {
                        CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                                StringKeyboard.error_Code,
                                StringKeyboard.failedtoUpdateQuantity
                        );
                        customAlertDialog.show(requireActivity().getSupportFragmentManager(),  StringKeyboard.error_CustomAlertDialog);

                    }
                } else {
                    CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                            StringKeyboard.error_Code,
                            StringKeyboard.failtoAddPurchaseDetails
                    );
                    customAlertDialog.show(requireActivity().getSupportFragmentManager(),  StringKeyboard.error_CustomAlertDialog);
                }
            } else {
                CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                        StringKeyboard.error_Code,
                        StringKeyboard.notEnoughQTYAvailable
                );
                customAlertDialog.show(requireActivity().getSupportFragmentManager(),  StringKeyboard.error_CustomAlertDialog);

            }
        } else {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(
                    StringKeyboard.error_Code,
                    StringKeyboard.ItemNotExistInDatabase
            );
            customAlertDialog.show(requireActivity().getSupportFragmentManager(),  StringKeyboard.error_CustomAlertDialog);

        }
    }

    private void clearInputFields() {
        purchaseBinding.edtItemCodeEditText.setText("");
        purchaseBinding.edtQtyPurchasedEditText.setText("");
        purchaseBinding.edtPurchaseDateEditText.setText("");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
