package de.hska.mycontacts.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;

import de.hska.mycontacts.activities.ContactDetailActivity;
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

    private static final String PARCEL_CONTACT = "de.hska.mycontacts.model.Contact";

    private ProgressDialog dialog;
    private Activity ctx;
    private Contact contact;

    public InsertContactTask(Activity context) {
        this.ctx = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(ctx, "", "Please wait...", true);
    }

    @Override
    protected Long doInBackground(Object... params) {
        contact = (Contact) params[0];
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
        if(image == null) {
            contact.setImage(Uri.parse(""));
        }
        contactValues.put(ContactEntry.COLUMN_NAME_IMAGE_PATH, contact.getImage().getPath());
        contactValues.put(ContactEntry.COLUMN_NAME_MAIL, contact.getMail());
        contactValues.put(ContactEntry.COLUMN_NAME_PHONE, contact.getPhone());
        contactValues.put(ContactEntry.COLUMN_ADDRESS_FK, addressId);

        return db.insert(ContactEntry.TABLE_NAME, null, contactValues);
    }

    @Override
    protected void onPostExecute(Long id) {
        dialog.dismiss();
        if(id == -1) {
            Toast.makeText(ctx.getBaseContext(),"Could not add contact to database.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ctx.getBaseContext(),"Added contact to database.", Toast.LENGTH_SHORT).show();
            Intent detailIntent = new Intent(ctx, ContactDetailActivity.class);
            detailIntent.putExtra(PARCEL_CONTACT, contact);
            ctx.startActivity(detailIntent);
        }
    }
}
