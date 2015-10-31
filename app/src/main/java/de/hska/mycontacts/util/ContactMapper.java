package de.hska.mycontacts.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;

import java.io.File;

import de.hska.mycontacts.dao.DatabaseSchema;
import de.hska.mycontacts.model.Address;
import de.hska.mycontacts.model.Contact;

/**
 * Created by larsbauer on 28.10.15.
 */
public class ContactMapper {

    public static Contact map(Cursor cursor) {

        Contact contact = new Contact();
        contact.setFirstName(cursor.getString(cursor.getColumnIndex(DatabaseSchema.ContactEntry.COLUMN_NAME_FIRSTNAME)));
        contact.setLastName(cursor.getString(cursor.getColumnIndex(DatabaseSchema.ContactEntry.COLUMN_NAME_LASTNAME)));
        contact.setImage(Uri.parse(cursor.getString(cursor.getColumnIndex(DatabaseSchema.ContactEntry.COLUMN_NAME_IMAGE_PATH))));
        contact.setMail(cursor.getString(cursor.getColumnIndex(DatabaseSchema.ContactEntry.COLUMN_NAME_MAIL)));
        contact.setPhone(cursor.getString(cursor.getColumnIndex(DatabaseSchema.ContactEntry.COLUMN_NAME_PHONE)));
        contact.setAddress(AddressMapper.map(cursor));

        return contact;
    }
}
