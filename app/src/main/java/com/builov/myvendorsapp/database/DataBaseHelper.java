package com.builov.myvendorsapp.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class DataBaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "customers.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase mDatabase;
    private Context mContext = null;
    private boolean mNeedUpdate = false;
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
else
        DB_PATH = "/data/data/" + context.getPackageName() +
                "/databases/";
        this.mContext = context;
        copyDataBase();
        this.getReadableDatabase();
    }
    //Проверка нужно ли обновить данные о БД
    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists()) dbFile.delete();
            copyDataBase();
            mNeedUpdate = false;
        }
    }
    //Проверка существования базы
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }
    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }
    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        OutputStream mOutput = new
                FileOutputStream(DB_PATH+DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength=mInput.read(mBuffer))>0)
            mOutput.write(mBuffer,0,mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }
    public boolean openDataBase() throws IOException{
        mDatabase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME,
                null,
                SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDatabase != null;
    }
    @Override
    public synchronized void close(){
        if (mDatabase !=null)
            mDatabase.close();
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db){
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion){
        if (newVersion>oldVersion)
            mNeedUpdate=true;
    }
}