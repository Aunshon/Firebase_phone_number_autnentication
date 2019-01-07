package com.example.aunshon.phone2;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText EnterCode,VarifyCode;
    Button SendCodeBtn,VarifyCodeBtn;
    String SentCode;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EnterCode=findViewById(R.id.phonenumber);
        VarifyCode=findViewById(R.id.smscode);
        SendCodeBtn=findViewById(R.id.sendcodetophone);
        VarifyCodeBtn=findViewById(R.id.Varifycode);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Code Recived", Toast.LENGTH_SHORT).show();
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Please check your phone number or check your country code", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            SentCode=s;
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Code Sent to "+EnterCode.getText().toString()+" 😍", Toast.LENGTH_SHORT).show();
        }
    };
    public void SendcodeBUTTON(View view) {
        progressDialog.setMessage("Sending Varification Code");
        progressDialog.show();
        String phoneNumber=EnterCode.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    public void VarifycodeBUTTON(View view) {
        progressDialog.setMessage("Varifing code😘..");
        progressDialog.show();
        String code=VarifyCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(SentCode, code);
        signInWithPhoneAuthCredential(credential);
    }
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
////                            // Sign in success, update UI with the signed-in user's information
////                            Log.d(TAG, "signInWithCredential:success");
////
////                            FirebaseUser user = task.getResult().getUser();
////                            // ...
//                            Toast.makeText(MainActivity.this, "Code mached", Toast.LENGTH_SHORT).show();
//                        } else {
////                            // Sign in failed, display a message and update the UI
////                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                                Toast.makeText(MainActivity.this, "Wrong code", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//    }
private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
    mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //here you can open new activity
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Login Successfull", Toast.LENGTH_LONG).show();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
}
}
