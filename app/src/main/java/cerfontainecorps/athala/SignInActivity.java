package cerfontainecorps.athala;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import AthalaPayload.RequeteAndroid;

import static cerfontainecorps.athala.LoginActivity.thC;

/**
 * Created by Mathieu on 14-03-17.
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    private Button Confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Confirm = (Button)findViewById(R.id.ConfirmButton);
        Confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ConfirmButton:
                EditText logon = (EditText) findViewById(R.id.pseudoText);
                EditText pass = (EditText)findViewById(R.id.PasswordText);
                String pseudo = logon.getText().toString();
                String password = pass.getText().toString();
                if(!pseudo.equals("") && !password.equals(""))
                {
                    RequeteAndroid req = new RequeteAndroid(RequeteAndroid.REQUEST_NEW_USER,pseudo,password);
                    thC.SetRequete(req);
                    finish();
                }
                break;
            default : break;
        }
    }
}
