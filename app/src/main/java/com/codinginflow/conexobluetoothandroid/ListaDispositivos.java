package com.codinginflow.conexobluetoothandroid;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String informacaoGeral = ((TextView)v).getText().toString();
//Fixme utilizado para teste Teste*/
//        Toast.makeText(getApplicationContext(), "Info:" + informacaoGeral, Toast.LENGTH_LONG).show();

        String endercoMac = informacaoGeral.substring(informacaoGeral.length() - 17);
//Fixme utilizado para teste Teste*/
//        Toast.makeText(getApplicationContext(), "Info:" + endercoMac, Toast.LENGTH_LONG).show();

        Intent retornaMac = new Intent();
        retornaMac.putExtra(ENDERECO_MAC, endercoMac);
        setResult(RESULT_OK, retornaMac);
        finish();
    }
}
