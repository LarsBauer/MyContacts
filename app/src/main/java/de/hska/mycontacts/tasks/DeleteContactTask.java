package de.hska.mycontacts.tasks;

import android.os.AsyncTask;

import de.hska.mycontacts.model.Contact;

/**
 * AsyncTask to delete Contact from database
 */
public class DeleteContactTask extends AsyncTask<Contact, Void, Integer> {

    /**
     * Runs on background thread and deletes provided Contact from database
     * @param params the Contact
     * @return number of rows affected
     */
    @Override
    protected Integer doInBackground(Contact... params) {
        //TODO not implemented yet
        return null;
    }
}
