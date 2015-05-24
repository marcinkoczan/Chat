package net.marcinkoczan.chat;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Start extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void serwer(View view){
        Log.d("Informacja", "Dajê siê wykryæ!");
        Intent pokazsie = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        pokazsie.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(pokazsie);
        Intent in = new Intent(this,MainActivity.class);
        in.putExtra("Stan",1);
        startActivity(in);
    }

    public void klient(View view){
        Intent in = new Intent(this,MainActivity.class);
        in.putExtra("Stan",2);
        startActivity(in);
    }
}
