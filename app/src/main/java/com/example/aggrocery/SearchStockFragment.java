package com.example.aggrocery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggrocery.databinding.FragmentSearchStockBinding;


public class SearchStockFragment extends Fragment {


private FragmentSearchStockBinding searchStockBinding;

    public SearchStockFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        searchStockBinding= FragmentSearchStockBinding.inflate(inflater,container,false);
        View view = searchStockBinding.getRoot();
        return view;
    }
}