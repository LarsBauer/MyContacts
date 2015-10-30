package de.hska.mycontacts.dao;

import android.provider.BaseColumns;

/**
 * Created by larsbauer on 26.10.15.
 */
public final class DatabaseSchema {

    private DatabaseSchema() {

    }

    public static abstract class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME_FIRSTNAME = "first_name";
        public static final String COLUMN_NAME_LASTNAME = "last_name";
        public static final String COLUMN_NAME_IMAGE_PATH = "image_path";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_MAIL = "mail";
        public static final String COLUMN_ADDRESS_FK = "address_fk";
    }

    public static abstract class AddressEntry implements BaseColumns {
        public static final String TABLE_NAME = "address";
        public static final String COLUMN_NAME_STREET = "street";
        public static final String COLUMN_NAME_NUMBER = "number";
        public static final String COLUMN_NAME_ZIPCODE = "zip_code";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_COUNTRY = "country";
    }
}
