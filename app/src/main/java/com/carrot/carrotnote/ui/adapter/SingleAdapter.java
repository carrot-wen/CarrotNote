package com.carrot.carrotnote.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.carrot.carrotnote.util.LogUtil;

import java.util.List;

public class SingleAdapter<T, VH extends ViewHolder<T>> extends RecyclerView.Adapter<VH> {
    private static final String TAG = "SingleAdapter";
    private ViewHolderFactory<T, VH> mViewHolderFactory;
    private List<T> mDataList;
    private Context mContext;

    private SingleAdapter(Context context, ViewHolderFactory<T, VH> ViewHolderFactory) {
        mViewHolderFactory = ViewHolderFactory;
        mContext = context;
    }

    public static <T, VH extends ViewHolder<T>> SingleAdapter<T, VH> of(Context context, ViewHolderFactory<T, VH> viewHolderFactory) {
        return new SingleAdapter<>(context, viewHolderFactory);
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mViewHolderFactory.getViewHolder(LayoutInflater.from(mContext), parent);
    }

    @Override
    public int getItemViewType(int position) {
        return mViewHolderFactory.getViewType();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        T item = mDataList.get(position);
        if (mViewHolderFactory.getClickListener() != null) {
            holder.itemView.setOnClickListener(v -> {
                LogUtil.d(TAG, "onBindViewHolder: " + item.getClass());
                mViewHolderFactory.getClickListener().onItemClick(item);
            });
        }
        if (mViewHolderFactory.getLongClickListener() != null) {
            holder.itemView.setOnLongClickListener(v -> mViewHolderFactory.getLongClickListener().onItemLongClick(item));
        }
        holder.onBind(item);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }



    private List<T>  sort(List<T> list) {
        return list;
    }

    public void setData(@NonNull List<T> data) {

        List<T> list = sort(data);

        if (mDataList == null) {
            mDataList = list;
            notifyItemRangeChanged(0, mDataList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mDataList.size();
                }

                @Override
                public int getNewListSize() {
                    return list.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mViewHolderFactory.getItemsComparator().areItemsTheSame(
                            mDataList.get(oldItemPosition), list.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return mViewHolderFactory.getContentComparator().areContentTheSame(
                            mDataList.get(oldItemPosition), list.get(newItemPosition));
                }
            });
            mDataList = list;
            result.dispatchUpdatesTo(this);
        }
    }
}
