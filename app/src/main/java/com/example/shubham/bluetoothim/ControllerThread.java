package com.example.shubham.bluetoothim;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Shubham on 8/5/2017.
 */

public class ControllerThread {

    private static final UUID myUUID=UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private final BluetoothAdapter bluetoothAdapter;
    private ClientConnectionThread clientconnectionThread;
    private ServerConnectionThread serverConnectionThread;
    private ConnectedThread connectedThread;
    private UUID deviceId;
    Context context;
    private BluetoothDevice bluetoothDevice;
    public ControllerThread(Context ctx){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        context=ctx;
        start();
    }

    public synchronized void start(){
        if(serverConnectionThread !=null){
            serverConnectionThread.cancel();
            serverConnectionThread=null;
        }
        if(clientconnectionThread==null){
            clientconnectionThread=new ClientConnectionThread();
            clientconnectionThread.start();
        }
    }


    public void startClient(BluetoothDevice device, UUID uuid){
        serverConnectionThread = new ServerConnectionThread(device,uuid);
        serverConnectionThread.start();
    }


    public void connected(BluetoothSocket bluetoothSocket,BluetoothDevice bluetoothDevice){

        connectedThread =new ConnectedThread(bluetoothSocket);
        connectedThread.start();
    }

    public void write(byte[] byteArr){
        connectedThread.write(byteArr);
    }



    private class ClientConnectionThread extends Thread{

        private final BluetoothServerSocket bluetoothServerSocket;

        public ClientConnectionThread(){
            BluetoothServerSocket bss=null;
            try{
                bss=bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("bluetooth",myUUID);
            }catch (Exception e){
                e.printStackTrace();
            }
            bluetoothServerSocket=bss;
        }

        public  void run(){
            BluetoothSocket bluetoothSocket=null;
            try{
                bluetoothSocket=bluetoothServerSocket.accept();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(bluetoothSocket!=null){
                connected(bluetoothSocket,bluetoothDevice);
            }
        }

        public void cancel(){
            try{
                bluetoothServerSocket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private class ServerConnectionThread extends Thread{
        private BluetoothSocket bluetoothSocket;

        public ServerConnectionThread(BluetoothDevice tempBluetoothdevice,UUID tempdeviceId){
            bluetoothDevice=tempBluetoothdevice;
            deviceId=tempdeviceId;

        }

        public void run(){
            BluetoothSocket tempbluetoothSocket=null;
            try{
                tempbluetoothSocket=bluetoothDevice.createInsecureRfcommSocketToServiceRecord(deviceId);

            }catch (Exception e){
                e.printStackTrace();
            }
            bluetoothSocket=tempbluetoothSocket;
            bluetoothAdapter.cancelDiscovery();
            try {
                bluetoothSocket.connect();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    bluetoothSocket.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            connected(bluetoothSocket,bluetoothDevice);
        }


        public void cancel(){
            try{
                bluetoothSocket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }



    private class ConnectedThread extends Thread{
        private final BluetoothSocket bluetoothSocket;
        private InputStream inputStream=null;
        private OutputStream outputStream=null;
        public ConnectedThread(BluetoothSocket tempBluetoothSocket){
            bluetoothSocket=tempBluetoothSocket;
            try {
                inputStream = bluetoothSocket.getInputStream();
                outputStream = bluetoothSocket.getOutputStream();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        public void run(){
            byte[] byteArr=new byte[1024];
            int bytes;
            while (true){
                try {
                    bytes = inputStream.read(byteArr);
                    if(bytes!=0) {
                        String inputMessage = new String(byteArr, 0, bytes);
                       // Toast.makeText(context,inputMessage,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }

        public void write(byte[] opByteArr){
            try {
                outputStream.write(opByteArr);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void cancel(){
            try{
                bluetoothSocket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



}
