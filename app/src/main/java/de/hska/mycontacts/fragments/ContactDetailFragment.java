package de.hska.mycontacts.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hska.mycontacts.R;
import de.hska.mycontacts.model.Contact;

/**
 * Fragment to display the details of selected Contact
 */
public class ContactDetailFragment extends Fragment {

    private static final String ARGS_CONTACT = "de.hska.mycontacts.model.Contact";

    private Contact contact;


    /**
     * Creates new instance of ContactDetailFragment
     * @param contact Contact whose details should be displayed
     * @return A new instance of fragment ContactDetailFragment.
     */
    public static ContactDetailFragment newInstance(Contact contact) {
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Default constructor for ContactDetailFragment used by factory method
     */
    public ContactDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Used to initialize Contact of the Fragment
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
     * @param inflater layout inflater
     * @param container container of the fragment
     * @param savedInstanceState bundle with data for re-initialization
     * @return inflated View for Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_detail, container, false);
    }

    /**
     * Called when View was created and used to initialize dynamic fragment content
     * @param view the created View
     * @param savedInstanceState bundle with data for re-initialization
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        initDetails();
    }

    /**
     * Initializes the text of TextViews with given Contact
     */
    private void initDetails() {
        View view = getView();
        ((TextView) view.findViewById(R.id.contactPhone)).setText(contact.getPhone());
        ((TextView) view.findViewById(R.id.contactMail)).setText(contact.getMail());

        de.hska.mycontacts.model.Address address = contact.getAddress();
        if(!address.getStreet().isEmpty() && !address.getNumber().isEmpty()) {
            ((TextView) view.findViewById(R.id.contactStreet)).setText(address.getStreet() + " " + address.getNumber());
        }
        if(!address.getZipCode().isEmpty() && !address.getCity().isEmpty()){
            ((TextView) view.findViewById(R.id.contactCity)).setText(address.getZipCode()  + " " +address.getCity());
        }
        ((TextView) view.findViewById(R.id.contactCountry)).setText(address.getCountry());
    }

}
