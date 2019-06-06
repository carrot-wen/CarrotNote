package com.carrot.carrotnote.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void onBind(T t);
}
