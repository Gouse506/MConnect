package in.vmc.mcubeconnect.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.vmc.mcubeconnect.R;
import in.vmc.mcubeconnect.activity.LoginActivity;
import in.vmc.mcubeconnect.activity.MyApplication;

import in.vmc.mcubeconnect.backgroundservice.IncomingSms;
import in.vmc.mcubeconnect.callbacks.SmsListener;
import in.vmc.mcubeconnect.parser.Requestor;
import in.vmc.mcubeconnect.utils.ConnectivityReceiver;
import in.vmc.mcubeconnect.utils.OTPDialogFragment;
import in.vmc.mcubeconnect.utils.SingleTon;
import in.vmc.mcubeconnect.utils.TAG;
import in.vmc.mcubeconnect.utils.Utils;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSignup extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener,
        in.vmc.mcubeconnect.utils.TAG, OTPDialogFragment.OTPDialogListener {

    private static final String TAG = "SignupActivity";
    @InjectView(R.id.toolbar)
    Toolbar mtoolbar;
    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.input_phone)
    EditText _phone;
    @InjectView(R.id.input_repassword)
    EditText _repassword;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;
    @InjectView(R.id.checkBox1)
    CheckBox terms;
    OTPDialogFragment otpDialogFragment = new OTPDialogFragment();
    AlertDialog.Builder alertDialog;
    @InjectView(R.id.rootLayout)
    LinearLayout mroot;
    String ResOtp, OTP1;
    String name, email, password, repassword, phone;
    private ProgressDialog progressDialog;
    private String errormsg;
    private RequestQueue requestQueue;
    private SingleTon volleySingleton;

    public FragmentSignup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.signup, container, false);

        ButterKnife.inject(this, view);
        ((LoginActivity)getActivity()).setSupportActionBar(mtoolbar);
        ((LoginActivity)getActivity()).getSupportActionBar().setTitle("SignUp");
        ((LoginActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((LoginActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        alertDialog = new AlertDialog.Builder(getActivity());
        volleySingleton = SingleTon.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
//                getActivity().finish();
//                startActivity(new Intent(getActivity(), LoginActivity.class));
                ((LoginActivity) getActivity()).showLoginFragment();


            }
        });

        return view;
    }


    public void updateList(final String smsMessage) {
        //update otp     Your one time passwod for Mconnect is: 356958
        OTP1 = smsMessage.substring(39);
        //  String OTP1=smsMessage.split(": ")[0];

        // Log.d("SMPadmaS", OTP1+" "+OTP);
        if (otpDialogFragment != null && otpDialogFragment.isVisible()) {
            otpDialogFragment.setOPT(OTP1);
        }


    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else {
            // writeToLog("Software Keyboard was not shown");
        }
    }


    public void signup() {
        Log.d(TAG, "Signup");
        hideKeyboard();
        if (!validate()) {

            // onSignupFailed();
            //   Snackbar.make(mroot, errormsg, Snackbar.LENGTH_SHORT).show();
            return;
        } else {

            _signupButton.setEnabled(true);
            progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);


            name = _nameText.getText().toString();
            email = _emailText.getText().toString();
            password = _passwordText.getText().toString();
            repassword = _repassword.getText().toString();
            phone = _phone.getText().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("A one time password will be sent to your number +91" + phone + " go back to change your mobile number or continue to activate .")
                    .setCancelable(false)
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            GetOtp();

                        }
                    })
                    .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            _signupButton.setEnabled(true);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        getActivity().setResult(RESULT_OK, null);
        getActivity().finish();
    }


    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String repassword = _repassword.getText().toString();
        String phone = _phone.getText().toString();
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.error);
        drawable.setBounds(new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));
        if (name.isEmpty() || name.length() < 4) {
            _nameText.setError("at least 4 characters", drawable);
            errormsg = "Name must contain at least 4 characters";
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (phone.isEmpty() || phone.length() < 10) {
            _phone.setError("at least 10 digit", drawable);
            errormsg = "Phone must contain at least 10 digit";
            valid = false;
        } else {
            _phone.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address", drawable);
            errormsg = "Enter a valid email address";
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (repassword.isEmpty() || repassword.length() < 4 || repassword.length() > 10) {
            _repassword.setError("between 4 and 10 alphanumeric characters", drawable);
            errormsg = "Password must be between 4 and 10 alphanumeric characters";
            valid = false;
        } else {
            _repassword.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters", drawable);
            errormsg = "Password must be between 4 and 10 alphanumeric characters";
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!repassword.equals(password)) {
            _repassword.setError("password mismatch", drawable);
            errormsg = "Password mismatch";
            valid = false;
        } else {
            _repassword.setError(null);
        }
        if (!terms.isChecked()) {
            Snackbar snack = Snackbar.make(mroot, "Accept terms and condition to proceed", Snackbar.LENGTH_SHORT)
                    .setAction(null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(getActivity(), R.color.accent));
            TextView tv = (TextView) snack.getView().findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
            valid = false;
        } else {
            _repassword.setError(null);
        }

        return valid;
    }

    private void showOTPDialog() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        otpDialogFragment = new OTPDialogFragment();
        otpDialogFragment.setCancelable(false);
       // otpDialogFragment.setDialogTitle("Enter OTP");
        otpDialogFragment.setTargetFragment(this, 0);
        otpDialogFragment.show(fragmentManager, "Input Dialog");
    }

    @Override
    public void onFinishInputDialog(String inputText) {
        hideKeyboard();
        if (ResOtp.equals(inputText)) {
            Register();


        } else {
            Toast.makeText(getActivity(), "Invalid OTP ",
                    Toast.LENGTH_SHORT).show();
            _signupButton.setEnabled(true);

        }

        progressDialog.dismiss();
    }

    public void GetOtp() {
        if (ConnectivityReceiver.isConnected()) {
            new GetOtp(_phone.getText().toString()).execute();
        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetOtp();

                        }
                    })
                    .setActionTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }
    }

    public void Register() {
        if (ConnectivityReceiver.isConnected()) {
            new Register().execute();

        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Register();
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.accent));
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }
    }


    class GetOtp extends AsyncTask<Void, Void, JSONObject> {
        String message = "No Response from server";
        String code = "N";
        String phone = "n";

        String msg;
        JSONObject response = null;

        public GetOtp(String phone) {
            this.phone = phone;
        }

        @Override
        protected void onPreExecute() {
            // showProgress("Login Please Wait.."); progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO Auto-generated method stub


            try {
                response = Requestor.requestgetOTP(requestQueue, GENERATE_OTP_URL, phone);
                //response = JSONParser.getOTP(GENERATE_OTP_URL, phone);
                Log.d("TEST", response.toString());
                if (response != null) {
                    if (response.has(CODE)) {

                        code = response.getString(CODE);
                        Log.d("TEST", code.toString());
                    }
                    if (response.has(OTP)) {
                        ResOtp = response.getString(OTP);
                    }
                    if (response.has(MESSAGE)) {
                        msg = response.getString(MESSAGE);
                    }
                }

                //  if(response.c)

            } catch (Exception e) {
                Log.d("TEST", e.toString());
                e.printStackTrace();
            }


            return response;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            if (data != null) {
                Log.d("TEST", data.toString());
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (code.equals("202")) {
                //Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                Utils.Alert(getActivity(), msg,false,"");
                _signupButton.setEnabled(true);
            } else {
                showOTPDialog();
            }

        }

    }

    class Register extends AsyncTask<Void, Void, JSONObject> {
        String code = "N";
        JSONObject response = null;
        private String msg;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setMessage("Verfying OTP");
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = Requestor.requestRegister(requestQueue, REGISTER_URL, phone, name, email, password);
                // response = JSONParser.Register(REGISTER_URL, phone, name, email, password);
                Log.d("TEST", response.toString());
                code = response.getString(CODE);
                msg = response.getString(MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return response;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            if (data != null) {
                Log.d("TEST", data.toString());
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (code.equals("N")) {
                Snackbar.make(mroot, "No Response From Server", Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Register();

                            }
                        }).
                        setActionTextColor(ContextCompat.getColor(getActivity(), R.color.primary_dark)).show();
            }
            if (code.equals("202")) {
                Snackbar.make(mroot, msg, Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Register();

                            }
                        })
                        .setActionTextColor(ContextCompat.getColor(getActivity(), R.color.accent)).show();
            } else {
                onSignupSuccess();
                Utils.Alert(getActivity(), msg,true,email);
                _signupButton.setEnabled(true);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }


    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (!isConnected) {
            message = "Sorry! Not connected to internet";
            color = Color.RED;

            Snackbar snackbar = Snackbar
                    .make(mroot, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }


}
