package com.meteo.meteo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    TextView textview = null;

    //////////////////////////////////
    //                              //
    //             onCreate         //
    //                              //
    //////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String Text = intent.getStringExtra(intent.EXTRA_TEXT);

        textview = (TextView) findViewById(R.id.text_meteo);
        textview.setText(Text);
    }

    //////////////////////////////////
    //                              //
    //             MENU             //
    //                              //
    //////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.Menu_share :
                OpenShare();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //////////////////////////////////
    //                              //
    //           OpenShare          //
    //                              //
    //////////////////////////////////
    public void OpenShare(){

        //Intent
            Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textview.getText());
                sendIntent.setType("text/plain");

        //Start inttent
            if(sendIntent.resolveActivity(getPackageManager()) != null){     //Check if there is an App to display map
                startActivity(sendIntent);                                   //Send Action
            }else{
                Toast.makeText(this, getString(R.string.no_share_app_msp), Toast.LENGTH_LONG).show();
                Log.v(MainActivity.LOG_TAG, "No app to share this msg!");
            }
    }
}
