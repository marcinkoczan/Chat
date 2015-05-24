package net.marcinkoczan.chat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by zwyklyuser on 2015-05-23.
 */
public class SerwerBluetooth extends Thread {
    private BluetoothServerSocket mmServerSocket=null;

    public  BluetoothSocket socket = null;
    public  PrintWriter out;
    String inputt=null;
    int stan=0; // 0 brak wiadomosci, 1 nowa wiadomosc
    Handler mHandler;

    public SerwerBluetooth(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothServerSocket tmp = null;
        try{
            UUID uuid= UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Us³uga witaj¹ca",uuid);
        } catch (IOException e) {}
        mmServerSocket = tmp;
    }

    public void run(){
        Log.d("Informacja","Uruchamiam serwer" );
        while(true){
            try{
                Log.d("Informacja", "Czekam na po³¹czenie od klienta");
                socket = mmServerSocket.accept();
                Log.d("Informacja","Mam klienta!");
//
                out = new PrintWriter(socket.getOutputStream(),true);
                out.println("Witaj kolego!");

                while (true){
                    BufferedReader inp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    inputt = inp.readLine();
                    Log.d("Informacja","Klient mówi: "+inputt);
                    stan=1;
                }

            } catch (IOException e){break;}
//            if (socket !=null){
//                //jakies operacje
//                try{
//                    mmServerSocket.close();
//                } catch (Exception e) {e.printStackTrace();}
//                break;
//            }
        }
    }

    public void send(String msg){
        try{
        out = new PrintWriter(socket.getOutputStream(),true);
        out.println(msg);}
        catch (Exception e){}
    }

    public String odebrana_wiadomosc(){
        stan=0;
        return inputt;
    }

}
