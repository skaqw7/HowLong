package com.example.namsoo.myslidingmenu;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by namsoo on 2015-08-04.
 */
public class DbManager {
    int MAX_INDEX = 35;

    String TAG_ERROR = "ERROR MESSAGE";
    String TAG_DEBUG = "LOG DEBUG";
    String PACKAGE_NAME = "com.example.namsoo.myslidingmenu";
    String databaseName = "vds_07_28.db";
    String tableName = "vds_07_28_down";
    String tableName2 = "vds_07_28_up";
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    Context mContext;

    int time;
    int index[] = new int[MAX_INDEX];
    String vds[] = new String[MAX_INDEX];//고정
    double x[] = new double[MAX_INDEX];//고정
    double y[] = new double[MAX_INDEX];//고정
    int length[] = new int[MAX_INDEX];//고정
    double speed[] = new double[MAX_INDEX];


    public DbManager(Context ctx, int temp) {

        if (temp == 2) {
            tableName = tableName2;
        }

        mContext = ctx;
        dumpDB(mContext);
        openDb();

        initialization();//초기화
        synchronization();//동기화

//        Toast.makeText(mContext, "DB 셋팅 완료 \t 시간 : " + getTime(), Toast.LENGTH_LONG).show();
    }


    public void initialization() {
        time = getTime();
        getX();
        getY();
        getLength();
        getVds();
    }

    public void synchronization() {
        getIndex();
        getSpeed();
    }


    public void getVds() {
        try {
            if (db != null) {
                Cursor cursor = db.rawQuery("SELECT vds FROM " + tableName + " WHERE time=" + getTime(), null);
                cursor.moveToFirst();

                int count = cursor.getCount();

                try {
                    for (int i = 0; i < count; i++) {
                        vds[i] = cursor.getString(0);
                        cursor.moveToNext();
                    }
                } catch (Exception e) {
                    cursor.close();
                }
                cursor.close();
            } else {
                Log.e(TAG_ERROR, "DataBase is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG_ERROR, e.getMessage());

        }
    }

    public void getLength() {
        try {
            if (db != null) {
                Cursor cursor = db.rawQuery("SELECT length FROM " + tableName + " WHERE time=" + getTime(), null);
                cursor.moveToFirst();

                int count = cursor.getCount();

                try {
                    for (int i = 0; i < count; i++) {
                        length[i] = cursor.getInt(0);
                        cursor.moveToNext();
                    }
                } catch (Exception e) {
                    cursor.close();
                }
                cursor.close();
            } else {
                Log.e(TAG_ERROR, "DataBase is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG_ERROR, e.getMessage());
        }
    }


    public void getY() {
        try {
            if (db != null) {
                Cursor cursor = db.rawQuery("SELECT y FROM " + tableName + " WHERE time=" + getTime(), null);
                cursor.moveToFirst();

                int count = cursor.getCount();

                try {
                    for (int i = 0; i < count; i++) {
                        y[i] = cursor.getDouble(0);
                        cursor.moveToNext();
                    }
                } catch (Exception e) {
                    cursor.close();
                }
                cursor.close();
            } else {
                Log.e(TAG_ERROR, "DataBase is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG_ERROR, e.getMessage());
        }
    }


    public void getX() {
        try {
            if (db != null) {
                Cursor cursor = db.rawQuery("SELECT x FROM " + tableName + " WHERE time=" + getTime(), null);
                cursor.moveToFirst();

                int count = cursor.getCount();

                try {
                    for (int i = 0; i < count; i++) {
                        x[i] = cursor.getDouble(0);
                        cursor.moveToNext();
                    }
                } catch (Exception e) {
                    cursor.close();
                }
                cursor.close();
            } else {
                Log.e(TAG_ERROR, "DataBase is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG_ERROR, e.getMessage());
        }
    }


    public void getSpeed() {
        try {
            if (db != null) {
                Cursor cursor = db.rawQuery("SELECT speed FROM " + tableName + " WHERE time=" + getTime(), null);
                cursor.moveToFirst();

                int count = cursor.getCount();

                try {
                    for (int i = 0; i < count; i++) {
                        speed[i] = cursor.getDouble(0);
                        cursor.moveToNext();
                    }
                } catch (Exception e) {
                    cursor.close();
                }
                cursor.close();
            } else {
                Log.e(TAG_ERROR, "DataBase is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG_ERROR, e.getMessage());
        }
    }


    public void getIndex() {
        try {
            if (db != null) {
                Cursor cursor = db.rawQuery("SELECT index_ FROM " + tableName + " WHERE time=" + getTime(), null);
                cursor.moveToFirst();

                int count = cursor.getCount();
                try {
                    for (int i = 0; i < count; i++) {
                        index[i] = cursor.getInt(0);
                        cursor.moveToNext();
                    }
                } catch (Exception e) {
                    cursor.close();
                }
                cursor.close();
            } else {
                Log.e(TAG_ERROR, "DataBase is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG_ERROR, e.getMessage());
        }
    }


    public int getTime() {

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        if (minute % 5 != 0) {
            minute = minute + 5 - minute % 5;
        }

        if (minute == 60) {
            return hour * 100 + 100;
        } else {
            return hour * 100 + minute;
        }
//        return 1040;
    }


    public void openDb() {
        try {
            databaseHelper = new DatabaseHelper(mContext, databaseName, null, 1);
            db = databaseHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG_ERROR, e.getMessage() + "데이터베이스를 열지 못했습니다.");
        }

    }


    public void dumpDB(Context mContext) {
        AssetManager manager = mContext.getAssets();
        String folderPath = "/data/data/" + PACKAGE_NAME + "/databases";
        String filePath = "/data/data/" + PACKAGE_NAME + "/databases/" + databaseName;

        File folder = new File(folderPath);
        File file = new File(filePath);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            InputStream is = manager.open(databaseName);
            BufferedInputStream bis = new BufferedInputStream(is);

            if (folder.exists()) {
            } else {
                folder.mkdirs();
            }

            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }

            bos.flush();
            bos.close();
            fos.close();
            bis.close();
            is.close();

        } catch (IOException e) {
            Log.e(TAG_ERROR, e.getMessage());
        }
    }


    public boolean checkDB(Context mContext) {

        String filePath = "/data/data/" + PACKAGE_NAME + "/databases/vds.db";
        File file = new File(filePath);
        return file.exists();
    }


    // 존재하는 table 확인
    public void table() {
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while (true) {
                if (!c.moveToNext())
                    break;
            }
        }
    }


    class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            //    Toast.makeText(getApplicationContext(), "Helper 의 onOpen 호출됨", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //  Toast.makeText(getApplicationContext(), "Helper 의 onCreate 호출됨", Toast.LENGTH_LONG).show();
            //  creatTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //  Toast.makeText(getApplicationContext(), "Helper 의 onUpgrade 호출됨 "+oldVersion+"->"+newVersion, Toast.LENGTH_LONG).show();
            //  changeTable(db);
        }
    }


}
