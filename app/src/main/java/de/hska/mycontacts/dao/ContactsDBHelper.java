package de.hska.mycontacts.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import de.hska.mycontacts.dao.DatabaseSchema.ContactEntry;
import de.hska.mycontacts.dao.DatabaseSchema.AddressEntry;
import de.hska.mycontacts.model.Address;

/**
 * Created by larsbauer on 26.10.15.
 */
public class ContactsDBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "Contacts.db";
    private static final String SQL_CREATE_TABLE_ADDRESS =
            "CREATE TABLE " + AddressEntry.TABLE_NAME + " (" +
                    AddressEntry._ID + " INTEGER PRIMARY KEY," +
                    AddressEntry.COLUMN_NAME_STREET + " TEXT," +
                    AddressEntry.COLUMN_NAME_NUMBER + " TEXT," +
                    AddressEntry.COLUMN_NAME_ZIPCODE + " TEXT," +
                    AddressEntry.COLUMN_NAME_CITY + " TEXT," +
                    AddressEntry.COLUMN_NAME_COUNTRY + " TEXT" +
            ");";
    private static final String SQL_CREATE_TABLE_CONTACT =
            "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                    ContactEntry._ID + " INTEGER PRIMARY KEY," +
                    ContactEntry.COLUMN_NAME_FIRSTNAME + " TEXT," +
                    ContactEntry.COLUMN_NAME_LASTNAME + " TEXT," +
                    ContactEntry.COLUMN_NAME_IMAGE_PATH + " TEXT," +
                    ContactEntry.COLUMN_NAME_PHONE + " TEXT," +
                    ContactEntry.COLUMN_NAME_MAIL + " TEXT," +
                    ContactEntry.COLUMN_ADDRESS_FK + " INTEGER," +
                    "FOREIGN KEY(" + ContactEntry.COLUMN_ADDRESS_FK + ") REFERENCES " + AddressEntry.TABLE_NAME + "(" + AddressEntry._ID + ")" +
            ")";
    private static final String SQL_DROP_TABLE_CONTACT = "DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME + ";";
    private static final String SQL_DROP_TABLE_ADDRESS = "DROP TABLE IF EXISTS " + AddressEntry.TABLE_NAME + ";";

    private static ContactsDBHelper instance = null;
    private Context ctx;

    private ContactsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;
    }

    public static ContactsDBHelper getInstance(Context ctx) {
        if(instance == null) {
            return new ContactsDBHelper(ctx.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_ADDRESS);
        db.execSQL(SQL_CREATE_TABLE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE_CONTACT);
        db.execSQL(SQL_DROP_TABLE_ADDRESS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
