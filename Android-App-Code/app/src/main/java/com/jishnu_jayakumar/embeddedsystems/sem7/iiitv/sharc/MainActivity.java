package com.jishnu_jayakumar.embeddedsystems.sem7.iiitv.sharc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int code;
    final Context context = this;
    private String password = "";
    private String Original_password = "@string/password";
    UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothSocket mBluetoothSocket;
    private BluetoothDevice bluetoothDevice;
    private BluetoothAdapter bluetoothAdapter;
    private String selectedDeviceAddress;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    Switch doorSwitch, fan_switch, light_switch, blind_switch, automate_switch;
    Button exit,automate_button;
    Handler bluetoothIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedDeviceAddress = (String) getIntent().getSerializableExtra("selectedDeviceAddress");
        code = 353;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Toast.makeText(getApplicationContext(), "Sending " + code + " | Selected address : " + selectedDeviceAddress, Toast.LENGTH_SHORT).show();
        findSelectedDevice();
        Toast.makeText(getApplicationContext(), "Sending " + code + " | Selected address : " + bluetoothDevice.getName(), Toast.LENGTH_SHORT).show();
        exit = (Button) findViewById(R.id.exit);
        automate_button = (Button) findViewById(R.id.automate_button);
        doorSwitch = (Switch) findViewById(R.id.door_switch);
        fan_switch = (Switch) findViewById(R.id.fan_switch);
        light_switch =(Switch) findViewById(R.id.light_switch);
        blind_switch = (Switch) findViewById(R.id.blind_switch);
        automate_switch = (Switch) findViewById(R.id.automate_switch);
        exit.setOnClickListener(this);
        automate_button.setOnClickListener(this);

        doorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.prompts, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text
                                            password=userInput.getText().toString();

                                            if(password.equals(Original_password)){


                                                //Send the code (1) for door opening to arduino

                                                doorSwitch.setChecked(true);

                                                Toast.makeText(getApplicationContext(), "Send the code (0) for door opening to arduino", Toast.LENGTH_SHORT).show();

                                                connectedThread.write("1");

                                                Toast.makeText(getApplicationContext(), "connectedThread.write(\"0\");", Toast.LENGTH_SHORT).show();

                                            }else{

                                                //Send the code (0) for keeping the door close to arduino

                                                doorSwitch.setChecked(false);

                                                //Toast.makeText(getApplicationContext(), "Send the code (1) for keeping the door close to arduino", Toast.LENGTH_SHORT).show();

                                                connectedThread.write("0");
                                                Toast.makeText(getApplicationContext(), "BEFORE", Toast.LENGTH_SHORT).show();
                                                connectedThread.read();
                                                Toast.makeText(getApplicationContext(), "AFTER", Toast.LENGTH_SHORT).show();
                                                //Toast.makeText(getApplicationContext(), "connectedThread.write(\"1\");", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            doorSwitch.setChecked(false);
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }else{

                    //Close the door

                    connectedThread.write("0");

                }
                }


        });


        fan_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){

                    connectedThread.write("2");

                    Toast.makeText(getApplicationContext(), "Sending : 2" , Toast.LENGTH_SHORT).show();

                }else{

                    //Close the door

                    connectedThread.write("3");

                    Toast.makeText(getApplicationContext(), "Sending : 3" , Toast.LENGTH_SHORT).show();

                }
            }


        });

        light_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){

                    connectedThread.write("4");

                    Toast.makeText(getApplicationContext(), "Sending : 4" , Toast.LENGTH_SHORT).show();

                }else{

                    //Close the door

                    connectedThread.write("5");

                    Toast.makeText(getApplicationContext(), "Sending : 5" , Toast.LENGTH_SHORT).show();

                }
            }


        });

        blind_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){

                    connectedThread.write("6");

                    Toast.makeText(getApplicationContext(), "Sending : 6" , Toast.LENGTH_SHORT).show();

                }else{

                    //Close the door

                    connectedThread.write("7");

                    Toast.makeText(getApplicationContext(), "Sending : 7" , Toast.LENGTH_SHORT).show();

                }
            }


        });

        automate_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){

                    connectedThread.write("8");

                    Toast.makeText(getApplicationContext(), "Sending : 8" , Toast.LENGTH_SHORT).show();

                }else{

                    //Close the door

                    connectedThread.write("9");

                    Toast.makeText(getApplicationContext(), "Sending : 9" , Toast.LENGTH_SHORT).show();

                }
            }


        });

    }

    @Override

    public void onClick(View view) {


        if(view.getId()==R.id.exit)
        {
            //Toast.makeText(this, "exit", Toast.LENGTH_SHORT).show();
            finish();
            this.closeConnection();
        }else if(view.getId()==R.id.automate_button)
        {
            Toast.makeText(this, "Automate", Toast.LENGTH_SHORT).show();
            connectedThread.write("8");

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.connectToOtherDevice();
    }

    public void findSelectedDevice(){

        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {

            if(device.getAddress().equals(selectedDeviceAddress)){

                this.bluetoothDevice = device;

                break;

            }

        }

    }

    public void connectToOtherDevice(){

        Toast.makeText(getBaseContext(), "connectToOtherDevice", Toast.LENGTH_LONG).show();
        connectThread = new ConnectThread(bluetoothDevice, bluetoothAdapter);
        connectThread.start();
        mBluetoothSocket = connectThread.getMmSocket();
        connectedThread = new ConnectedThread(mBluetoothSocket);
        connectedThread.start();
        Toast.makeText(getBaseContext(), "connectedThread.start(); -> initiated", Toast.LENGTH_LONG).show();
    }

    public void closeConnection(){

        connectThread.cancel();

    }


    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
           /* byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    Toast.makeText(getApplicationContext(), "Response:"+readMessage , Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    break;
                }
            }*/
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
                Toast.makeText(getBaseContext(), "write bytes over BT connection via outstream", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }

        //write method
        public void read(){
            Toast.makeText(getApplicationContext(), "read001", Toast.LENGTH_SHORT).show();
            byte[] buffer = new byte[256];
            Toast.makeText(getApplicationContext(), "read001", Toast.LENGTH_SHORT).show();
            int bytes = 0;
            Toast.makeText(getApplicationContext(), "read001", Toast.LENGTH_SHORT).show();
            try {
                Toast.makeText(getApplicationContext(), "read1", Toast.LENGTH_SHORT).show();
                bytes = mmInStream.read(buffer);            //read bytes from input buffer
                Toast.makeText(getApplicationContext(), "read2", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "READ ERROR in read()", Toast.LENGTH_SHORT).show();
            }
            String readMessage = new String(buffer, 0, bytes);
            // Send the obtained bytes to the UI Activity via handler
            Toast.makeText(getApplicationContext(), "Response:"+readMessage , Toast.LENGTH_SHORT).show();
        }

    }

}
