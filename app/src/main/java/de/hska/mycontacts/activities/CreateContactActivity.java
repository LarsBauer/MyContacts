package de.hska.mycontacts.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hska.mycontacts.R;
import de.hska.mycontacts.dao.ContactsDBHelper;
import de.hska.mycontacts.model.Address;
import de.hska.mycontacts.model.Contact;
import de.hska.mycontacts.tasks.InsertContactTask;

public class CreateContactActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_IMAGE_CROP = 1;

    private ContactsDBHelper dbHelper;
    private Contact newContact = new Contact();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        dbHelper = ContactsDBHelper.getInstance(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK) {
                    cropImage();
                }
                break;
            case REQUEST_IMAGE_CROP:
                Bitmap tmp = data.getExtras().getParcelable("data");
                break;
            default:
                super.onActivityResult(requestCode,resultCode, data);
        }
    }

    private void captureImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                newContact.setImage(Uri.fromFile(createImageFile()));
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (newContact.getImage() != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, newContact.getImage());
                try {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                } catch(ActivityNotFoundException e) {
                    //display an error message
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void cropImage() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(newContact.getImage(), "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, newContact.getImage());
            startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
        } catch (ActivityNotFoundException e) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "CONTACT_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(!storageDir.exists()) {
            storageDir.mkdir();
        }

        return new File(storageDir, imageFileName + ".jpg");
    }

    private void insertTestContact() {
        //Long timestamp = System.currentTimeMillis() / 1000L;
        //Address address = new Address("Street_" + timestamp, timestamp%10+"", timestamp%100000+"", "City_"+timestamp, "Country_" + timestamp);
        //newContact = new Contact("Firstname_" + timestamp, "Lastname_"+timestamp, timestamp.toString(), timestamp+"@mail.de", address);

        InsertContactTask insertTask = new InsertContactTask(this);
        insertTask.execute(newContact, dbHelper);
    }
}
