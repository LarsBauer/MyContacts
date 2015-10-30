package de.hska.mycontacts.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;

import de.hska.mycontacts.activities.ContactListActivity;
import de.hska.mycontacts.dao.DatabaseSchema.AddressEntry;
import de.hska.mycontacts.dao.DatabaseSchema.ContactEntry;
import de.hska.mycontacts.dao.ContactsDBHelper;
import de.hska.mycontacts.model.Address;
import de.hska.mycontacts.model.Contact;

/**
 * Created by larsbauer on 27.10.15.
 */
public class InsertContactTask extends AsyncTask<Object, Void, Long> {

    private ProgressDialog dialog;
    private Activity ctx;

    public InsertContactTask(Activity context) {
        this.ctx = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(ctx, "", "Please wait...", true);
    }

    @Override
    protected Long doInBackground(Object... params) {
        Contact contact = (Contact) params[0];
        Address address = contact.getAddress();

        ContactsDBHelper dbHelper = (ContactsDBHelper) params[1];
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues addressValues = new ContentValues();
        addressValues.put(AddressEntry.COLUMN_NAME_STREET, address.getStreet());
        addressValues.put(AddressEntry.COLUMN_NAME_NUMBER, address.getNumber());
        addressValues.put(AddressEntry.COLUMN_NAME_ZIPCODE, address.getZipCode());
        addressValues.put(AddressEntry.COLUMN_NAME_CITY, address.getCity());
        addressValues.put(AddressEntry.COLUMN_NAME_COUNTRY, address.getCountry());

        long addressId = db.insert(AddressEntry.TABLE_NAME, null, addressValues);

        ContentValues contactValues = new ContentValues();
        contactValues.put(ContactEntry.COLUMN_NAME_FIRSTNAME, contact.getFirstName());
        contactValues.put(ContactEntry.COLUMN_NAME_LASTNAME, contact.getLastName());
        Uri image = contact.getImage();
        if(image != null && new File(image.getPath()).exists()) {
            contactValues.put(ContactEntry.COLUMN_NAME_IMAGE_PATH, contact.getImage().getPath());
        }
        contactValues.put(ContactEntry.COLUMN_NAME_MAIL, contact.getMail());
        contactValues.put(ContactEntry.COLUMN_NAME_PHONE, contact.getPhone());
        contactValues.put(ContactEntry.COLUMN_ADDRESS_FK, addressId);

        long id = db.insert(ContactEntry.TABLE_NAME, null, contactValues);
        if(id == -1) {
            Toast.makeText(ctx,"Could not add contact to database.", Toast.LENGTH_SHORT).show();
        }
        return id;
    }

    @Override
    protected void onPostExecute(Long id) {
        //ContactListActivity activity = (ContactListActivity) ctx;
        //activity.notifyDataChanges();
        dialog.dismiss();
    }
}
