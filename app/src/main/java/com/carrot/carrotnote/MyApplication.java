package com.carrot.carrotnote;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;


import org.greenrobot.eventbus.EventBus;
import org.jaaksi.pickerview.picker.BasePicker;
import org.jaaksi.pickerview.topbar.ITopBar;
import org.jaaksi.pickerview.util.Util;
import org.jaaksi.pickerview.widget.DefaultCenterDecoration;
import org.jaaksi.pickerview.widget.PickerView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application{

    public static Context mContext;
    public static Handler mHandler;
    public static Application mApplication;
    private static ExecutorService sPoolExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mHandler = new Handler();
        mApplication = this;
        sPoolExecutor = Executors.newCachedThreadPool();
        initEventBus();
        initDefaultPicker();
    }

    private void initDefaultPicker() {
        // 利用修改静态默认属性值，快速定制一套满足自己app样式需求的Picker.
        // BasePickerView
        PickerView.sDefaultItemSize = 50;
        PickerView.sDefaultIsCirculation = true;

        // PickerView
        PickerView.sOutTextSize = 18;
        PickerView.sOutColor = Color.GRAY;
        DefaultCenterDecoration.sDefaultLineColor = Color.RED;

//        // BasePicker
//        int padding = Util.dip2px(this, 20);
//        BasePicker.sDefaultPaddingRect = new Rect(padding, padding, padding, padding);
//        BasePicker.sDefaultPickerBackgroundColor = Color.WHITE;
//        // 自定义 TopBar
//        BasePicker.sDefaultTopBarCreator = new BasePicker.IDefaultTopBarCreator() {
//            @Override public ITopBar createDefaultTopBar(LinearLayout parent) {
//                return new CustomTopBar(parent);
//            }
//        };

//        // DefaultCenterDecoration
//        DefaultCenterDecoration.sDefaultLineWidth = 1;
//        DefaultCenterDecoration.sDefaultLineColor = Color.RED;
//        //DefaultCenterDecoration.sDefaultDrawable = new ColorDrawable(Color.WHITE);
//        int leftMargin = Util.dip2px(this, 10);
//        int topMargin = Util.dip2px(this, 2);
//        DefaultCenterDecoration.sDefaultMarginRect =
//                new Rect(leftMargin, -topMargin, leftMargin, -topMargin);
    }

    private void initEventBus() {
        EventBus.builder()
                .logSubscriberExceptions(false)
                .sendNoSubscriberEvent(false)
                .throwSubscriberException(true)
                .installDefaultEventBus();
    }

    public static Context getContext() {
        return mContext;
    }

    public static Application getApplication() {
        return mApplication;
    }

    public static void runOnNewThread(Runnable runnable) {
        sPoolExecutor.execute(runnable);
    }

    public static void runOnUiThread(Runnable runnable, long delayMillis) {
        mMainHandle.postDelayed(runnable, delayMillis);
    }

    public static BillRepository getRepository() {
        return BillRepository.get(mApplication);
    }

    public static void runOnUiThread(Runnable runnable) {
        runOnUiThread(runnable, 0);
    }


    private static Handler mMainHandle = new Handler(Looper.getMainLooper());
}
