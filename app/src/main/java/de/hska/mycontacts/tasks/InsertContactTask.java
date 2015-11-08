package de.hska.mycontacts.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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
 * AsyncTask to insert new created Contact into SQLite database
 */
public class InsertContactTask extends AsyncTask<Contact, Void, Long> {

    private static final String PARCEL_CONTACT = "de.hska.mycontacts.model.Contact";

    private ProgressDialog dialog;
    private Context ctx;
    private Contact contact;

    /**
     * Constructor for InsertContactTask
     * @param context the context
     */
    public InsertContactTask(Context context) {
        this.ctx = context;
    }

    /**
     * Runs on UI thread before execution of AsyncTask and gets used to display a ProgressDialog
     */
    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(ctx, "", "Please wait...", true);
    }

    /**
     * Runs on background thread and inserts contact in database
     * @param params
     * @return row id of the inserted entry or -1 if an error occurred
     */
    @Override
    protected Long doInBackground(Contact... params) {
        contact = params[0];

        return ContactsDBHelper.getInstance(ctx).insertContact(contact);
    }

    /**
     * Runs on UI thread and is used to give user feedback and redirect to ContactsDetailActivity of newly created Contact
     * @param id row id of entry or error code
     */
    @Override
    protected void onPostExecute(Long id) {
        dialog.dismiss();
        if(id == -1) {
            Toast.makeText(ctx,"Could not add contact to database.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ctx,"Added contact to database.", Toast.LENGTH_SHORT).show();
            Intent detailIntent = new Intent(ctx, ContactDetailActivity.class);
            detailIntent.putExtra(PARCEL_CONTACT, contact);
            ctx.startActivity(detailIntent);
        }
    }
}
