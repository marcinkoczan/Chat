package net.marcinkoczan.chat;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by zwyklyuser on 2015-05-23.
 */
public class ClientBluetooth extends Thread {

    public BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    public  PrintWriter out;
    int stan=0; // 0 brak wiadomosci, 1 nowa wiadomosc
    String inputt;
    Handler mHandler;

    public ClientBluetooth(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        try{
            UUID uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (Exception e){}
        mmSocket=tmp;
    }

    public void run() {
        try{
            Log.d("Informacja", "Próba po³¹czenia.....");
            mmSocket.connect();
            Log.d("Informacja", "Po³¹czono z serwerem!");
            BufferedReader in = new BufferedReader(new InputStreamReader(mmSocket.getInputStream()));
            String input = in.readLine();
            Log.d("Informacja","Serwer mówi: "+input);

            while (true){
                BufferedReader inp = new BufferedReader(new InputStreamReader(mmSocket.getInputStream()));
                inputt = inp.readLine();
                Log.d("Informacja","Serwer mówi: "+inputt);
                stan=1;

            }

        } catch (Exception e) {
//            try {
//                mmSocket.close();
//            } catch (Exception cle) {}

        }
        }

    public void send(String msg){
        try{
            out = new PrintWriter(mmSocket.getOutputStream(),true);
            out.println(msg);}
        catch (Exception e){}
    }

    public String odebrana_wiadomosc(){
        stan=0;
        return inputt;
    }


    }

