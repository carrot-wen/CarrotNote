package com.carrot.carrotnote.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.carrot.carrotnote.R;
import com.carrot.carrotnote.model.Bill;

public class BillViewHolder extends ViewHolder<Bill> {

    private TextView mNumber;
    private TextView mReason;

    public BillViewHolder(@NonNull View itemView) {
        super(itemView);
        mNumber = itemView.findViewById(R.id.bill_number);
        mReason = itemView.findViewById(R.id.bill_reason);
    }

    @Override
    public void onBind(Bill bill) {
        mNumber.setText(String.valueOf(bill.getNumber()));
        mReason.setText(bill.getReason());
    }


    public static class Factory extends ViewHolderFactory<Bill,BillViewHolder> {

        @Override
        public int getViewType() {
            return 1;
        }

        @Override
        public BillViewHolder getViewHolder(LayoutInflater inflater, ViewGroup parent) {
            View view = inflater.inflate(R.layout.bill_item,parent,false);
            return new BillViewHolder(view);
        }
    }
}
