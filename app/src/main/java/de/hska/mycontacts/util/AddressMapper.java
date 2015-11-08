package de.hska.mycontacts.util;

import android.database.Cursor;

import de.hska.mycontacts.dao.DatabaseSchema.AddressEntry;
import de.hska.mycontacts.model.Address;

/**
 * Maps current index of a Cursor to Address object
 */
public class AddressMapper {

    /**
     * Map Cursor to Address
     * @param cursor the cursor
     * @return mapped Address
     */
    public static Address map(Cursor cursor) {
        Address address = new Address();
        address.setId(cursor.getLong(cursor.getColumnIndex(AddressEntry._ID)));
        address.setStreet(cursor.getString(cursor.getColumnIndex(AddressEntry.COLUMN_NAME_STREET)));
        address.setNumber(cursor.getString(cursor.getColumnIndex(AddressEntry.COLUMN_NAME_NUMBER)));
        address.setZipCode(cursor.getString(cursor.getColumnIndex(AddressEntry.COLUMN_NAME_ZIPCODE)));
        address.setCity(cursor.getString(cursor.getColumnIndex(AddressEntry.COLUMN_NAME_CITY)));
        address.setCountry(cursor.getString(cursor.getColumnIndex(AddressEntry.COLUMN_NAME_COUNTRY)));

        return address;
    }

}
