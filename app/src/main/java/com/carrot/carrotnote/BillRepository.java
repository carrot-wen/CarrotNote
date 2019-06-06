package com.carrot.carrotnote;


import android.app.Application;
import android.content.Context;

import com.carrot.carrotnote.model.Bill;
import com.carrot.carrotnote.model.BillDao;
import com.carrot.carrotnote.model.CostDatabase;

import java.util.List;

public class BillRepository {
    private static final String TAG = "BillRepository";
    private static BillRepository INSTANCE;

    private BillDao mBillDao;

    private BillRepository(Context context) {
        CostDatabase database = CostDatabase.getInstance(context);
        mBillDao = database.getDao();
    }

    public static BillRepository get(Application application) {
        if (INSTANCE == null) {
            synchronized (BillRepository.class){
                if (INSTANCE == null) {
                    INSTANCE = new BillRepository(application);
                }
            }
        }
        return INSTANCE;
    }


    public void add(Bill... bills) {
       MyApplication.runOnNewThread(()-> mBillDao.inert(bills));

    }


    public void getBills(Callback<List<Bill>> callable) {
        MyApplication.runOnNewThread(()-> callable.call(mBillDao.getAll()));
    }


    public void updateOrAdd(Bill bill) {
        if (bill.getId() == 0) {
            add(bill);
        } else {
            update(bill);
        }
    }


    public void update(Bill bill) {
        MyApplication.runOnNewThread(()->mBillDao.update(bill));
    }

    public void delete(Bill bill) {
        MyApplication.runOnNewThread(()->mBillDao.delete(bill));
    }

    public void delete(int id) {
        MyApplication.runOnNewThread(()->mBillDao.delete(id));
    }

    public interface Callback<T> {
        void call(T t);
    }

}
