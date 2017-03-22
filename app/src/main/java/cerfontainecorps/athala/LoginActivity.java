package cerfontainecorps.athala;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import AthalaData.User;
import AthalaPayload.ReponseAndroid;
import AthalaPayload.Requete;
import AthalaPayload.RequeteAndroid;
import Threads.ThreadClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public static ThreadClient thC;
    private Button ConnectButton;
    public static ReponseAndroid retRep = new ReponseAndroid(ReponseAndroid.REPONSE_OK);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        thC = new ThreadClient();
        thC.start();
        ConnectButton  = (Button)findViewById(R.id.ConnectButton);
        ConnectButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        RequeteAndroid req = null;
        switch(v.getId())
        {
            case R.id.ConnectButton:
                EditText logon = (EditText) findViewById(R.id.LoginText);
                EditText pass = (EditText)findViewById(R.id.passText);
                String pseudo = logon.getText().toString();
                String password = pass.getText().toString();
                if(!pseudo.equals("") && !password.equals("")){
                    req = new RequeteAndroid(RequeteAndroid.REQUEST_LOGIN, pseudo,password);
                    thC.SetRequete(req);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("Sleep error :" +e);
                    }
                    if(retRep.getCode()==ReponseAndroid.REPONSE_OK)
                    {
                        Intent intent = new Intent(this,MainMenuActivity.class);
                        intent.putExtra("login",pseudo);
                        intent.putExtra("pwd",password);
                        startActivity(intent);
                    }
                }
                break;
        }

    }
    public void onSignIn(View view)
    {
        switch(view.getId())
        {
            case R.id.SignIn:
                Intent intent = new Intent(this,SignInActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}