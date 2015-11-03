package de.hska.mycontacts.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.hska.mycontacts.model.Contact;

/**
 * Created by larsbauer on 03.11.15.
 */
public class ContactFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Details", "Map"};
    private Context ctx;
    private Contact contact;

    public ContactFragmentPagerAdapter(FragmentManager fm, Context context, Contact contact) {
        super(fm);
        this.ctx = context;
        this.contact = contact;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return TestPageFragment.newInstance(position);
        } else {
            return ContactMapFragment.newInstance(contact);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
