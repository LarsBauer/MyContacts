package de.hska.mycontacts.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;

import de.hska.mycontacts.R;
import de.hska.mycontacts.dao.ContactsDBHelper;
import de.hska.mycontacts.model.Address;
import de.hska.mycontacts.model.Contact;
import de.hska.mycontacts.tasks.InsertContactTask;

/**
 * Activity used to create new contacts
 */
public class CreateContactActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_IMAGE_CROP = 1;
    private static final int REQUEST_IMAGE_CHOOSE = 2;

    private static final String DIALOG_TITLE = "Add photo!";
    private static final String DIALOG_CAPTURE_IMAGE = "Take picture";
    private static final String DIALOG_CHOOSE_IMAGE = "Choose from gallery";
    private static final CharSequence[] DIALOG_OPTIONS = {DIALOG_CAPTURE_IMAGE, DIALOG_CHOOSE_IMAGE, "Cancel"};

    private ContactsDBHelper dbHelper;
    private Contact contact = new Contact();
    private AlertDialog.Builder builder;

    /**
     * Used to initialize the layout and field of the Activity
     * @param savedInstanceState bundle with data for re-initialization
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        dbHelper = ContactsDBHelper.getInstance(this);
        builder = new AlertDialog.Builder(this);

        Button cameraButton = (Button) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            /**
             * OnClickListener for camera button opens AlertDialog for user selection
             * @param v clicked View
             */
            @Override
            public void onClick(View v) {
                builder.setTitle(DIALOG_TITLE);
                builder.setItems(DIALOG_OPTIONS, new DialogInterface.OnClickListener() {
                    /**
                     * OnClickListener for dialog options to let user decide whether to open camera or choose existing image
                     * @param dialog clicked Dialog
                     * @param which position of the clicked option
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected = (String) DIALOG_OPTIONS[which];
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

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            /**
             * OnClickListener for save button triggers insert into database
             * @param v clicked View
             */
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });
    }

    /**
     * Creates new Contact and Address and starts AsyncTask for database insert
     */
    private void saveContact() {
        Address address = new Address();
        address.setStreet(getStringValue(R.id.streetInput));
        address.setNumber(getStringValue(R.id.contactNumber));
        address.setZipCode(getStringValue(R.id.contactZipcode));
        address.setCity(getStringValue(R.id.contactCity));
        address.setCountry(getStringValue(R.id.countryInput));

        contact.setAddress(address);
        contact.setFirstName(getStringValue(R.id.firstnameInput));
        contact.setLastName(getStringValue(R.id.lastnameInput));
        contact.setPhone(getStringValue(R.id.phoneInput));
        contact.setMail(getStringValue(R.id.mailInput));

        InsertContactTask insertTask = new InsertContactTask(this);
        insertTask.execute(contact, dbHelper);
    }

    /**
     * Helper method to extract String values from input fields
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
     * Callback for startActivityForResult gets used to process result of intents
     * @param requestCode request code of the started intent
     * @param resultCode status code to determine whether intent was successful
     * @param data returned data of the intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    cropImage();
                    break;
                case REQUEST_IMAGE_CHOOSE:
                    contact.setImage(data.getData());
                    cropImage();
                    break;
                case REQUEST_IMAGE_CROP:
                    ImageView preview = (ImageView) findViewById(R.id.imagePreview);
                    Bitmap tmp = data.getExtras().getParcelable("data");
                    preview.setImageBitmap(tmp);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            Toast.makeText(this, "Whoops - something went wrong!", Toast.LENGTH_SHORT).show();
        }
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
                contact.setImage(Uri.fromFile(createImageFile()));
            } catch (IOException ex) {
                Toast.makeText(this, "Whoops - could not access external storage!", Toast.LENGTH_SHORT).show();
            }
            if (contact.getImage() != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contact.getImage());
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
        cropIntent.setDataAndType(contact.getImage(), "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("return-data", true);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, contact.getImage());
        try {
            startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Whoops - your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates File for picture in external file directory
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
     * @param intent Intent to be checked
     * @return true if Intent is safe to use
     */
    private boolean isIntentSupported(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        return !activities.isEmpty();
    }

    /**
     * Helper method to copy chosen image from gallery into App's private file storage
     * @param src source File
     * @param dst destination File
     * @throws IOException if copy process fails
     */
    //TODO copy image to app media store
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
}
