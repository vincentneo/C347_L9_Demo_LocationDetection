package sg.edu.rp.c347.id19007966.demoLocationDetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    Button getLastLocationButton, getLocationUpdateButton, removeLocationUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        getLastLocationButton = findViewById(R.id.btnGetLastLocation);
        getLocationUpdateButton = findViewById(R.id.btnGetLocationUpdate);
        removeLocationUpdateButton = findViewById(R.id.btnRemoveLocationUpdate);

        getLastLocationButton.setOnClickListener( view -> {
            if (checkPermission()) {
                Task<Location> locationTask = client.getLastLocation();
                locationTask.addOnSuccessListener(this, this::onGetLocationSuccess);
            }
            else {
                System.out.println("permission denied");
            }
        });

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(50);
        mLocationRequest.setSmallestDisplacement(0); // using 0 for testing so that i can see many calls for small location changes

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Toast.makeText(MainActivity.this, textFrom(location), Toast.LENGTH_SHORT).show();
            }
        };
        removeLocationUpdateButton.setEnabled(false);
        getLocationUpdateButton.setOnClickListener( view -> {
            client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            getLocationUpdateButton.setEnabled(false);
            removeLocationUpdateButton.setEnabled(true);
        });

        removeLocationUpdateButton.setOnClickListener(view -> {
            client.removeLocationUpdates(mLocationCallback);
            getLocationUpdateButton.setEnabled(true);
            removeLocationUpdateButton.setEnabled(false);
        });
    }

    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        }
        else {
            String[] fineLoc = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(MainActivity.this, fineLoc, 0);
            return (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                    || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED);
        }
    }

    private void onGetLocationSuccess(Location location) {
        String toastMessage;
        if (location != null) {
            toastMessage = textFrom(location);
        }
        else {
            toastMessage = "No last known location found.";
        }
        Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    private String textFrom(Location location) {
        return "Latitude: " + location.getLatitude()
                + "\nLongitude: " + location.getLongitude()
                + "\nAltitude: " + location.getAltitude();
    }


}