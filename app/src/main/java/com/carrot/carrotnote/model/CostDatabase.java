package com.carrot.carrotnote.model;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Bill.class},version = 2)
public abstract class CostDatabase extends RoomDatabase {
    private static volatile CostDatabase INSTANCE;

    public static final String DATABASE_NAME = "Cost.db";

    public abstract BillDao getDao();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE bills "
                    + " ADD COLUMN bill_note TEXT");
            database.execSQL("UPDATE bills SET bill_note = bill_reason");
        }
    };

    public static CostDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (CostDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CostDatabase.class,DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
