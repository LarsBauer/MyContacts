package de.hska.mycontacts.fragments;


import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import de.hska.mycontacts.R;
import de.hska.mycontacts.model.Contact;
import de.hska.mycontacts.tasks.GeocodingTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARGS_CONTACT = "de.hska.mycontacts.model.Contact";

    private GoogleMap map;
    private Address address;
    private Contact contact;

    /**
     * Creates new instance of ContactMapFragment
     *
     * @param contact Contact whose Address should be displayed
     * @return a new instance of fragment ContactMapFragment
     */
    public static ContactMapFragment newInstance(Contact contact) {
        ContactMapFragment fragment = new ContactMapFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Default constructor for ContactMapFragment used by factory method
     */
    public ContactMapFragment() {
        // Required empty public constructor
    }

    /**
     * Used to initialize Contact of the Fragment
     *
     * @param savedInstanceState bundle with data for re-initialization
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.contact = getArguments().getParcelable(ARGS_CONTACT);
        }
    }

    /**
     * Used to inflate the defined layout of the Fragment
     *
     * @param inflater           layout inflater
     * @param container          container of the fragment
     * @param savedInstanceState bundle with data for re-initialization
     * @return inflated View for Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_map, container, false);
    }

    /**
     * Used to determine whether ContactMapFragment is visible at the moment to initialize Address
     *
     * @param visible true if visible
     */
    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible && map == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);
        }
    }


    /**
     * Callback when async requested Google Map is ready
     *
     * @param googleMap the retrieved map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (!isAddressInitialized()) {
            GeocodingTask geocodingTask = new GeocodingTask(getActivity());
            geocodingTask.execute(contact.getAddress());
        }
    }

    /**
     * Gets called by GeocodingTask and draws Marker on map when Address is valid
     *
     * @param result
     */
    public void setAddress(Address result) {
        this.address = result;
        if (isAddressInitialized()) {
            LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(contact.getFirstName() + " " + contact.getLastName())
                    .snippet(createSnippet(address)));
            marker.showInfoWindow();
        } else {
            Toast.makeText(getContext(), "Whoops - could not resolve address!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Helper method to create a snippet for Marker's info window
     *
     * @param address the Address to display
     * @return address lines in 1 line
     */
    private String createSnippet(Address address) {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < address.getMaxAddressLineIndex() + 1; ++i) {
            lines.add(address.getAddressLine(i));
        }
        return TextUtils.join(", ", lines);
    }

    /**
     * Helper method to check whether retrieved Address is valid
     *
     * @return true if not null and containing latitude and longitude
     */
    public boolean isAddressInitialized() {
        return address != null && address.hasLatitude() && address.hasLongitude();
    }
}
