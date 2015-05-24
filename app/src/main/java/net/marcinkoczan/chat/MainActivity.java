package net.marcinkoczan.chat;

import android.app.DownloadManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class MainActivity extends ActionBarActivity {

    public BluetoothAdapter ba;
    public BluetoothDevice serwer;
    public SerwerBluetooth serwerek;
    public ClientBluetooth kliencik;
    int stan = 0; // 0 nic, 1 serwer, 2 klient
    public ListView wiadomosci;
    public ArrayAdapter<String> aa;
    public ArrayList<String> lista = new ArrayList<String>();
    public ArrayList<String> lista2 = new ArrayList<String>();
    public EditText ed;
    public Button btn;
    public InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
        ba = BluetoothAdapter.getDefaultAdapter();
        wiadomosci = (ListView) findViewById(R.id.lv);
        aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista);

        wiadomosci.setAdapter(aa);
        ed = (EditText) findViewById(R.id.et);
        btn = (Button) findViewById(R.id.btn);

        Intent in = getIntent();
        stan = in.getIntExtra("Stan",0);
        Log.d("Info",stan+"");


        if (stan == 1) {
            serwerek = new SerwerBluetooth();
            serwerek.start();
            czekaj_na_wiadomosc cnw = new czekaj_na_wiadomosc();
            cnw.start();
        }
        if (stan == 2) {
            serwer = ba.getRemoteDevice("00:AA:70:FF:B1:51");
            kliencik = new ClientBluetooth(serwer);
            kliencik.start();
            czekaj_na_wiadomosc cnw = new czekaj_na_wiadomosc();
            cnw.start();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void dajsiewykryc(){
        Log.d("Informacja", "Dajê siê wykryæ!");
        Intent pokazsie = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        pokazsie.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        startActivity(pokazsie);

    }

    public void pokazsparowane(){
        Log.d("Informacja","Sparowane urz¹dzenia to:");
        Set<BluetoothDevice> pairedDevices = ba.getBondedDevices();
        if (pairedDevices.size()>0){
            for (BluetoothDevice device : pairedDevices){
                Log.d("Informacja",device.getName()+" - "+device.getAddress());
            }
        }
    }

    public void wyslij(View view){
        if (stan == 1){
            aa.notifyDataSetChanged();
            serwerek.send(ed.getText().toString());
            lista.add("Serwer: " + ed.getText().toString());
            ed.setText("");
        }
        if (stan == 2)
        {
            aa.notifyDataSetChanged();
            kliencik.send(ed.getText().toString());
            lista.add("Klient: " + ed.getText().toString());
            ed.setText("");
        }

    }

    public void odbierz_widomosc(){
            if (stan==1){
                if (serwerek.stan == 1) {
                    lista.add("Klient: " + serwerek.odebrana_wiadomosc());
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
            else{
                if (kliencik.stan == 1) {
                    lista.add("Serwer: " + kliencik.odebrana_wiadomosc());
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                 }
            }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.dajsiewykryc) {
            dajsiewykryc();
            return true;
        }
        if (id == R.id.pokazsparowane) {
            pokazsparowane();
            return true;
        }
        if (id == R.id.serwer) {
            stan =1;
            serwerek = new SerwerBluetooth();
            serwerek.start();
            czekaj_na_wiadomosc cnw = new czekaj_na_wiadomosc();
            cnw.start();
            return true;
        }
        if (id == R.id.klient) {
            stan =2;
            serwer = ba.getRemoteDevice("00:AA:70:FF:B1:51");
            kliencik = new ClientBluetooth(serwer);
            kliencik.start();
            czekaj_na_wiadomosc cnw = new czekaj_na_wiadomosc();
            cnw.start();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class czekaj_na_wiadomosc extends Thread {
        public void run(){
            Log.d("Info","czekam na wiadomosc");
            while(true){
            try{
                while(true){

                odbierz_widomosc();
            }} catch (Exception e){}
        }}
    }
}



