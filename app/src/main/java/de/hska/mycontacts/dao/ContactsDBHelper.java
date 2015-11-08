package de.hska.mycontacts.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import de.hska.mycontacts.dao.DatabaseSchema.ContactEntry;
import de.hska.mycontacts.dao.DatabaseSchema.AddressEntry;
import de.hska.mycontacts.model.Address;
import de.hska.mycontacts.model.Contact;

/**
 * Helper class to create SQLite database, table schemas and insert values
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

    /**
     * Private constructor for ContactsDBHelper to prevent instantiation
     * @param context context
     */
    private ContactsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;
    }

    /**
     * Using singleton pattern to retrieve instance of database helper
     * @param ctx context
     * @return new instance of ContactsDBHelper or existing one
     */
    public static ContactsDBHelper getInstance(Context ctx) {
        if(instance == null) {
            return new ContactsDBHelper(ctx.getApplicationContext());
        }
        return instance;
    }

    /**
     * Used by Loader to retrieve all Contacts from database
     * @return Cursor with all rows
     */
    public Cursor findAllContacts() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String sortOrder = ContactEntry.COLUMN_NAME_LASTNAME + " ASC";
        qb.setTables(ContactEntry.TABLE_NAME + " JOIN " + AddressEntry.TABLE_NAME + " ON " + ContactEntry.TABLE_NAME + "." + ContactEntry.COLUMN_ADDRESS_FK + "=" + AddressEntry.TABLE_NAME + "." + AddressEntry._ID);
        return qb.query(getReadableDatabase(), null,null, null, null, null, sortOrder);
    }

    /**
     * Inserts Contact into SQLite database
     * @param contact the Contact which should be persisted
     * @return row id of created entry or -1 if an error occurred
     */
    public long insertContact(Contact contact) {

        long addressId = insertAddress(contact.getAddress());
        if(addressId == -1) {
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(ContactEntry.COLUMN_NAME_FIRSTNAME, contact.getFirstName());
        values.put(ContactEntry.COLUMN_NAME_LASTNAME, contact.getLastName());
        Uri image = contact.getImage();
        if(image == null) {
            contact.setImage(Uri.parse(""));
        }
        values.put(ContactEntry.COLUMN_NAME_IMAGE_PATH, contact.getImage().getPath());
        values.put(ContactEntry.COLUMN_NAME_MAIL, contact.getMail());
        values.put(ContactEntry.COLUMN_NAME_PHONE, contact.getPhone());
        values.put(ContactEntry.COLUMN_ADDRESS_FK, addressId);

        return getWritableDatabase().insert(ContactEntry.TABLE_NAME, null, values);
    }

    /**
     * Inserts Address into SQLite database
     * @param address the Adress which should be persisted
     * @return row id of the created entry or -1 if an error occurred
     */
    private long insertAddress(Address address) {
        ContentValues values = new ContentValues();
        values.put(AddressEntry.COLUMN_NAME_STREET, address.getStreet());
        values.put(AddressEntry.COLUMN_NAME_NUMBER, address.getNumber());
        values.put(AddressEntry.COLUMN_NAME_ZIPCODE, address.getZipCode());
        values.put(AddressEntry.COLUMN_NAME_CITY, address.getCity());
        values.put(AddressEntry.COLUMN_NAME_COUNTRY, address.getCountry());

        return getWritableDatabase().insert(AddressEntry.TABLE_NAME, null, values);
    }

    /**
     * Updates given Contact in SQLite database
     * @param contact the Contact which should be updated
     * @return number of rows affected
     */
    public int updateContact(Contact contact) {
        String whereClause = ContactEntry._ID + " = ?";
        String[] whereArgs = {String.valueOf(contact.getId())};

        int affected = updateAddress(contact.getAddress());
        if(affected == 0) {
            //TODO error handling
            return affected;
        }

        //TODO not implemented yet
        ContentValues values = new ContentValues();

        return getWritableDatabase().update(ContactEntry.TABLE_NAME, values, whereClause, whereArgs);
    }

    /**
     * Updates given Address in SQLite database
     * @param address the Address which should be updated
     * @return number of rows affected
     */
    private int updateAddress(Address address) {
        String whereClause = AddressEntry._ID + " = ?";
        String[] whereArgs = {String.valueOf(address.getId())};

        //TODO not implemented yet
        ContentValues values = new ContentValues();

        return getWritableDatabase().update(AddressEntry.TABLE_NAME, values, whereClause, whereArgs);
    }

    /**
     * Deletes given Contact from SQLite database
     * @param contact the Contact which should be deleted
     * @return number of rows affected
     */
    public int deleteContact(Contact contact) {
        String whereClause = ContactEntry._ID + " = ?";
        String[] whereArgs = {String.valueOf(contact.getId())};

        int affected = deleteAddress(contact.getAddress());
        if(affected == 0) {
            //TODO error handling
            return affected;
        }

        return getWritableDatabase().delete(ContactEntry.TABLE_NAME, whereClause, whereArgs);
    }

    /**
     * Deletes given Address from SQLite database
     * @param address the Address which should be deleted
     * @return number of rows affected
     */
    private int deleteAddress(Address address) {
        String whereClause = AddressEntry._ID + " = ?";
        String[] whereArgs = {String.valueOf(address.getId())};

        return getWritableDatabase().delete(AddressEntry.TABLE_NAME, whereClause, whereArgs);
    }

    /**
     * Called when database gets created for first time and used to create tables
     * @param db the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_ADDRESS);
        db.execSQL(SQL_CREATE_TABLE_CONTACT);
    }

    /**
     * Called when database version gets upgraded or tables get changed
     * @param db the database
     * @param oldVersion old database version number
     * @param newVersion new database version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE_CONTACT);
        db.execSQL(SQL_DROP_TABLE_ADDRESS);
        onCreate(db);
    }

    /**
     * Called when database version gets downgraded
     * @param db the database
     * @param oldVersion old database version number
     * @param newVersion new database version number
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
