package com.example.aggrocery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.aggrocery.databinding.FragmentSalesBinding;


public class SalesFragment extends Fragment {

   private FragmentSalesBinding salesBinding;
    public SalesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        salesBinding= FragmentSalesBinding.inflate(inflater,container,false);
        View view = salesBinding.getRoot();
        return view;
    }
}