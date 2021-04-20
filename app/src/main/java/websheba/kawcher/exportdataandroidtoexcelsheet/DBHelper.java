package websheba.kawcher.exportdataandroidtoexcelsheet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student.db";
    private static final int DATABASE_VERSION = 1;



    private SQLiteDatabase db;
    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;



        final String SQL_CREATE_STUDENT_TABLE = "CREATE TABLE " +
                DatabaseContract.StudentTable.TABLE_NAME + " ( " +
                DatabaseContract.StudentTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.StudentTable.NAME + " TEXT, " +
                DatabaseContract.StudentTable.EMAIL + " TEXT,  " +
                DatabaseContract.StudentTable.PHONE_NO + " TEXT  " +
                ")";


        db.execSQL(SQL_CREATE_STUDENT_TABLE);



    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.StudentTable.TABLE_NAME);

        onCreate(db);
    }





    public long insertUser(UserModel model){
        db=getWritableDatabase();

        ContentValues cv=new ContentValues();

        cv.put( DatabaseContract.StudentTable.NAME,model.getName());
        cv.put( DatabaseContract.StudentTable.PHONE_NO,model.getPhoneNo());
        cv.put( DatabaseContract.StudentTable.EMAIL,model.getEmail());


        long id=db.insert(DatabaseContract.StudentTable.TABLE_NAME,null,cv);

        return id;
    }

    public ArrayList<UserModel> getAllLocalUser(){
        db=getReadableDatabase();
        ArrayList<UserModel>list=new ArrayList<>();
        Cursor cursor=db.rawQuery("SELECT * FROM " +DatabaseContract.StudentTable.TABLE_NAME,null);

        if(cursor.moveToFirst()){
            do {


                String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.StudentTable.NAME));
                String email = cursor.getString(cursor.getColumnIndex(DatabaseContract.StudentTable.EMAIL));
                String phoneNo = cursor.getString(cursor.getColumnIndex(DatabaseContract.StudentTable.PHONE_NO));

                list.add(new UserModel(name,phoneNo,email));
            } while (cursor.moveToNext());
        }

        return list;
    }




}
