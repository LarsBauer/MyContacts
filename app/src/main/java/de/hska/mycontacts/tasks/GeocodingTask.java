package de.hska.mycontacts.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.hska.mycontacts.R;
import de.hska.mycontacts.activities.ContactDetailActivity;
import de.hska.mycontacts.fragments.ContactMapFragment;
import de.hska.mycontacts.model.Address;

/**
 * Created by larsbauer on 03.11.15.
 */
public class GeocodingTask extends AsyncTask<Address, Void, List<android.location.Address>> {

    private Context ctx;
    private ProgressDialog dialog;
    private String errorMessage = "";

    public GeocodingTask(Context context) {
        ctx = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(ctx, "", "Please wait...", true);
    }

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

    private String getQueryString(Address address) {
        String query = "";

        query += address.getStreet() == null ? " " : address.getStreet() + " ";
        query += address.getNumber() == null ? " " : address.getNumber() + " ";
        query += address.getZipCode() == null ? " " : address.getZipCode() + " ";
        query += address.getCity() == null ? " " : address.getCity() + " ";
        query += address.getCountry() == null ? "" : address.getCity();

        return query;
    }

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
