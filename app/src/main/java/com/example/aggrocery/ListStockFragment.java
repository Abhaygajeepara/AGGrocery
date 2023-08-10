package com.example.aggrocery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aggrocery.databinding.FragmentListStockBinding;

public class ListStockFragment extends Fragment {

    private FragmentListStockBinding fragmentListStockBinding;
    public ListStockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentListStockBinding= FragmentListStockBinding.inflate(inflater,container,false);
        View view = fragmentListStockBinding.getRoot();
        return view;
    }
}