package cerfontainecorps.athala;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Mathieu on 16-03-17.
 */

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.AddUserFloatingButton);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        System.out.println(v.getId());
        switch(v.getId())
        {
            case R.id.AddUserFloatingButton:
                EditText persoNEditText = (EditText)findViewById(R.id.PersoName);
                EditText lifePointEditText=(EditText)findViewById(R.id.lifepoint);
                EditText ManaPointEditText = (EditText)findViewById(R.id.manapoint);
                String persoN = persoNEditText.getText().toString();
                String persoLife = lifePointEditText.getText().toString();
                String persoMana = ManaPointEditText.getText().toString();

                if(!persoN.equals("") && !persoLife.equals("") && !persoMana.equals(""))
                {
                    AthalaData.Character c = new AthalaData.Character(persoN);
                    c.setMana(Integer.parseInt(persoMana));
                    c.setPv(Integer.parseInt(persoLife));
                    MainMenuActivity.CharacList.add(c);
                    Intent intent = new Intent();
                    setResult(1, intent);
                    finish();
                }
                else
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Remplissez tout les champs !";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            default:break;
        }
    }
}
