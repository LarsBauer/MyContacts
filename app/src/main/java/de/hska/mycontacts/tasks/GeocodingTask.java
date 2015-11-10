package de.hska.mycontacts.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hska.mycontacts.activities.ContactDetailActivity;
import de.hska.mycontacts.fragments.ContactMapFragment;
import de.hska.mycontacts.model.Address;

/**
 * Resolves Contact's Address by using Google's Geocoding API
 */
public class GeocodingTask extends AsyncTask<Address, Void, List<android.location.Address>> {

    private Context ctx;
    private ProgressDialog dialog;
    private String errorMessage = "";

    /**
     * Constructor for GeocodingTask
     *
     * @param context the context
     */
    public GeocodingTask(Context context) {
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
     * Runs on background thread and resolves given Address by using Google Geocoder
     *
     * @param params Address which should be resolved
     * @return list of Addresses including latitude and longitude or empty list of no matches were found
     */
    @Override
    protected List<android.location.Address> doInBackground(Address... params) {
        Address address = params[0];
        String query = getQueryString(address);
        List<android.location.Address> addresses = new ArrayList<>();

        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocationName(query, 1);
        } catch (IOException e) {
            errorMessage = "Connection to Geocoder API failed!";
        } catch (IllegalStateException e) {
            errorMessage = "Could not resolve given address!";
        }

        return addresses;
    }

    /**
     * Helper method to create query string from Address object
     *
     * @param address the Address which should be resolved
     * @return query string including all Address fields
     */
    private String getQueryString(Address address) {
        String query = "";

        query += address.getStreet() == null ? " " : address.getStreet() + " ";
        query += address.getNumber() == null ? " " : address.getNumber() + " ";
        query += address.getZipCode() == null ? " " : address.getZipCode() + " ";
        query += address.getCity() == null ? " " : address.getCity() + " ";
        query += address.getCountry() == null ? "" : address.getCountry();

        return query;
    }

    /**
     * Runs on UI thread and is used to give user feedback and set found Address to ContactMapFragment
     *
     * @param addresses
     */
    @Override
    protected void onPostExecute(List<android.location.Address> addresses) {
        if (addresses.isEmpty()) {
            Toast.makeText(ctx, errorMessage, Toast.LENGTH_SHORT);
            addresses.add(new android.location.Address(Locale.getDefault()));
        }
        ContactDetailActivity activity = (ContactDetailActivity) ctx;
        ContactMapFragment fragment = (ContactMapFragment) activity.getCurrentFragment();
        fragment.setAddress(addresses.get(0));
        dialog.dismiss();
    }
}
