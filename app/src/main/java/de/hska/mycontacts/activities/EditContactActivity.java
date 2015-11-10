package de.hska.mycontacts.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hska.mycontacts.R;
import de.hska.mycontacts.model.Address;
import de.hska.mycontacts.model.Contact;
import de.hska.mycontacts.tasks.UpdateContactTask;
import de.hska.mycontacts.util.Constants;

import static de.hska.mycontacts.util.Constants.DIALOG_CAPTURE_IMAGE;
import static de.hska.mycontacts.util.Constants.DIALOG_CHOOSE_IMAGE;
import static de.hska.mycontacts.util.Constants.DIALOG_IMAGE_OPTIONS;
import static de.hska.mycontacts.util.Constants.DIALOG_IMAGE_TITLE;
import static de.hska.mycontacts.util.Constants.PARCEL_CONTACT;
import static de.hska.mycontacts.util.Constants.REQUEST_IMAGE_CAPTURE;
import static de.hska.mycontacts.util.Constants.REQUEST_IMAGE_CHOOSE;
import static de.hska.mycontacts.util.Constants.REQUEST_IMAGE_CROP;

/**
 * Activity to edit Contacts and save changes to SQLite database
 */
public class EditContactActivity extends AppCompatActivity {

    private Contact updatedContact;
    private Contact contact;
    private AlertDialog.Builder builder;

    /**
     * Used to initialize the layout and field of the Activity
     *
     * @param savedInstanceState bundle with data for re-initialization
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        contact = getIntent().getParcelableExtra(Constants.PARCEL_CONTACT);
        updatedContact = contact;
        initContent();

        builder = new AlertDialog.Builder(this);

        Button cameraButton = (Button) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            /**
             * OnClickListener for camera button opens AlertDialog for user selection
             * @param v clicked View
             */
            @Override
            public void onClick(View v) {
                builder.setTitle(DIALOG_IMAGE_TITLE);
                builder.setItems(DIALOG_IMAGE_OPTIONS, new DialogInterface.OnClickListener() {
                    /**
                     * OnClickListener for dialog options to let user decide whether to open camera or choose existing image
                     * @param dialog clicked Dialog
                     * @param which position of the clicked option
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected = (String) DIALOG_IMAGE_OPTIONS[which];
                        switch (selected) {
                            case DIALOG_CAPTURE_IMAGE:
                                captureImage();
                                break;
                            case DIALOG_CHOOSE_IMAGE:
                                chooseImage();
                                break;
                            default:
                                dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

    }

    /**
     * Used to inflate the Activity's specific menu
     *
     * @param menu the Menu
     * @return whether to show menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_contact, menu);
        return true;
    }

    /**
     * Gets called when item from menu gets selected
     *
     * @param item the selected menu item
     * @return true if event was handled successfully
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                updateContact();
                return true;
            case R.id.action_cancel:
                Intent detailIntent = new Intent(this, ContactDetailActivity.class);
                detailIntent.putExtra(PARCEL_CONTACT, contact);
                startActivity(detailIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initializes EditText views
     */
    private void initContent() {
        ((EditText) findViewById(R.id.firstnameEdit)).setText(updatedContact.getFirstName());
        ((EditText) findViewById(R.id.lastnameEdit)).setText(updatedContact.getLastName());
        Uri image = updatedContact.getImage();
        if (image != null && new File(image.getPath()).exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(updatedContact.getImage().getPath());
            ((ImageView) findViewById(R.id.imagePreview)).setImageBitmap(myBitmap);
        }
        ((EditText) findViewById(R.id.phoneEdit)).setText(updatedContact.getPhone());
        ((EditText) findViewById(R.id.mailEdit)).setText(updatedContact.getMail());
        Address address = updatedContact.getAddress();
        ((EditText) findViewById(R.id.streetEdit)).setText(address.getStreet());
        ((EditText) findViewById(R.id.numberEdit)).setText(address.getNumber());
        ((EditText) findViewById(R.id.zipcodeEdit)).setText(address.getZipCode());
        ((EditText) findViewById(R.id.cityEdit)).setText(address.getCity());
        ((EditText) findViewById(R.id.countryEdit)).setText(address.getCountry());
    }

    /**
     * Retrieves Contact and Address field from UI and starts AsyncTask for database update
     */
    private void updateContact() {
        Address address = updatedContact.getAddress();
        address.setStreet(getStringValue(R.id.streetEdit));
        address.setNumber(getStringValue(R.id.numberEdit));
        address.setZipCode(getStringValue(R.id.zipcodeEdit));
        address.setCity(getStringValue(R.id.cityEdit));
        address.setCountry(getStringValue(R.id.countryEdit));

        updatedContact.setAddress(address);
        updatedContact.setFirstName(getStringValue(R.id.firstnameEdit));
        updatedContact.setLastName(getStringValue(R.id.lastnameEdit));
        updatedContact.setPhone(getStringValue(R.id.phoneEdit));
        updatedContact.setMail(getStringValue(R.id.mailEdit));

        UpdateContactTask updateTask = new UpdateContactTask(this);
        updateTask.execute(updatedContact);
    }

    /**
     * Helper method to extract String values from input fields
     *
     * @param id id of the EditText
     * @return text value of EditText
     */
    private String getStringValue(int id) {
        View field = findViewById(id);
        if (field instanceof EditText) {
            EditText textField = (EditText) field;
            return textField.getText().toString();
        }
        return "";
    }

    /**
     * Starts Intent to choose existing image from gallery
     */
    private void chooseImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_IMAGE_CHOOSE);
    }

    /**
     * Starts Intent to open camera and take picture
     */
    private void captureImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (isIntentSupported(cameraIntent)) {
            try {
                updatedContact.setImage(Uri.fromFile(createImageFile()));
            } catch (IOException ex) {
                Toast.makeText(this, "Whoops - could not access external storage!", Toast.LENGTH_SHORT).show();
            }
            if (updatedContact.getImage() != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, updatedContact.getImage());
                try {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Whoops - something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Whoops - your device doesn't support capturing images!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Starts (unofficial) Intent to crop chosen image which is likely to fail
     */
    private void cropImage() {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(updatedContact.getImage(), "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 500);
        cropIntent.putExtra("outputY", 500);
        cropIntent.putExtra("return-data", true);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, updatedContact.getImage());
        try {
            startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Whoops - your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates File for picture in external file directory
     *
     * @return new File in App's private file storage
     * @throws IOException if access to external storage fails
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "CONTACT_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        return new File(storageDir, imageFileName + ".jpg");
    }

    /**
     * Helper method to check if a Intent is supported by device
     *
     * @param intent Intent to be checked
     * @return true if Intent is safe to use
     */
    private boolean isIntentSupported(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        return !activities.isEmpty();
    }

}
