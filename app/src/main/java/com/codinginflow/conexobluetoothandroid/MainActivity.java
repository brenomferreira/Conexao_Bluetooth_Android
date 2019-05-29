package com.codinginflow.conexobluetoothandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();

    /*ToDo:*/
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    //TextView mStatusBlueTv, mPairedTv;
    //ImageView mBlueIv;
    //Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn;

    BluetoothAdapter mBlueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*onClick() ViewHolder*/
        this.mViewHolder.turn_on = (Button) findViewById(R.id.onBtn);
        this.mViewHolder.turn_on.setOnClickListener(this);
        this.mViewHolder.turn_off = (Button) findViewById(R.id.offBtn);
        this.mViewHolder.turn_off.setOnClickListener(this);
        this.mViewHolder.discoverable = (Button) findViewById(R.id.discoverableBtn);
        this.mViewHolder.discoverable.setOnClickListener(this);
        this.mViewHolder.get_paired_devices = (Button) findViewById(R.id.pairedBtn);
        this.mViewHolder.get_paired_devices.setOnClickListener(this);

        /*TextView ViewHolder*/
        this.mViewHolder.mStatusBlueTv = (TextView) findViewById(R.id.statusBluetoothTv);
        this.mViewHolder.mPairedTv = (TextView) findViewById(R.id.pairedTv);


        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        //check if bluetooth is available or not
        if (mBlueAdapter == null) {
            this.mViewHolder.mStatusBlueTv.setText("Bluetooth is not available");
        } else {
            this.mViewHolder.mStatusBlueTv.setText("Bluetooth is available");
        }

//fixme
//        //set image according to bluetooth status(on/off)
//        if (mBlueAdapter.isEnabled()){
//            mBlueIv.setImageResource(R.drawable.ic_action_on);
//        }
//        else {
//            mBlueIv.setImageResource(R.drawable.ic_action_off);
//        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.onBtn) {
            if (!mBlueAdapter.isEnabled()) {
                showToast(getString(R.string.Turning_on_BT)); //function
                //intent to on bluetooth
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            } else {
                showToast(getString(R.string.BT_is_already_on)); // function
            }
        }
        if (id == R.id.offBtn) {

            if (mBlueAdapter.isEnabled()) {
                mBlueAdapter.disable();
                showToast(getString(R.string.Turning_BT_Off));
//                mBlueIv.setImageResource(R.drawable.ic_action_off); // Imagem
            } else {
                showToast(getString(R.string.BT_is_already_off));
            }

        }
        if (id == R.id.discoverableBtn) {

            if (!mBlueAdapter.isDiscovering()) {
                showToast(getString(R.string.Making_Your_Device_Discoverable));
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(intent, REQUEST_DISCOVER_BT);
            }

        }
        if (id == R.id.pairedBtn) {

            if (mBlueAdapter.isEnabled()) {
                this.mViewHolder.mPairedTv.setText("Paired Devices");
                Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                for (BluetoothDevice device : devices) {
                    this.mViewHolder.mPairedTv.append("\nDevice: " + device.getName() + ", " + device);
                }
            } else {
                //bluetooth is off so can't get paired devices
                showToast("Turn on bluetooth to get paired devices");
            }


        }// fim pairedBTN


    }

    private static class ViewHolder {
        Button turn_on;
        Button turn_off;
        Button discoverable;
        Button get_paired_devices;
        TextView mStatusBlueTv;
        TextView mPairedTv;

    } // Fim ViewHolder

//fixme
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_ENABLE_BT:
//                if (resultCode == RESULT_OK) {
//                    //bluetooth is on
//                    mBlueIv.setImageResource(R.drawable.ic_action_on);
//                    showToast("Bluetooth is on");
//                } else {
//                    //user denied to turn bluetooth on
//                    showToast("could't on bluetooth");
//                }
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }


    /*ToDo:*/
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}


