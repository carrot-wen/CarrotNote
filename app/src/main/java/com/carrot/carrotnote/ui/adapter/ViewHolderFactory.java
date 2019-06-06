package com.carrot.carrotnote.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Objects;

public abstract class ViewHolderFactory<V, VH extends ViewHolder<V>> {

    private OnItemClickListener<V> mClickListener;
    private OnItemLongClickListener<V> mLongClickListener;
    private ItemsComparator<V> mItemsComparator;
    private ContentComparator<V> mContentComparator;

    final void build(AdapterBuilder<V, VH> builder) {
        mClickListener = builder.clickListener;
        mLongClickListener = builder.longClickListener;
        mItemsComparator = builder.itemsComparator != null ? builder.itemsComparator
                : Objects::equals;
        mContentComparator = builder.contentComparator != null ? builder.contentComparator
                : Objects::equals;
    }

    public abstract int getViewType();

    public SingleAdapter<V, VH> get(Context context) {
        return SingleAdapter.of(context, this);
    }

    public abstract VH getViewHolder(LayoutInflater inflater, ViewGroup parent);

    public OnItemClickListener<V> getClickListener() {
        return mClickListener;
    }

    public OnItemLongClickListener<V> getLongClickListener() {
        return mLongClickListener;
    }

    public ItemsComparator<V> getItemsComparator() {
        return mItemsComparator;
    }

    public ContentComparator<V> getContentComparator() {
        return mContentComparator;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(T item);
    }

    public interface ItemsComparator<T> {
        boolean areItemsTheSame(T oldItem, T newItem);
    }

    public interface ContentComparator<T> {
        boolean areContentTheSame(T oldItem, T newItem);
    }

}
