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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    Button getLastLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLastLocationButton = findViewById(R.id.btnGetLastLocation);

        getLastLocationButton.setOnClickListener( view -> {
            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
            if (checkPermission()) {
                Task<Location> locationTask = client.getLastLocation();
                locationTask.addOnSuccessListener(this, this::onGetLocationSuccess);
            }
            else {
                System.out.println("permission denied");
            }
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
            toastMessage = "Latitude: " + location.getLatitude()
                        + "\nLongitude: " + location.getLongitude()
                        + "\nAltitude: " + location.getAltitude();
        }
        else {
            toastMessage = "No last known location found.";
        }
        Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
    }


}