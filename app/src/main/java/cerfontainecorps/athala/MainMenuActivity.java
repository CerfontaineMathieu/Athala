package cerfontainecorps.athala;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;

import AthalaData.User;
import AthalaPayload.ReponseAndroid;
import AthalaPayload.RequeteAndroid;

/**
 * Created by Mathieu on 14-03-17.
 */

public class MainMenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static ListView Charac_lv;
    public static LinkedList CharacList;
    public static ArrayAdapter<String> adapterList;
    private User MainUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        CharacList = new LinkedList();

        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        String pwd = intent.getStringExtra("pwd");
        MainUser = new User(login,pwd);
        RequeteAndroid req = new RequeteAndroid(RequeteAndroid.REQUEST_GET_CHARAC,MainUser);
        LoginActivity.thC.SetRequete(req);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Sleep error :" +e);
        }
        if(LoginActivity.retRep.getCode()== ReponseAndroid.REPONSE_OK)
        {
            MainUser = LoginActivity.retRep.getMainUser();
            CharacList = MainUser.getListOfCharac();
            if(CharacList.size()>0) {
                Charac_lv = (ListView) findViewById(R.id.CharactersList);
                adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CharacList);
                Charac_lv.setAdapter(adapterList);
                Charac_lv.setOnItemClickListener(this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.AddUserButton:
                Intent intent = new Intent(this,AddUserActivity.class);
                int requestReturn = 1;
                startActivityForResult(intent,requestReturn);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object o = parent.getItemAtPosition(position);
        System.out.println(o);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(CharacList.size()>0) {
            MainUser.setListOfCharac(CharacList);
            RequeteAndroid req = new RequeteAndroid(RequeteAndroid.REQUEST_NEW_CHARAC,MainUser);
            LoginActivity.thC.SetRequete(req);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Sleep error :" +e);
            }
            if(LoginActivity.retRep.getCode()== ReponseAndroid.REPONSE_OK)
            {
                Charac_lv = (ListView) findViewById(R.id.CharactersList);
                adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CharacList);
                Charac_lv.setAdapter(adapterList);
                Charac_lv.setOnItemClickListener(this);
            }
            else
            {
                Context context = getApplicationContext();
                CharSequence text = "Un erreur s'est produite pendant la création du personnage, veuillez réessayer.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }
}
