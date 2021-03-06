package activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adapters.ListAdapter;
import data.PreferencesLayer;
import me.dstny.activities.R;
import util.Util;



public class PhoneNumbers extends Activity {

    private ListView listView;
    private ListAdapter listAdapter;
   public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_numbers);
        listView = (ListView) findViewById(R.id.listViewOfPhoneNumbers);
        listAdapter = new ListAdapter(this, Util.phoneNumbers);
        listView.setAdapter(listAdapter);
    }



    public void phoneNumbersBackButtonPressed(View view) {
        Intent intent=new Intent(PhoneNumbers.this,Settings.class);
        startActivity(intent);
        finish();
    }

    public void phoneNumberAddPressed(View view) {
        AlertDialog.Builder builder = new   AlertDialog.Builder(PhoneNumbers.this);
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Phone Number");
        alertDialog.setMessage("Enter a phone number for a good friend or family member.");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        alertDialog.setView(input);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        // code that checks to see if the email is valid or used by another account
                        // if it passes checks make it the new email and close alert dialog
                        String phoneNumber = input.getText().toString();
                        if (Util.phoneNumbers.contains(phoneNumber)) {
                            Toast.makeText(PhoneNumbers.this, "Already added phone number", Toast.LENGTH_SHORT).show();
                        } else if(phoneNumber.length()!=10) {
                            Toast.makeText(PhoneNumbers.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Util.phoneNumbers.add(phoneNumber);
                            PreferencesLayer.getInstance().setPhoneNumbers(Util.phoneNumbers);
                            listAdapter.notifyDataSetChanged();
                            listView.invalidateViews();
                        }

                    }
                });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                    }
                });
        alertDialog.show();
    }

    public void onBackPressed() {
        Intent intent = new Intent(PhoneNumbers.this, Settings.class );
        startActivity(intent);
        finish();
    }




}
