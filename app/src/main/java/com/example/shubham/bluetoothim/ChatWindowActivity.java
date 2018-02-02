package com.example.shubham.bluetoothim;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created by Shubham on 8/6/2017.
 */

public class ChatWindowActivity extends Activity {
ControllerThread mBluetoothConnection;
    EditText editTextSendMsg;
    Button btnSend;
    BluetoothDevice btDevice;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        FragmentTransaction ft=getFragmentManager().beginTransaction();

        btDevice = getIntent().getExtras().getParcelable("BluetoothDevice");
        mBluetoothConnection = new ControllerThread(ChatWindowActivity.this);


        editTextSendMsg =(EditText)findViewById(R.id.editTextSendMsg);
        btnSend = (Button)findViewById(R.id.btnSend);

        startConnection();
        for(int i=0;i<5;i++){
            FragmentMessages fragmentMessages = new FragmentMessages();
            ft.add(R.id.messagesFragment, fragmentMessages);
        }
        ft.commit();



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] bytes = editTextSendMsg.getText().toString().getBytes(Charset.defaultCharset());
                mBluetoothConnection.write(bytes);
            }
        });


    }

    //create method for starting connection
//***remember the conncction will fail and app will crash if you haven't paired first
    public void startConnection(){
        startBTConnection(btDevice,MY_UUID_INSECURE);
    }

    /**
     * starting chat service method
     */
    public void startBTConnection(BluetoothDevice device, UUID uuid){

        mBluetoothConnection.startClient(device,uuid);
    }

}
