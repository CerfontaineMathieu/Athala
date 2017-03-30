package cerfontainecorps.athala;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;

import AthalaData.User;
import AthalaPayload.ReponseAndroid;
import AthalaPayload.RequeteAndroid;
import Threads.ThreadClient;
import Threads.ThreadClientTimeout;

/**
 * Created by Mathieu on 14-03-17.
 */

public class MainMenuActivity extends AppCompatActivity implements AbsListView.MultiChoiceModeListener  {
    public static ListView Charac_lv;
    public static LinkedList CharacList;
    public static ArrayAdapter<String> adapterList;
    private User MainUser;
    private LinkedList checkCharac;
    private ThreadClientTimeout thCT;
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
            thCT = new ThreadClientTimeout(ThreadClient.ip, MainUser);
            thCT.start();
            CharacList = MainUser.getListOfCharac();
            if (CharacList.size() > 0) {
                Charac_lv = (ListView) findViewById(R.id.CharactersList);
                Charac_lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                Charac_lv.setMultiChoiceModeListener(this);
                adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CharacList);
                Charac_lv.setAdapter(adapterList);
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
                Charac_lv= (ListView) findViewById(R.id.CharactersList);
                adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CharacList);
                Charac_lv.setAdapter(adapterList);
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

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        System.out.println("OnItemCheckedStateChanged | Checked : Item["+position+"]["+id+"] -> "+checked );
        AthalaData.Character currentCharater = (AthalaData.Character) Charac_lv.getItemAtPosition(position);
        View v = Charac_lv.getChildAt(position);
        if(checked){
            checkCharac.add(currentCharater);
            v.setBackgroundColor(Color.GRAY);
        }
        else
        {
            checkCharac.remove(currentCharater);
            v.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        checkCharac = new LinkedList();
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.cab_menu, menu);
        return true;

    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        System.out.println("OnPrepareActionMode");
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cab_delete:
                // delete all the selected character from server
                removeDeletedCharac();
                MainUser.setListOfCharac(CharacList);
                RequeteAndroid req = new RequeteAndroid(RequeteAndroid.REQUEST_DELETE_CHARAC,MainUser);
                LoginActivity.thC.SetRequete(req);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("Sleep error :" +e);
                }
                if(LoginActivity.retRep.getCode()== ReponseAndroid.REPONSE_OK)
                {
                    Charac_lv= (ListView) findViewById(R.id.CharactersList);
                    adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CharacList);
                    Charac_lv.setAdapter(adapterList);
                }
                mode.finish(); // Action picked, so close the CAB
                return true;
            default:
                return false;
        }

    }

    public void removeDeletedCharac()
    {
        for(int i =0; i<checkCharac.size();i++)
        {
            AthalaData.Character c = (AthalaData.Character) checkCharac.get(i);
            if(CharacList.contains(c))
            {
                CharacList.remove(c);
            }
        }
    }
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        System.out.println("OnDestroyActionMode");
    }
}
