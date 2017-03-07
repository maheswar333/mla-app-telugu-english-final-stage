package main.com.dvb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import main.com.dvb.services.WebServices;

public class UserForgetPassword extends AppCompatActivity implements TextWatcher {

   private AlertDialog alertDialogok;
    private ProgressDialog progressDialog;
    private Button forget_submit;
    private EditText forgetuserName,forgetmobile;
    private TextInputLayout forgetuserNameTTL,forgetmobileTTL;
    private WebServices webServices;
    private TextView messagetext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forget_password);

        forgetuserName = (EditText) findViewById(R.id.forget_UserName);
        forgetmobile = (EditText) findViewById(R.id.forget_mobilenumber);
        forgetuserNameTTL = (TextInputLayout)findViewById(R.id.forget_UserNnameTTL);
        forgetmobileTTL = (TextInputLayout)findViewById(R.id.forgetmobileTTL);
        forget_submit = (Button) findViewById(R.id.forget_submit);
        messagetext = (TextView) findViewById(R.id.forget_textview);
        forgetuserName.addTextChangedListener(this);
        forgetmobile.addTextChangedListener(this);
        getSupportActionBar().setTitle("Forgot Password ");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        webServices = new WebServices();
        forget_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = forgetuserName.getText().toString();
                String mobile = forgetmobile.getText().toString();

                if(userName.equalsIgnoreCase("")|| mobile.equalsIgnoreCase("")){

                    if (userName.isEmpty()){
                        forgetuserName.requestFocus();
                        forgetuserNameTTL.setErrorEnabled(true);
                        forgetuserNameTTL.setError("You missed the User Name field");
                    }
                    if (mobile.isEmpty()){
                        forgetmobile.requestFocus();
                        forgetmobileTTL.setErrorEnabled(true);
                        forgetmobileTTL.setError("You missed the password field");

                    }
                    return;
                }

                String formPostData ="";
                try {
                    formPostData = ForgetPassFormData(userName,mobile);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (checkIsNetworkAvailable()){
                    Log.e("username",""+userName);
                    Log.e("password",""+mobile);

                    // new LoginTask().execute(formPostData);
                }else{
                    errorMessage();
                }


            }
        });

    }

    private String ForgetPassFormData(String uname,String mobi) throws UnsupportedEncodingException {

        String data = URLEncoder.encode("name", "UTF-8")
                + "=" + URLEncoder.encode(uname, "UTF-8");
        data += "&" + URLEncoder.encode("username", "UTF-8")
                + "=" + URLEncoder.encode(mobi, "UTF-8");
        return  data;
    }


    public class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result="";

            try {
                result = webServices.postRequest(params[0], Constants.USER_LOGIN);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            forgetuserName.setText("");
            forgetmobile.setText("");
            progressDialog.dismiss();
            try {
                JSONObject responseObj = new JSONObject(s);
                boolean error = responseObj.getBoolean("error");
                boolean result = responseObj.getBoolean("result");

                if(result){
                    Toast.makeText(UserForgetPassword.this, "sent successfull", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Not processed, please try again", Toast.LENGTH_LONG).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count == 0) {
            if (forgetuserName.isFocused()) {
                forgetuserNameTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            } else if (forgetmobile.isFocused()) {
                forgetmobileTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }
        }else {
            if (forgetuserName.isFocused()) {
                forgetuserNameTTL.setErrorEnabled(false);
                forgetuserName.setBackground(ContextCompat.getDrawable(this, R.drawable.textfield_active));
                forgetuserNameTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            } else if (forgetmobile.isFocused()) {
                forgetmobileTTL.setErrorEnabled(false);
                forgetmobile.setBackground(ContextCompat.getDrawable(this, R.drawable.textfield_active));
                forgetmobileTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            }

        }
    }


    @Override
    public void afterTextChanged(Editable s) {

    }


    //Checking for network connection
    public boolean checkIsNetworkAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    public void errorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert.!");
        builder.setMessage("Unable to connect server. Please check your internet connection.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return;
    }


}
