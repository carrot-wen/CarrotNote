package com.carrot.carrotnote.model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BillDao {


    @Query("SELECT * FROM bills")
    List<Bill> getAll();

    @Insert
    void inert(Bill... result);

    @Update
    void update(Bill bill);

    @Delete
    int delete(Bill... results);

    @Query("DELETE FROM bills WHERE bill_id IN (:ids)")
    int delete(int... ids);

}
