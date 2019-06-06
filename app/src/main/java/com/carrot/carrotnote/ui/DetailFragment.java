package com.carrot.carrotnote.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.carrot.carrotnote.MyApplication;
import com.carrot.carrotnote.R;
import com.carrot.carrotnote.model.Bill;
import com.carrot.carrotnote.util.KeyboardUtils;
import com.carrot.carrotnote.util.LogUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.jaaksi.pickerview.picker.BasePicker;
import org.jaaksi.pickerview.picker.TimePicker;
import org.jaaksi.pickerview.widget.PickerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DetailFragment extends Fragment implements TimePicker.OnTimeSelectListener {
    private static final String TAG = "DetailFragment";
    private final static String EXTRA_BILL = "extra_bill";
    private final static String DATE_TO_STRING_DETAIL_PATTERN = "yyyy-MM-dd HH:mm";
    private final static long TEN_YEAR = 10L * 365 * 24 * 60 * 60 * 1000L;

    private final static int UNCHANGED = 0;
    private final static int QUALIFIED = 1;
    private final static int UNQUALIFIED = 2;

    private AppCompatActivity mActivity;
    private Bill mBill;
    private EditText mCount;
    private TextView mTime;
    private EditText mReason;
    private EditText mNote;
    private Bill mTemp;
    private TextView mFocus;

    private SimpleDateFormat mDateFormat;
    private TimePicker mTimePicker;


    public static DetailFragment getInstance(Bill bill) {
        DetailFragment instance = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_BILL, bill);
        instance.setArguments(bundle);
        return instance;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
        mActivity.getOnBackPressedDispatcher().addCallback(mCallback);
        mDateFormat = new SimpleDateFormat(DATE_TO_STRING_DETAIL_PATTERN, Locale.getDefault());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        FloatingActionButton saveButton = root.findViewById(R.id.save_button);
        createPicker();
        saveButton.setOnClickListener(v -> {
            mActivity.onBackPressed();
        });

        if (getArguments() == null) {
            throw new RuntimeException("Why not bill?");
        }
        mBill = getArguments().getParcelable(EXTRA_BILL);
        mTemp = mBill.copy();

        LogUtil.d(TAG, "onCreateView: getBIll " + mBill);
        initView(root);
        bindView();
        return root;
    }

    private void initView(View root) {
        mCount = root.findViewById(R.id.detail_count);
        setListenerAndUnFocus(mCount);
        mTime = root.findViewById(R.id.detail_time);
        //setListenerAndUnFocus(mTime);
        mReason = root.findViewById(R.id.detail_reason);
        setListenerAndUnFocus(mReason);
        mNote = root.findViewById(R.id.detail_note);
        setListenerAndUnFocus(mNote);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListenerAndUnFocus(EditText editText) {
//        editText.setOnTouchListener(mTouchListener);
//        outFocus(editText);
    }


    private void bindView() {
        mCount.setText(Double.compare(mBill.getNumber(), 0) != 0 ? String.valueOf(mBill.getNumber()) : "");
        mTime.setText(mDateFormat.format(mBill.getTime()));
        mTime.setOnClickListener(v -> mTimePicker.show());
        mReason.setText(mBill.getReason());
        mNote.setText(mBill.getNote());

    }


    private int checkBill() {

        String numberVal = mCount.getText().toString().trim();
        mTemp.setReason(mReason.getText().toString().trim());
        mTemp.setNote(mNote.getText().toString().trim());
        if (!TextUtils.isEmpty(numberVal)) {
            mTemp.setNumber(Double.valueOf(numberVal));
        } else {
            mTemp.setNumber(0);
        }

        LogUtil.d(TAG, "checkBill:  " + mTemp.getId() + "   " + mBill.getId());
        LogUtil.d(TAG, "checkBill:  " + mTemp.getTime() + "   " + mBill.getTime());
        LogUtil.d(TAG, "checkBill:  " + mTemp.getNumber() + "   " + mBill.getNumber());
        LogUtil.d(TAG, "checkBill:  " + mTemp.getReason() + "   " + mBill.getReason());
        LogUtil.d(TAG, "checkBill:  " + mTemp.getNote() + "   " + mBill.getNote());

        if (mTemp.equals(mBill)) {
            LogUtil.d(TAG, "checkBill: unchanged ");
            return UNCHANGED;
        } else {
            LogUtil.d(TAG, "checkBill: not equals then ");
        }

        if (mTemp.getTime() > System.currentTimeMillis()) {
            return UNQUALIFIED;
        } else if (TextUtils.isEmpty(mTemp.getReason())) {
            return UNQUALIFIED;
        } else if (mTemp.getNumber() == 0) {
            return UNQUALIFIED;
        }

        LogUtil.d(TAG, "checkBill: retrun true ");
        return QUALIFIED;

    }

    private void saveBill() {
        LogUtil.d(TAG, "saveBill: update " + mTemp.getId());
        MyApplication.getRepository().updateOrAdd(mTemp);
    }

    private OnBackPressedCallback mCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            switch (checkBill()) {
                case QUALIFIED:
                    saveBill();
                case UNCHANGED:
                    setEnabled(false);
                    mActivity.onBackPressed();
                    break;
                case UNQUALIFIED:
                    showDialog();
                    break;
            }
        }
    };


    @Override
    public void onStop() {
        super.onStop();
        KeyboardUtils.hideSoftInput(mActivity);
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            LogUtil.d(TAG, "onTouch:  " + v.getId() + "    " + event.getActionMasked());
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (v.getId()) {
                    case R.id.detail_count:
//                    case R.id.detail_time:
                    case R.id.detail_reason:
                    case R.id.detail_note:
                        inFocus(v);
                        return true;
                    default:
                        outFocus(mFocus);
                }
            }
            return false;
        }
    };


    private void outFocus(TextView view) {
        Handler handler = new Handler();
        handler.removeCallbacksAndMessages(null);
        if (view != null) {
            view.setFocusableInTouchMode(false);
            view.clearFocus();
            view.setGravity(Gravity.CENTER);

            KeyboardUtils.hideSoftInput(mActivity);
        }
    }

    private void inFocus(View view) {
        outFocus(mFocus);
        mFocus = (TextView) view;
        mFocus.setFocusableInTouchMode(true);
        mFocus.requestFocus();
        mFocus.setGravity(Gravity.START);
        KeyboardUtils.showSoftInput(mActivity);
    }

    private void showDialog() {
        // TODO: 2019/5/27 show dialog

        Toast.makeText(mActivity, "tik info", Toast.LENGTH_LONG).show();
    }

    private void createPicker() {
        long now = System.currentTimeMillis();
        mTimePicker = new TimePicker.Builder(mActivity, TimePicker.TYPE_ALL, this)
                .setRangDate(now - TEN_YEAR, now + TEN_YEAR)
                .setSelectedDate(now)
                // 设置 Formatter
                .setFormatter(new TimePicker.DefaultFormatter() {
                    // 自定义Formatter显示去年，今年，明年
                    @Override
                    public CharSequence format(TimePicker picker, int type, int position, long num) {
                        if (type == TimePicker.TYPE_YEAR) {
                            return num + "年";
                        } else if (type == TimePicker.TYPE_MONTH) {
                            return String.format("%d月", num);
                        }

                        return super.format(picker, type, position, num);
                    }
                }).create();
    }


    @Override
    public void onTimeSelect(TimePicker picker, Date date) {
        LogUtil.d(TAG, "onTimeSelect:  " + mDateFormat.format(date));
        mTime.setText(mDateFormat.format(date));
        mTemp.setTime(date.getTime());
    }
}
