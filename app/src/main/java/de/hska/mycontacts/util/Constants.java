package de.hska.mycontacts.util;

/**
 * Created by larsbauer on 10.11.15.
 */
public class Constants {

    //Parcel keys
    public static final String PARCEL_CONTACT = "de.hska.mycontacts.model.Contact";

    //Activity request codes
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_IMAGE_CROP = 1;
    public static final int REQUEST_IMAGE_CHOOSE = 2;

    //Dialog
    public static final String DIALOG_TITLE = "Add photo!";
    public static final String DIALOG_CAPTURE_IMAGE = "Take picture";
    public static final String DIALOG_CHOOSE_IMAGE = "Choose from gallery";
    public static final CharSequence[] DIALOG_OPTIONS = {DIALOG_CAPTURE_IMAGE, DIALOG_CHOOSE_IMAGE, "Cancel"};

}
