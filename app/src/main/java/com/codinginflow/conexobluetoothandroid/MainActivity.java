package com.codinginflow.conexobluetoothandroid;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*ToDo:*/
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private static final int SOLICITA_CONEXAO = 2;
    ConnectedThread connectedThread;

    private static String MAC = null;


    //TextView mStatusBlueTv, mPairedTv;
    //ImageView mBlueIv;
    //Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn;
    BluetoothAdapter mBlueAdapter;
    BluetoothDevice mDevice;
    BluetoothSocket mSocket;

    UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    boolean conexao = false;
    private ViewHolder mViewHolder = new ViewHolder();

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
        this.mViewHolder.conectar = (Button) findViewById(R.id.conect);
        this.mViewHolder.conectar.setOnClickListener(this);



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

        if (id == R.id.conect) {
            if (conexao) {
                /*desconectar*/
                try{
                    mSocket.close();
                    conexao = false;

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "ocorreu um erro: " + e, Toast.LENGTH_LONG).show();
                }
            } else {
                /*conectar*/
                Intent abrelista = new Intent(MainActivity.this, ListaDispositivos.class);
                startActivityForResult(abrelista, SOLICITA_CONEXAO);


            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case SOLICITA_CONEXAO:
                if (resultCode == Activity.RESULT_OK) {
                    MAC = data.getExtras().getString(ListaDispositivos.ENDERECO_MAC);
                    Toast.makeText(getApplicationContext(), "MAC: " + MAC, Toast.LENGTH_LONG).show();
                    mDevice = mBlueAdapter.getRemoteDevice(MAC);
                    try {
                        /*"00001101-0000-1000-8000-00805f9b34fb"*/
                        mSocket = mDevice.createRfcommSocketToServiceRecord(myUUID); // criar canal de comunicação
                        mSocket.connect();

                        conexao = true;

                        connectedThread = new ConnectedThread(mSocket);
                        connectedThread.start();

/*
                        FIXME: ENVIAR A MENSAGEM APÓS CONECTAR
*/
                        connectedThread.enviar("Conectado");



                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro " + e, Toast.LENGTH_LONG).show();
                conexao = false;
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Falha ao obter o MAC", Toast.LENGTH_LONG).show();
                }


        }
    }

    /*ToDo:*/
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


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

    private static class ViewHolder {
        Button turn_on;
        Button turn_off;
        Button discoverable;
        Button get_paired_devices;
        Button conectar;
        TextView mStatusBlueTv;
        TextView mPairedTv;

    } // Fim ViewHolder

    private class ConnectedThread extends Thread {
        // private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
//            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
//            while (true) {
//                try {
//                    // Read from the InputStream
//                    bytes = mmInStream.read(buffer);
//                    // Send the obtained bytes to the UI activity
//                    /*FIXME: por enquanto ainda nao esta sendo utilizado (responsavel por receber dados)*/
//                    /* mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();*/
//                } catch (IOException e) {
//                    break;
//                }
//            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void enviar(String dadosEnviar) {
            byte[] msgBuffer = dadosEnviar.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {

            }
        }
//FIXME
//        /* Call this from the main activity to shutdown the connection */
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) { }
//        }
    }





}




