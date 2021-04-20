package websheba.kawcher.exportdataandroidtoexcelsheet;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static class StudentTable implements BaseColumns {

        public static final String TABLE_NAME = "student_table";
        public static final String _ID = "_id";
        public static final String EMAIL = "email";
        public static final String NAME = "name";
        public static final String PHONE_NO = "phone";


    }
}
