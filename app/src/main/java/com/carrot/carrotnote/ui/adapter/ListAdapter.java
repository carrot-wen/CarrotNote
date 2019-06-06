package com.carrot.carrotnote.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class ListAdapter<T> extends RecyclerView.Adapter<ListAdapter.ViewHolder<T>> {

    private List<T> mDataList;
    private OnItemClickListener<T> mClickListener;
    private OnItemLongClickListener<T> mLongClickListener;
    private ItemsComparator<T> mItemsComparator;
    private ContentComparator<T> mContentComparator;
    private onCreateItemListener<T> mOnCreateItemListener;

    private ListAdapter(Builder<T> builder) {
        mClickListener = builder.clickListener;
        mLongClickListener = builder.longClickListener;
        mItemsComparator = builder.itemsComparator != null ? builder.itemsComparator
                : Objects::equals;
        mContentComparator = builder.contentComparator != null ? builder.contentComparator
                : Objects::equals;

        mOnCreateItemListener = builder.onCreateItemListener;
    }

    /**
     * @param clazz to ensure type of item
     * @param <T>   type of item
     * @return a builder of adapter
     */
    static <T> Builder<T> getBuilder(Class<T> clazz) {
        return new Builder<>();
    }

    public void setItemsComparator(ItemsComparator<T> itemsComparator) {
        mItemsComparator = itemsComparator;
    }

    public void setContentComparator(ContentComparator<T> contentComparator) {
        mContentComparator = contentComparator;
    }

    @NonNull
    @Override
    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  mOnCreateItemListener.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> holder, int position) {
        T item = mDataList.get(position);
        holder.onBind(item);
        holder.itemView.setOnClickListener(mClickListener == null ? null :
                v -> mClickListener.onItemClick(item));

        holder.itemView.setOnLongClickListener(mLongClickListener == null ? null :
                v -> mLongClickListener.onItemLongClick(item));
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public void setData(@NonNull List<T> list) {
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
                    return mItemsComparator.areItemsTheSame(
                            mDataList.get(oldItemPosition), list.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return mContentComparator.areContentTheSame(
                            mDataList.get(oldItemPosition), list.get(newItemPosition));
                }
            });
            mDataList = list;
            result.dispatchUpdatesTo(this);
        }
    }

    public void setClickListener(OnItemClickListener<T> listener) {
        mClickListener = listener;
    }

    public void setLongClickListener(OnItemLongClickListener<T> listener) {
        mLongClickListener = listener;
    }

    public static abstract class ViewHolder<V> extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }


        abstract void onBind(V item);
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

    public interface OnBindItemListener<T> {
        boolean onBind(T item);
    }

    public interface onCreateItemListener<T> {
        ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType);
    }


    public static class Builder<T> {
        private Context mContext;
        private OnItemClickListener<T> clickListener;
        private OnItemLongClickListener<T> longClickListener;
        private ItemsComparator<T> itemsComparator;
        private ContentComparator<T> contentComparator;
        private onCreateItemListener<T> onCreateItemListener;

        public void setContext(Context context) {
            mContext = context;
        }

        public Builder<T> setClickListener(OnItemClickListener<T> clickListener) {
            this.clickListener = clickListener;
            return this;
        }

        public Builder<T> setLongClickListener(OnItemLongClickListener<T> longClickListener) {
            this.longClickListener = longClickListener;
            return this;
        }

        public Builder<T> setItemsComparator(ItemsComparator<T> itemsComparator) {
            this.itemsComparator = itemsComparator;
            return this;
        }

        public Builder<T> setContentComparator(ContentComparator<T> contentComparator) {
            this.contentComparator = contentComparator;
            return this;
        }

        public Builder<T> setCreateItemListener(onCreateItemListener<T> onCreateItemListener) {
            this.onCreateItemListener = onCreateItemListener;
            return this;
        }

        public ListAdapter<T> build() {
            if (onCreateItemListener == null) {
                throw new RuntimeException("onCreateItemListener must be init");
            }
            if (mContext == null) {
                throw new RuntimeException("context must be init");
            }
            return new ListAdapter<>(this);
        }
    }


}
