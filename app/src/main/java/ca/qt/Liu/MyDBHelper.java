package ca.qt.Liu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * this class made to generate a database for app
 */
public class MyDBHelper extends SQLiteOpenHelper {
    // Database name
    public static String DATABASE_NAME = "MyDatabase.db";

    // Current database version
    public static int DATABASE_VERSION = 1;

    // "_id" is the recommended primary key and first column.
    public static String SQL_CREATE = "CREATE TABLE booksTable (_id INTEGER PRIMARY KEY, title TEXT, author TEXT)";

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is first created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    // This is only called if the DATABASE_VERSION changes
    // Possible actions - delete table, then call onCreate
    // Use update statements to migrate previous version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
