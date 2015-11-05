package de.hska.mycontacts.fragments;


import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

    public static ContactMapFragment newInstance(Contact contact) {
        ContactMapFragment fragment = new ContactMapFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    public ContactMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.contact = getArguments().getParcelable(ARGS_CONTACT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_map, container, false);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible && map == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (!isAddressInitialized()) {
            GeocodingTask geocodingTask = new GeocodingTask(getActivity());
            geocodingTask.execute(contact.getAddress());
        }
    }

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

    private String createSnippet(Address address) {
        List<String> lines = new ArrayList<>();
        for(int i = 0; i < address.getMaxAddressLineIndex() + 1; ++i) {
            lines.add(address.getAddressLine(i));
        }
        return TextUtils.join(", ", lines);
    }

    public Address getAddress() {
        return address;
    }

    public boolean isAddressInitialized() {
        return address != null && address.hasLatitude() && address.hasLongitude();
    }
}
