package com.example.retbutt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button getlocation,opncamera;
    EditText edlocation;
    TextView tvlongitude, tvlattitude, tvlocality;
    private LocationManager manager;
    private LocationListener listener;
    private Double lng, latt;
    String cityName;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getlocation = (Button) findViewById(R.id.getlocation);
        opncamera=(Button)findViewById(R.id.opncam);
        tvlongitude = (TextView) findViewById(R.id.longitude);
        tvlattitude = (TextView) findViewById(R.id.lattitude);
        tvlocality = (TextView) findViewById(R.id.locality);
        edlocation=(EditText)findViewById(R.id.edlocation);



     getlocation.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             manager = (LocationManager) getSystemService(LOCATION_SERVICE);
             listener = new LocationListener() {
                 @Override
                 public void onLocationChanged(Location location) {

                     //calculate lattitude and longitude
                     lng = location.getLongitude();
                     latt = location.getLatitude();

                     tvlongitude.setText(lng.toString());
                     tvlattitude.setText(latt.toString());

                     if(manager!=null){
                         manager.removeUpdates(listener);
                     }

                     //get the address from the lattitude and longitude
                     Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                     try {
                          addresses=geocoder.getFromLocation(latt,lng,1);
                         Log.d("mylog","complete addresses:"+addresses.toString());

                         //complete address
                         String address=addresses.get(0).getAddressLine(0);
                         Log.d("mylog","addresse:"+address);


                         //city name
                         cityName=addresses.get(0).getLocality();
                         Log.d("mylog","city Name:"+cityName);
                         tvlocality.setText(cityName);

                     } catch (IOException e) {
                         e.printStackTrace();
                     }


                 }

                 @Override
                 public void onStatusChanged(String provider, int status, Bundle extras) {

                 }

                 @Override
                 public void onProviderEnabled(String provider) {

                 }

                 @Override
                 public void onProviderDisabled(String provider) {
                     Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                     startActivityForResult(intent, 1);

                 }
             };

             javapermission();



         }
     });

     //To accessing the camera
    opncamera.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(cityName.equalsIgnoreCase(edlocation.getText().toString())){
                Intent intent1=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1,999);
            }
            else{
                Toast.makeText(MainActivity.this,"Go to your respective location",Toast.LENGTH_SHORT).show();
            }
        }
    });


    }


    //create function for the java permission
    private void javapermission() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                return;
            }

        }

        manager.requestLocationUpdates("gps", 5000, 0, listener);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            javapermission();
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

    }
}

