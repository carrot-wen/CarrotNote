package com.carrot.carrotnote.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carrot.carrotnote.IPresenter;
import com.carrot.carrotnote.MainPresenter;
import com.carrot.carrotnote.R;
import com.carrot.carrotnote.ui.adapter.AdapterBuilder;
import com.carrot.carrotnote.ui.adapter.BillViewHolder;
import com.carrot.carrotnote.ui.adapter.SingleAdapter;
import com.carrot.carrotnote.compile.Compiler;
import com.carrot.carrotnote.model.Bill;
import com.carrot.carrotnote.publish.Publisher;
import com.carrot.carrotnote.util.LogUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private IPresenter mIPresenter;
    private RecyclerView mRecyclerView;
    private SingleAdapter<Bill, BillViewHolder> mAdapter;
    private Activity mActivity;


    public static MainFragment getInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mIPresenter = new MainPresenter(new Publisher(new Publisher.Observer() {
            @Override
            public void notifyError(String msg) {
                LogUtil.d(TAG, "notifyError:  " + msg);
            }

            @Override
            public void notifyInfo(String msg) {
                LogUtil.d(TAG, "notifyInfo:  " + msg);
            }

            @Override
            public void notify(List<Bill> bills) {
                print(bills);
                mAdapter.setData(bills);
            }
        }));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = root.findViewById(R.id.bill_list);
        initRecyclerView();
        initButton(root);
        LogUtil.d(TAG, "onCreateView:  ");
        mIPresenter.load();


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    private void initButton(View root) {
        FloatingActionButton addButton = root.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> startDetailActivity(new Bill()));
    }


    private void initRecyclerView() {
        mAdapter = AdapterBuilder.of(BillViewHolder.Factory.class)
                .setItemsComparator((a, b) -> a.getId() == b.getId())
                .setClickListener(this::startDetailActivity)
                .get(mActivity);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    private void startDetailActivity(Bill bill) {
        Intent intent = new Intent(mActivity, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_BILL, bill);
        mActivity.startActivity(intent);
    }

    private void load() {
        mIPresenter.exec(Compiler.build("get", null));
    }


    private void print(List<Bill> bills) {
        LogUtil.d(TAG, "print:  " + bills.size());
        for (Bill bill : bills) {
            LogUtil.d(TAG, "print: bill " + bill.toString());
        }
    }


}
