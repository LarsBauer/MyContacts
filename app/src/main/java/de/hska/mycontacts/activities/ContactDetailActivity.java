package de.hska.mycontacts.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import de.hska.mycontacts.R;
import de.hska.mycontacts.fragments.ContactFragmentPagerAdapter;
import de.hska.mycontacts.model.Contact;

/**
 * Activity to display contact details
 */
public class ContactDetailActivity extends FragmentActivity {

    private static final String PARCEL_CONTACT = "de.hska.mycontacts.model.Contact";

    private Contact contact;
    private ImageView imageView;
    private ViewPager pager;

    /**
     * Used to initialize the layout and field of the Activity
     * @param savedInstanceState bundle with data for re-initialization
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        contact = getIntent().getParcelableExtra(PARCEL_CONTACT);
        initHeaderContent();

        pager = (ViewPager) findViewById(R.id.viewPager);
        ContactFragmentPagerAdapter adapter = new ContactFragmentPagerAdapter(getSupportFragmentManager(), this, contact);
        pager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    /**
     * Initializes contact image and name
     */
    private void initHeaderContent() {
        imageView = (ImageView) findViewById(R.id.contactImage);
        Uri image = contact.getImage();
        if (image != null && new File(image.getPath()).exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(contact.getImage().getPath());
            imageView.setImageBitmap(myBitmap);
        }
        ((TextView) findViewById(R.id.contactFirstname)).setText(contact.getFirstName());
        ((TextView) findViewById(R.id.contactLastname)).setText(contact.getLastName());

    }

    /**
     * Gets triggered when user presses the back button of the device
     */
    @Override
    public void onBackPressed() {
        Intent listIntent = new Intent(this, ContactListActivity.class);
        startActivity(listIntent);
    }

    /**
     * Retrieves active Fragment from ViewPager
     * @return current visible tab Fragment
     */
    public Fragment getCurrentFragment() {
        Fragment result = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + pager.getCurrentItem());
        return result;
    }
}
