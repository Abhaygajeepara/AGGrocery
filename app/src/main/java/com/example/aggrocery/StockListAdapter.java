package com.example.aggrocery;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aggrocery.Models.StockModel;
import com.example.aggrocery.databinding.ListItemLayoutBinding;

import java.util.List;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.ViewHolder> {

    private List<StockModel> stockList;

    public StockListAdapter(List<StockModel> stockList) {
        this.stockList = stockList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemLayoutBinding recordLayoutBinding = ListItemLayoutBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(recordLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StockModel stockItem = stockList.get(position);

      holder.recordLayoutBinding.itemCodeTextView.setText(String.valueOf(stockItem.getItemCode()));
        holder.recordLayoutBinding.itemNameTextView.setText(stockItem.getItemName());
        holder.recordLayoutBinding.qtyStockTextView.setText( String.valueOf(stockItem.getQtyInStock()));
        holder.recordLayoutBinding.priceTextView.setText(  String.valueOf(stockItem.getPrice()));
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListItemLayoutBinding recordLayoutBinding;

        public ViewHolder(@NonNull ListItemLayoutBinding binding) {
            super(binding.getRoot());
            recordLayoutBinding = binding;
        }
    }
}
