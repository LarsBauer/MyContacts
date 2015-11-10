package de.hska.mycontacts.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.hska.mycontacts.model.Contact;

/**
 * //TODO lookup exact usage and function
 */
public class ContactFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"Details", "Map"};
    private Context ctx;
    private Contact contact;

    /**
     * Constructor for ContactFragmentPagerAdapter
     *
     * @param fm      fragment manager
     * @param context the context
     * @param contact the Contact to pass to ContactMapFragment
     */
    public ContactFragmentPagerAdapter(FragmentManager fm, Context context, Contact contact) {
        super(fm);
        this.ctx = context;
        this.contact = contact;
    }

    /**
     * Used to retrieve the number of views available
     *
     * @return number of views in adapter
     */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    /**
     * Creates new Fragment for position
     *
     * @param position the position
     * @return new instance of the Fragment specified in position
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ContactDetailFragment.newInstance(contact);
        } else {
            return ContactMapFragment.newInstance(contact);
        }
    }

    /**
     * Used to retrieve the title of a specified Fragment
     *
     * @param position position of the Fragment
     * @return the title
     */
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
