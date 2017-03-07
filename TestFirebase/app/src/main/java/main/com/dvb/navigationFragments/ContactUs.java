package main.com.dvb.navigationFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import main.com.dvb.Dashboard_main;
import main.com.dvb.R;

/**
 * Created by AIA on 11/6/16.
 */

public class ContactUs extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact_us, container, false);

//        TextView phoneNumber = (TextView) view.findViewById(R.id.contact_number_text);
//        phoneNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCall();
////                Intent intent = new Intent(Intent.ACTION_CALL);
////                intent.setData(Uri.parse("tel:" + getResources().getString(R.string.phone)));
////                if (ActivityCompat.checkSelfPermission(Dashboard_main.context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
////                    // TODO: Consider calling
////                    //    ActivityCompat#requestPermissions
////                    // here to request the missing permissions, and then overriding
////                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                    //                                          int[] grantResults)
////                    // to handle the case where the user grants the permission. See the documentation
////                    // for ActivityCompat#requestPermissions for more details.
////                    ActivityCompat.requestPermissions(thisActivity,
////                            new String[]{Manifest.permission.READ_CONTACTS},
////                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
////                    return;
////                }
////                Dashboard_main.context.startActivity(intent);
//            }
//        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall();
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }

    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(Dashboard_main.context, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},123);
//            requestPermissions(
//                    (Dashboard_main)Dashboard_main.context,
//                    new String[]{Manifest.permission.CALL_PHONE},
//                    123);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + getResources().getString(R.string.phone)));
            Dashboard_main.context.startActivity(intent);
//            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:12345678901")));
        }
    }
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.contact_us);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        getSupportActionBar().setTitle("Contact Us");
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        onBackPressed();
//        return true;
//    }
}
