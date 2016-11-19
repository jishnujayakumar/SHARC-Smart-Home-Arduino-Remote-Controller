package com.jishnu_jayakumar.embeddedsystems.sem7.iiitv.sharc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ToggleButton;

public class BluetoothActivity extends Activity implements OnClickListener,OnItemClickListener{

    ArrayAdapter<String> listAdapter;
    Button paired_devices, exit;
    ListView listView;
    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> setOfAvailableBluetoothDevices;
    ArrayList<String> pairedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        init();
        paired_devices.setOnClickListener(this);
        exit.setOnClickListener(this);

        //[1] Check if the device has a bluetooth device or not
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "No bluetooth detected", Toast.LENGTH_SHORT).show();
            finish(); //Finish the activity
        } else {
            if (!bluetoothAdapter.isEnabled()) {
               // Toast.makeText(getApplicationContext(), "bluetooth detected", Toast.LENGTH_SHORT).show();
                turnOnBT();
            }
            //Toast.makeText(getApplicationContext(), "getListOfPairedDevices", Toast.LENGTH_SHORT).show();

        }
        getListOfPairedDevices();

    }

    private void turnOnBT() {
        Intent intentForEnablingBluetooth = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intentForEnablingBluetooth, 1);
    }

    public void getListOfPairedDevices() {
        setOfAvailableBluetoothDevices = bluetoothAdapter.getBondedDevices();
        if (setOfAvailableBluetoothDevices.size() > 0) {

            for (BluetoothDevice device : setOfAvailableBluetoothDevices) {

                //listAdapter.add(device.getName() + "\n" + device.getAddress() + "\n" + device.toString());
                pairedDevices.add(device.getName() + "\n" + device.getAddress());
            }

        }
    }

    private void init() {
        //connectButton = (Button) findViewById(R.id.connect_button);
        listView = (ListView) findViewById(R.id.listView);
        paired_devices = (Button) findViewById(R.id.paired_devices);
        exit = (Button) findViewById(R.id.exit);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 0);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = new ArrayList<String>();

    }

        @Override
        public void onClick(View view) {

            if(view.getId()==R.id.paired_devices)
            {
               // Toast.makeText(this, "qqqqqqqqqqqqqqqqqqqqqqq", Toast.LENGTH_SHORT).show();
                for (BluetoothDevice device : setOfAvailableBluetoothDevices) {

                    listAdapter.add(device.getName() + "\n" + device.getAddress() + "\n" + device.toString());
                    pairedDevices.add(device.getName() + "\n" + device.getAddress());
                }


            }
            if(view.getId()==R.id.exit)
            {
                //Toast.makeText(this, "exit", Toast.LENGTH_SHORT).show();
                finish();
            }

        }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Object[] O = setOfAvailableBluetoothDevices.toArray();
            BluetoothDevice selectedDevice = (BluetoothDevice) O[i];

            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra("selectedDeviceAddress", selectedDevice.getAddress());
            startActivity(intent);

    }

}