package de.hska.mycontacts.tasks;

import android.app.ProgressDialog;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.hska.mycontacts.fragments.ContactMapFragment;
import de.hska.mycontacts.model.Address;

/**
 * Created by larsbauer on 03.11.15.
 */
public class GeocodingTask extends AsyncTask<Address, Void, List<android.location.Address>> {

    private Fragment parent;
    private ProgressDialog dialog;

    public GeocodingTask(Fragment fragment) {
        parent = fragment;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(parent.getContext(), "", "Please wait...", true);
    }

    @Override
    protected List<android.location.Address> doInBackground(Address... params) {
        Address address = params[0];
        String query = getQueryString(address);
        List<android.location.Address> addresses;

        Geocoder geocoder = new Geocoder(parent.getContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocationName(query, 1);
        } catch (IOException e) {
            Toast.makeText(parent.getContext(), "Could not resolve given address!", Toast.LENGTH_SHORT).show();
            return Collections.emptyList();
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
        if(parent instanceof ContactMapFragment) {
            ContactMapFragment fragment = (ContactMapFragment) parent;
            fragment.initMap(addresses.get(0));
            dialog.dismiss();
        }
    }
}
