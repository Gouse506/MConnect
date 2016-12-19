package in.vmc.mcubeconnect.fragment;


import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
public class ForgotPassword extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener, TAG,
        TextWatcher {

    @InjectView(R.id.fginput_phone)
    EditText _phone;
    @InjectView(R.id.sendOtp)
    Button _sendOTp;
    @InjectView(R.id.fginput_repassword)
    EditText _repassword;
    @InjectView(R.id.fginput_password)
    EditText _passwordText;
    @InjectView(R.id.btn_changepassword)
    Button _changePassButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;
    @InjectView(R.id.fg_OTP)
    EditText _fgOTP;
    @InjectView(R.id.mroot)
    LinearLayout mroot;
    OTPDialogFragment otpDialogFragment = new OTPDialogFragment();
    AlertDialog.Builder alertDialog;
    private String OTP1,OTPres,OTPsms;
    private ProgressDialog progressDialog;
    private String password, repassword, phone;
    private RequestQueue requestQueue;
    private SingleTon volleySingleton;


    public ForgotPassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        ButterKnife.inject(this, view);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
       ((LoginActivity)getActivity()).setSupportActionBar(toolbar);
       ((LoginActivity)getActivity()).getSupportActionBar().setTitle("Forgot Password");
        ((LoginActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((LoginActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        volleySingleton = SingleTon.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        _phone.requestFocus();
       getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog = new AlertDialog.Builder(getActivity());

        _changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(OTPsms.equals(OTPres))
                changePass();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
               // startActivity(new Intent(getActivity(),LoginActivity.class));
//               getActivity().finish();
             ((LoginActivity) getActivity()).showLoginFragment();



            }
        });

        _sendOTp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTP1 = _phone.getText().toString();
                if(OTP1!=null  && OTP1.length()==10) {
                    GetOtp();
                    _sendOTp.setText("Resend");
                }else {
                    _phone.setError("Enter Phone Number");
                }
            }
        });


        _phone.addTextChangedListener(this);



        return view;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      IncomingSms.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text",messageText);
                _fgOTP.setText(messageText.substring(42));

                //Toast.makeText(getActivity(),"Message: "+messageText,Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String password = _passwordText.getText().toString();
        String repassword = _repassword.getText().toString();
        String phone = _phone.getText().toString();
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.error);
        drawable.setBounds(new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));

        if (phone.isEmpty() || phone.length() < 10) {
            _phone.setError("at least 10 digit", drawable);

            valid = false;
        } else {
            _phone.setError(null);
        }


        if (repassword.isEmpty() || repassword.length() < 4 || repassword.length() > 10) {
            _repassword.setError("between 4 and 10 alphanumeric characters", drawable);

            valid = false;
        } else {
            _repassword.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters", drawable);

            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!repassword.equals(password)) {
            _repassword.setError("password mismatch", drawable);

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
        otpDialogFragment.setDialogTitle("Enter OTP");
        otpDialogFragment.show(fragmentManager, "Input Dialog");
    }

    public void changePass() {

        Log.d("TAG", "forgotPass");
        hideKeyboard();
        if (!validate()) {
            return;
        } else {

            phone = _phone.getText().toString();
            password = _passwordText.getText().toString();
            OTPsms=_fgOTP.getText().toString();
            ChangePass();
        }

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



//    @Override
//    public void onFinishInputDialog(String inputText) {
//        hideKeyboard();
//        OTP1 = inputText;
//        ChangePass();
//    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
       /* if (s.length() == 10) {
            OTP1 = _phone.getText().toString();
            GetOtp();
        }*/
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void updateList(final String smsMessage) {
        //Your OTP for Mconnect password change is: 090502
        OTP1 = smsMessage.substring(42);
        //  String OTP1=smsMessage.split(": ")[0];
        _fgOTP.setText(OTP1);
        _passwordText.requestFocus();


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

    public void ChangePass() {
        if (ConnectivityReceiver.isConnected()) {
            new ChangePass().execute();

        } else {
            Snackbar snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ChangePass();
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
             //showProgress("Waiting for SMS..");
            _sendOTp.setEnabled(false);
            _fgOTP.setText("Waiting for OTP");
            Log.d("OTP", OTP1);
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO Auto-generated method stub


            try {
                response = Requestor.requestgetOTP(requestQueue, FORGOT_OTP_URL, phone);
                // response = JSONParser.getOTP(FORGOT_OTP_URL, phone);
                Log.d("RESPONSE", response + "");
                if (response != null) {
                    if (response.has(CODE)) {
                        code = response.getString(CODE);
                    }
                    if (response.has(MESSAGE)) {
                        msg = response.getString(MESSAGE);
                    }
                    if (response.has(OTP)) {
                        OTPres = response.getString(OTP);
                    }
                }

                //  if(response.c)

            } catch (Exception e) {
                e.printStackTrace();
            }


            return response;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.error);
            drawable.setBounds(new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));
            _sendOTp.setEnabled(true);
            if (data != null) {

                Log.d("OTP", data.toString());

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (code.equals("202")) {
                    //  Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                    _phone.setError(msg, drawable);
                    _changePassButton.setEnabled(true);
                   // _sendOTp.setEnabled(true);
                } else {
                    //showOTPDialog();
                    _phone.setError(null);
                    //_sendOTp.setEnabled(true);

                }

            }
        }

    }


    private void showProgress(String msg) {
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    class ChangePass extends AsyncTask<Void, Void, JSONObject> {
        String code = "N";
        JSONObject response = null;
        private String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _changePassButton.setEnabled(false);
            showProgress("Verifying OTP..");
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = Requestor.requestChangePass(requestQueue, CHANGED_PASS, phone, OTPsms, password);
                //response = JSONParser.ChangePass(CHANGED_PASS, phone, OTP1, password);
                code = response.getString(CODE);
                msg = response.getString(MESSAGE);
                Log.d("RESPONSE", response + "");
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
            _changePassButton.setEnabled(true);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (code.equals("n")) {
                Snackbar.make(mroot, "No Response From Server", Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChangePass();
                            }
                        }).
                        setActionTextColor(ContextCompat.getColor(getActivity(), R.color.primary_dark)).show();
            }
           // if (code.equals("202")) {
            if (code!=null && !code.equals("400")) {
                Snackbar.make(mroot, msg, Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChangePass();

                            }
                        })
                        .setActionTextColor(ContextCompat.getColor(getActivity(), R.color.accent)).show();
            } else {
                Utils.Alert(getActivity(),msg,false,null);
                //_changePassButton.setEnabled(true);
//                Intent intent = new Intent();
//                intent.putExtra("msg", msg);
//               getActivity().setResult(RESULT_OK, intent);
//                getActivity().finish();

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
