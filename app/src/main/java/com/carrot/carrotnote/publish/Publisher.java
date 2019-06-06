package com.carrot.carrotnote.publish;

import com.carrot.carrotnote.MyApplication;
import com.carrot.carrotnote.model.Bill;
import com.carrot.carrotnote.ui.MainActivity;

import java.util.List;

public class Publisher {


    private Observer mObserver;

    public Publisher(Observer observer) {
        mObserver = observer;
    }

    public void show(List<Bill> bills) {
        MyApplication.runOnUiThread(() -> mObserver.notify(bills));
    }

    public void showError(String error) {
        MyApplication.runOnUiThread(() -> mObserver.notifyError(error));

    }

    public void showInfo(String info) {
        MyApplication.runOnUiThread(() -> mObserver.notifyInfo(info));

    }


    public interface Observer {

        void notifyError(String msg);

        void notifyInfo(String msg);

        void notify(List<Bill> bills);
    }
}
