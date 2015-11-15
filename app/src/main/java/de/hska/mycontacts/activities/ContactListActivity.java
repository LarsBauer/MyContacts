package de.hska.mycontacts.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import de.hska.mycontacts.R;
import de.hska.mycontacts.dao.ContactsDBHelper;
import de.hska.mycontacts.dao.DatabaseSchema.ContactEntry;
import de.hska.mycontacts.util.ContactMapper;

/**
 * Activity which contains the list of contacts
 */
public class ContactListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SQLITE_LOADER = 0;
    private static final String PARCEL_CONTACT = "de.hska.mycontacts.model.Contact";

    private SimpleCursorAdapter adapter;
    private ListView contactList;
    private SearchView searchField;
    private int backButtonCount = 0;

    /**
     * Used to initialize the layout and field of the Activity
     * @param savedInstanceState bundle with data for re-initialization
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initAdapter();
        getLoaderManager().initLoader(SQLITE_LOADER, null, this);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            /**
             * OnClickListener for the add button navigates to CreateContactAcitivy
             * @param v clicked View
             */
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(ContactListActivity.this, CreateContactActivity.class);
                startActivity(createIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);

        searchField = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Cursor result = ContactsDBHelper.getInstance(ContactListActivity.this).findContactsByName(query);
                adapter.changeCursor(result);
                if (result.getCount() == 0) {
                    Toast.makeText(ContactListActivity.this, "No contacts were found matching your query", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchField.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getLoaderManager().restartLoader(SQLITE_LOADER, null, ContactListActivity.this);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Used to initialize the CursorAdapter and layout of the ListView
     */
    private void initAdapter() {
        String[] fromColumns = {ContactEntry.COLUMN_NAME_LASTNAME, ContactEntry.COLUMN_NAME_FIRSTNAME};
        int[] toViews = {android.R.id.text1, android.R.id.text2};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null,fromColumns,toViews, 0);
        contactList = (ListView) findViewById(R.id.contactList);
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * OnItemClickListener for item in ListView
             * @param parent where the click happened
             * @param view clicked View
             * @param position clicked position
             * @param id row id of the clicked item
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SQLiteCursor cursor = (SQLiteCursor) parent.getItemAtPosition(position);
                Intent detailIntent = new Intent(ContactListActivity.this, ContactDetailActivity.class);
                detailIntent.putExtra(PARCEL_CONTACT, ContactMapper.map(cursor));
                startActivity(detailIntent);
            }
        });
        contactList.setAdapter(adapter);
    }

    /**
     * Instantiate a new Loader used to retrieve data from SQLite automatically
     * @param loaderID id of the loader
     * @param args additional arguments
     * @return new instance of Loader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        switch (loaderID) {
            case SQLITE_LOADER:
                final String sortOrder = ContactEntry.COLUMN_NAME_LASTNAME + " ASC";
                return new CursorLoader(this, null, null, null, null, sortOrder) {
                    /**
                     * Loads contacts from SQLite database
                     * @return cursor with database rows
                     */
                    @Override
                    public Cursor loadInBackground() {
                        return ContactsDBHelper.getInstance(ContactListActivity.this).findAllContacts();
                    }
                };
            default:
                return null;
        }
    }

    /**
     * Sets retrieved Cursor to SimpleCursorAdapter when database query is finished
     * @param loader finished Loader
     * @param data Cursor with data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data);
    }

    /**
     * Gets called when Loader gets reset
     * @param loader Loader to reset
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }

    /**
     * Gets called when user presses back button on device and is used to close the application
     */
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            backButtonCount = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press again to exit the application", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
