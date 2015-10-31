package de.hska.mycontacts.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private static final int REQUEST_IMAGE_CHOOSE = 2;

    private ContactsDBHelper dbHelper;
    private Contact contact = new Contact();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        dbHelper = ContactsDBHelper.getInstance(this);

        Button cameraButton = (Button) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //captureImage();
                //chooseImage();
                //TODO Intent chooser
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });
    }

    private void saveContact() {
        Address address = new Address();
        address.setStreet(getStringValue(R.id.streetInput));
        address.setNumber(getStringValue(R.id.numberInput));
        address.setZipCode(getStringValue(R.id.zipcodeInput));
        address.setCity(getStringValue(R.id.cityInput));
        address.setCountry(getStringValue(R.id.countryInput));

        contact.setAddress(address);
        contact.setFirstName(getStringValue(R.id.firstnameInput));
        contact.setLastName(getStringValue(R.id.lastnameInput));
        contact.setPhone(getStringValue(R.id.phoneInput));
        contact.setMail(getStringValue(R.id.mailInput));

        InsertContactTask insertTask = new InsertContactTask(this);
        insertTask.execute(contact, dbHelper);
    }

    private String getStringValue(int id) {
        View field = findViewById(id);
        if(field instanceof EditText) {
            EditText textField = (EditText) field;
            return textField.getText().toString();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK) {
                    cropImage();
                }
                break;
            case REQUEST_IMAGE_CHOOSE:
                if(resultCode == RESULT_OK) {
                    contact.setImage(data.getData());
                    cropImage();
                    break;
                }
            case REQUEST_IMAGE_CROP:
                ImageView preview = (ImageView) findViewById(R.id.imagePreview);
                Bitmap tmp = data.getExtras().getParcelable("data");
                preview.setImageBitmap(tmp);
                break;
            default:
                super.onActivityResult(requestCode,resultCode, data);
        }
    }

    private void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private void chooseImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_IMAGE_CHOOSE);
    }

    private void captureImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                contact.setImage(Uri.fromFile(createImageFile()));
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (contact.getImage() != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contact.getImage());
                try {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                } catch(ActivityNotFoundException e) {
                    //display an error message
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void cropImage() {
        try {
            //throw new ActivityNotFoundException("test");
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(contact.getImage(), "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, contact.getImage());
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
        //contact = new Contact("Firstname_" + timestamp, "Lastname_"+timestamp, timestamp.toString(), timestamp+"@mail.de", address);

        InsertContactTask insertTask = new InsertContactTask(this);
        insertTask.execute(contact, dbHelper);
    }
}
