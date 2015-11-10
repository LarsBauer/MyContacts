package de.hska.mycontacts.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import de.hska.mycontacts.activities.ContactDetailActivity;
import de.hska.mycontacts.dao.ContactsDBHelper;
import de.hska.mycontacts.model.Contact;

import static de.hska.mycontacts.util.Constants.*;

/**
 * AsyncTask to insert new created Contact into SQLite database
 */
public class InsertContactTask extends AsyncTask<Contact, Void, Contact> {

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
    protected Contact doInBackground(Contact... params) {
        contact = params[0];

        return ContactsDBHelper.getInstance(ctx).insertContact(contact);
    }

    /**
     * Runs on UI thread and is used to give user feedback and redirect to ContactsDetailActivity of newly created Contact
     * @param updatedContact updated Contact or null if an error occurred
     */
    @Override
    protected void onPostExecute(Contact updatedContact) {
        dialog.dismiss();
        if(updatedContact == null) {
            Toast.makeText(ctx,"Could not add contact to database.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ctx,"Added contact to database.", Toast.LENGTH_SHORT).show();
            Intent detailIntent = new Intent(ctx, ContactDetailActivity.class);
            detailIntent.putExtra(PARCEL_CONTACT, updatedContact);
            ctx.startActivity(detailIntent);
        }
    }
}
