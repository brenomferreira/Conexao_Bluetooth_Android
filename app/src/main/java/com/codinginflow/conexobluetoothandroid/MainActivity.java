package com.codinginflow.conexobluetoothandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.onBtn) {
        }
        if (id == R.id.offBtn) {
        }
        if (id == R.id.discoverableBtn) {
        }
        if (id == R.id.pairedBtn) {
        }


    }

    private static class ViewHolder {
        Button turn_on;
        Button turn_off;
        Button discoverable;
        Button get_paired_devices;

    } // Fim ViewHolder

}
