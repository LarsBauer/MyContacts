package de.hska.mycontacts.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import de.hska.mycontacts.activities.ContactDetailActivity;
import de.hska.mycontacts.dao.ContactsDBHelper;
import de.hska.mycontacts.model.Contact;

import static de.hska.mycontacts.util.Constants.PARCEL_CONTACT;

/**
 * AsyncTask to update existing Contact
 */
public class UpdateContactTask extends AsyncTask<Contact, Void, Integer> {

    private ProgressDialog dialog;
    private Context ctx;
    private Contact contact;

    /**
     * Constructor for UpdateContactTask
     *
     * @param context the context
     */
    public UpdateContactTask(Context context) {
        ctx = context;
    }

    /**
     * Runs on UI thread before execution of AsyncTask and gets used to display a ProgressDialog
     */
    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(ctx, "", "Please wait...", true);
    }

    /**
     * Runs on background thread and updates Contact in SQLite database
     *
     * @param params the Contact which should be updated
     * @return number of the rows affected
     */
    @Override
    protected Integer doInBackground(Contact... params) {
        contact = params[0];

        return ContactsDBHelper.getInstance(ctx).updateContact(contact);
    }

    /**
     * Runs on UI thread and is used to give user feedback and redirect to ContactsDetailActivity of updated Contact
     *
     * @param affectedRows number of affected rows
     */
    @Override
    protected void onPostExecute(Integer affectedRows) {
        dialog.dismiss();
        if (affectedRows != 1) {
            Toast.makeText(ctx, "Could not update contact in database.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ctx, "Updated contact in database.", Toast.LENGTH_SHORT).show();
            Intent detailIntent = new Intent(ctx, ContactDetailActivity.class);
            detailIntent.putExtra(PARCEL_CONTACT, contact);
            ctx.startActivity(detailIntent);
        }
    }
}
