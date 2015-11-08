package de.hska.mycontacts.util;

import android.database.Cursor;
import android.net.Uri;

import de.hska.mycontacts.dao.DatabaseSchema.ContactEntry;
import de.hska.mycontacts.model.Contact;

/**
 * Maps current index of a Cursor to Contact object
 */
public class ContactMapper {

    /**
     * Map Cursor to Contact
     * @param cursor the cursor
     * @return mapped Contact
     */
    public static Contact map(Cursor cursor) {

        Contact contact = new Contact();
        contact.setId(cursor.getLong(cursor.getColumnIndex(ContactEntry._ID)));
        contact.setFirstName(cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_FIRSTNAME)));
        contact.setLastName(cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_LASTNAME)));
        contact.setImage(Uri.parse(cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_IMAGE_PATH))));
        contact.setMail(cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_MAIL)));
        contact.setPhone(cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NAME_PHONE)));
        contact.setAddress(AddressMapper.map(cursor));

        return contact;
    }
}
