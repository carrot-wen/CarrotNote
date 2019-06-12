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

public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";

    private IPresenter mIPresenter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SingleAdapter<Bill, BillViewHolder> mAdapter;
    private Activity mActivity;


    private FloatingActionButton mAddButton;


    public static ListFragment getInstance() {
        return new ListFragment();
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
                bills.sort((o1, o2) -> (int) (o2.getTime() - o1.getTime()));
                print(bills);
                mAdapter.setData(bills);
            }
        }));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = root.findViewById(R.id.bill_list);
        initButton(root);
        initRecyclerView();
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
        mAddButton = root.findViewById(R.id.add_button);
        mAddButton.setOnClickListener(v -> startDetailActivity(new Bill()));
    }


    private void initRecyclerView() {
        mAdapter = AdapterBuilder.of(BillViewHolder.Factory.class)
                .setItemsComparator((a, b) -> a.getId() == b.getId())
                .setClickListener(this::startDetailActivity)
                .get(mActivity);

        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
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


    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {


        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

            boolean hasCovered = covered();
            if (hasCovered && mAddButton.getVisibility() == View.VISIBLE) {
                mAddButton.setVisibility(View.GONE);
            } else if (!hasCovered && mAddButton.getVisibility() == View.GONE) {
                mAddButton.setVisibility(View.VISIBLE);
            }
        }


        /**
         * Determine if the button obscures the last item
         */
        private boolean covered() {
            return !mRecyclerView.canScrollVertically(1) && mRecyclerView.canScrollVertically(-1);
        }

    };


}
