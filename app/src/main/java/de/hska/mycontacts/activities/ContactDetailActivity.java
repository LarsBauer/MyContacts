package de.hska.mycontacts.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import de.hska.mycontacts.R;
import de.hska.mycontacts.fragments.ContactFragmentPagerAdapter;
import de.hska.mycontacts.model.Contact;

public class ContactDetailActivity extends FragmentActivity {

    private static final String PARCEL_CONTACT = "de.hska.mycontacts.model.Contact";

    private Contact contact;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        contact = getIntent().getParcelableExtra(PARCEL_CONTACT);
        Log.d("ContactDetailActivity", contact.toString());
        initContent();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ContactFragmentPagerAdapter(getSupportFragmentManager(), this, contact));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initContent() {
        imageView = (ImageView) findViewById(R.id.contactImage);
        Uri image = contact.getImage();
        if (image != null && new File(image.getPath()).exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(contact.getImage().getPath());
            imageView.setImageBitmap(myBitmap);
        }
        ((TextView) findViewById(R.id.contactFirstname)).setText(contact.getFirstName());
        ((TextView) findViewById(R.id.contactLastname)).setText(contact.getLastName());

    }

    @Override
    public void onBackPressed() {
        Intent listIntent = new Intent(this, ContactListActivity.class);
        startActivity(listIntent);
    }
}
