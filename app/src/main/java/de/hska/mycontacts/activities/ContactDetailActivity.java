package de.hska.mycontacts.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import de.hska.mycontacts.R;
import de.hska.mycontacts.model.Contact;

public class ContactDetailActivity extends AppCompatActivity {

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
    }

    private void initContent() {
        imageView = (ImageView) findViewById(R.id.contactImage);
        Uri image = contact.getImage();
        if (image != null && new File(image.getPath()).exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(contact.getImage().getPath());
            imageView.setImageBitmap(myBitmap);
        }
        ((TextView) findViewById(R.id.debugText)).setText(contact.toString());
//        ((TextView) findViewById(R.id.mailTextView)).setText(contact.getMail());
//        ((TextView) findViewById(R.id.phoneTextView)).setText(contact.getPhone());
    }

    @Override
    public void onBackPressed() {
        Intent listIntent = new Intent(this, ContactListActivity.class);
        startActivity(listIntent);
    }
}
