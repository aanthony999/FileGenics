package com.example.filetransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothSettings extends AppCompatActivity {

    private static final int DISCOVER_DURATION = 300;

    private static final int REQUEST_BLU = 1;
    private Button settings, Onbtn, Offbtn, Discoverbtn, listDevices, Select, send, listen;
    TextView status, paired, filenameview, msg_box;
    ImageView visualstatus, imageView;
    ListView listView;
    EditText writemsg;

    BluetoothAdapter BLTadapter;
    BluetoothDevice[] btArray;

    SendReceive sendReceive;


    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;

    int REQUEST_ENABLE_BLUETOOTH = 1;

    private static final String APP_NAME = "fileTransfer";
    private static final UUID MY_UUID = UUID.fromString("02ddf2be-438a-11ea-b77f-2e728ce88125");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        findViewByIdes();
        BLTadapter = BluetoothAdapter.getDefaultAdapter();

        // first check is to see if bluetooth is available on device
        if (BLTadapter == null) {
            status.setText("Bluetooth not available");
        } else {
            status.setText("Bluetooth is available");
        }

        if (BLTadapter.isEnabled()) {
            visualstatus.setImageResource(R.drawable.ic_action_on);
        } else {
            visualstatus.setImageResource(R.drawable.ic_action_off);
        }


        implementListeners();

    }

    private void findViewByIdes() {
        BLTadapter = BluetoothAdapter.getDefaultAdapter();
        Onbtn = findViewById(R.id.blt1);
        Offbtn = findViewById(R.id.blt2);
  //      listDevices = findViewById(R.id.listdevice);
        status = findViewById(R.id.tv1);
    //    paired = findViewById(R.id.msg);
        visualstatus = findViewById(R.id.BLTvisualstatus);
        send = findViewById(R.id.listdevices);
        listView = findViewById(R.id.lvDiscussionTopics);
   //     listen = findViewById(R.id.listen);
        msg_box = (TextView) findViewById(R.id.status);
   //     writemsg = findViewById(R.id.writemsg);

    }

    private void implementListeners() {
        Onbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BLTadapter.isEnabled()) {
                    Toast.makeText(BluetoothSettings.this, "Turning on bluetooth", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                } else {
                    Toast.makeText(BluetoothSettings.this, "Bluetooth is already turned on", Toast.LENGTH_LONG).show();
                }
                visualstatus.setImageResource(R.drawable.ic_action_on);
            }

        });

        Offbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BLTadapter.isEnabled()) {
                    BLTadapter.disable();
                    Toast.makeText(BluetoothSettings.this, "Turning off bluetooth", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(BluetoothSettings.this, "Bluetooth is already turned off", Toast.LENGTH_LONG).show();
                }
                visualstatus.setImageResource(R.drawable.ic_action_off);
            }
        });

        Discoverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    Toast.makeText(BluetoothSettings.this, "Turning on bluetooth", Toast.LENGTH_LONG).show();
               /* if (BLTadapter.isDiscovering()) {
                    Toast.makeText(BluetoothSettings.this, "Making Device Discoverable", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }*/

                BLTadapter.startDiscovery();

            }
        });
        listDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> bt = BLTadapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                btArray = new BluetoothDevice[bt.size()];
                int index = 0;

                if (bt.size() > 0) {
                    for (BluetoothDevice device : bt) {
                        btArray[index] = device;
                        strings[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, strings);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerClass serverClass = new ServerClass();
                serverClass.start();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                BluetoothSettings.ClientClass clientClass = new BluetoothSettings.ClientClass(btArray[i]);
                clientClass.start();

                status.setText("connecting");
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String string= String.valueOf(writemsg.getText());
                sendReceive.write(string.getBytes());
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch(msg.what)
            {
                case STATE_LISTENING:
                    status.setText("listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg = new String(readBuff,0,msg.arg1);
                    msg_box.setText(tempMsg);
                    break;
            }
            return true;
        }
    });

    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        public ServerClass()
        {
            try {
                serverSocket=BLTadapter.listenUsingRfcommWithServiceRecord(APP_NAME,MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run()
        {
            BluetoothSocket socket=null;

            while(socket==null)
            {
                try {
                    Message message = Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);

                    socket=serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }

                if(socket!=null)
                {
                    Message message = Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);

                    sendReceive=new SendReceive(socket);
                    sendReceive.start();

                    break;
                }

            }
        }
    }

    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass(BluetoothDevice device1)
        {
            device = device1;

            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run()
        {
            try {
                socket.connect();
                Message message = Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);

                sendReceive=new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }

    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive(BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn =bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream = tempIn;
            outputStream= tempOut;

        }
        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while(true)
            {
                ;
                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        public void write(byte[] bytes)
        {
            try{
                outputStream.write(bytes);
                outputStream.flush();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }


    private class Thread2 extends Thread
    {
        @Override
        public void run()
        {
            for(int i=0;i<50;i++)
            {

                Message message= Message.obtain();
                message.arg1=i;
                handler.sendMessage(message);

                try{
                    sleep(500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

}