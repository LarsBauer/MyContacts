package de.hska.mycontacts.tasks;

import android.os.AsyncTask;

import de.hska.mycontacts.model.Contact;

/**
 * AsyncTask to update existing Contact
 */
public class UpdateContactTask extends AsyncTask<Contact, Void, Integer> {

    /**
     * Runs on background thread and updates Contact in SQLite database
     * @param params the Contact which should be updated
     * @return number of the rows affected
     */
    @Override
    protected Integer doInBackground(Contact... params) {
        //TODO not implemented yet
        return null;
    }
}
