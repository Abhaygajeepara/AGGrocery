package com.example.aggrocery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggrocery.databinding.FragmentPurchaseBinding;

public class PurchaseFragment extends Fragment {


private FragmentPurchaseBinding purchaseBinding;
    public PurchaseFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        purchaseBinding= FragmentPurchaseBinding.inflate(inflater,container,false);
        View view = purchaseBinding.getRoot();
        return view;
    }
}