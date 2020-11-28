package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

class WifiReceiver extends BroadcastReceiver {

    WifiManager wifiManager;
    StringBuilder sb;
    ListView wifiDeviceList;


    ArrayList<String> lvl= new ArrayList<String>();
    ArrayList<String> frq = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<String>();
    private Object Comparator;


    public WifiReceiver(WifiManager wifiManager, ListView wifiDeviceList) {
        this.wifiManager = wifiManager;
        this.wifiDeviceList = wifiDeviceList;
    }

    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
            sb = new StringBuilder();
            List<ScanResult> wifiList = wifiManager.getScanResults();

            ArrayList<String> deviceList = new ArrayList<>();
            Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
                public int compare(ScanResult lhs, ScanResult rhs) {
                    return WifiManager.compareSignalLevel(rhs.level, lhs.level);
                }


            };
            for (ScanResult scanResult : wifiList) {

                sb.append("\n").append(scanResult.SSID).append(" - ").append(scanResult.capabilities);
                deviceList.add(scanResult.SSID);

                lvl.add(String.valueOf(scanResult.level));
                frq.add(String.valueOf(scanResult.frequency));

                names.add(scanResult.SSID);

                Collections.sort(deviceList);

            }


          //

            ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, deviceList.toArray());
            wifiDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(context,calculateDistance(Integer.parseInt(lvl.get(i)),Integer.parseInt(frq.get(i)),names.get(i)),Toast.LENGTH_SHORT).show();
                }


            });

            wifiDeviceList.setAdapter(arrayAdapter);
        }
    }
    public String calculateDistance(double levelInDb, double freqInMHz,String name)    {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
        return name+" : "+String.valueOf(Math.pow(10.0, exp));
    }

}
