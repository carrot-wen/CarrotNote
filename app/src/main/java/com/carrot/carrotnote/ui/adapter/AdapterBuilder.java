package com.carrot.carrotnote.ui.adapter;

import android.content.Context;

public class AdapterBuilder<T,V extends ViewHolder<T>> {
     Context mContext;
     ViewHolderFactory.OnItemClickListener<T> clickListener;
     ViewHolderFactory.OnItemLongClickListener<T> longClickListener;
     ViewHolderFactory.ItemsComparator<T> itemsComparator;
     ViewHolderFactory.ContentComparator<T> contentComparator;
     private Class<? extends ViewHolderFactory<T,V>> mFactoryClazz;

    private AdapterBuilder() {

    }

    public static<T,V extends ViewHolder<T>> AdapterBuilder<T,V> of(Class<? extends ViewHolderFactory<T,V >> clazz) {
        AdapterBuilder<T,V> builder = new AdapterBuilder<>();
        builder.mFactoryClazz = clazz;
        return builder;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public AdapterBuilder<T,V> setClickListener(ViewHolderFactory.OnItemClickListener<T> clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public AdapterBuilder<T,V> setLongClickListener(ViewHolderFactory.OnItemLongClickListener<T> longClickListener) {
        this.longClickListener = longClickListener;
        return this;
    }

    public AdapterBuilder<T,V> setItemsComparator(ViewHolderFactory.ItemsComparator<T> itemsComparator) {
        this.itemsComparator = itemsComparator;
        return this;
    }

    public AdapterBuilder<T,V> setContentComparator(ViewHolderFactory.ContentComparator<T> contentComparator) {
        this.contentComparator = contentComparator;
        return this;
    }


    public ViewHolderFactory<T,V> build() {
        ViewHolderFactory<T,V> viewHolderFactory;
        try {
            viewHolderFactory = mFactoryClazz.newInstance();
            viewHolderFactory.build(this);
        } catch (IllegalAccessException|InstantiationException e) {
            throw new RuntimeException(e);
        }
        return viewHolderFactory;
    }

    public SingleAdapter<T,V> get(Context context) {
        return build().get(context);
    }
}
