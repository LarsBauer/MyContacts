package de.hska.mycontacts.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import de.hska.mycontacts.activities.ContactDetailActivity;
import de.hska.mycontacts.activities.ContactListActivity;
import de.hska.mycontacts.dao.ContactsDBHelper;
import de.hska.mycontacts.model.Contact;

import static de.hska.mycontacts.util.Constants.PARCEL_CONTACT;

/**
 * AsyncTask to delete Contact from database
 */
public class DeleteContactTask extends AsyncTask<Contact, Void, Integer> {

    private Context ctx;
    private ProgressDialog dialog;
    private Contact contact;

    /**
     * Constructor for InsertContactTask
     * @param context the context
     */
    public DeleteContactTask(Context context) {
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
     * Runs on background thread and deletes provided Contact from database
     * @param params the Contact
     * @return number of rows affected
     */
    @Override
    protected Integer doInBackground(Contact... params) {
        contact = params[0];
        return ContactsDBHelper.getInstance(ctx).deleteContact(contact);
    }

    /**
     * Runs on UI thread and is used to give user feedback and redirect to ContactsListActivity after deletion
     * @param affectedRows number of affected rows
     */
    @Override
    protected void onPostExecute(Integer affectedRows) {
        dialog.dismiss();
        if(affectedRows != 1) {
            Toast.makeText(ctx, "Could not delete contact from database.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ctx,"Deleted contact from database.", Toast.LENGTH_SHORT).show();
            Intent listIntent = new Intent(ctx, ContactListActivity.class);
            ctx.startActivity(listIntent);
        }
    }
}
