package com.codinginflow.conexobluetoothandroid;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.Set;

public class ListaDispositivos extends ListActivity {

    private BluetoothAdapter mBlueAdapter = null;
    static String ENDERECO_MAC = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> dispositivosPareados = mBlueAdapter.getBondedDevices();

        if (dispositivosPareados.size() > 0) {
            for (BluetoothDevice dispositivos : dispositivosPareados) {
         String nomeBt = dispositivos.getName();
         String macBT = dispositivos.getAddress();
         ArrayBluetooth.add(nomeBt + "\n" + macBT);
            }
        }
        setListAdapter(ArrayBluetooth);

    }

}
