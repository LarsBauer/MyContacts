package de.hska.mycontacts.util;

import android.database.Cursor;

import de.hska.mycontacts.dao.DatabaseSchema;
import de.hska.mycontacts.model.Address;

/**
 * Created by larsbauer on 30.10.15.
 */
public class AddressMapper {

    public static Address map(Cursor cursor) {
        Address address = new Address();
        address.setStreet(cursor.getString(cursor.getColumnIndex(DatabaseSchema.AddressEntry.COLUMN_NAME_STREET)));
        address.setNumber(cursor.getString(cursor.getColumnIndex(DatabaseSchema.AddressEntry.COLUMN_NAME_NUMBER)));
        address.setZipCode(cursor.getString(cursor.getColumnIndex(DatabaseSchema.AddressEntry.COLUMN_NAME_ZIPCODE)));
        address.setCity(cursor.getString(cursor.getColumnIndex(DatabaseSchema.AddressEntry.COLUMN_NAME_CITY)));
        address.setCountry(cursor.getString(cursor.getColumnIndex(DatabaseSchema.AddressEntry.COLUMN_NAME_COUNTRY)));

        return address;
    }

}
