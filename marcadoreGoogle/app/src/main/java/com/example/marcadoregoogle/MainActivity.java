package com.example.marcadoregoogle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.marcadoregoogle.Models.DatosUniversidad;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    GoogleMap googleMap;
    String jsonFileString = "";
    List<DatosUniversidad> uteq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarDatos();
        genrFragment();

    }

    private void positioinMaps() {
        for (int u = 0; u < uteq.size(); u++) {
            Log.i("Logs", "Item " + u + "\n" + uteq.get(u).toString());
            pointmarkers(new LatLng
                    (uteq.get(u).getLatitude(),
                            uteq.get(u).getLongitude()),
                    uteq.get(u).toString());
        }
    }

    private void cargarDatos() {
        jsonFileString = loadJSONFromAsset("uteq.json");
        Gson gson = new Gson();

        Type type = new TypeToken<List<DatosUniversidad>>() {
        }.getType();

        uteq = gson.fromJson(jsonFileString, type);
    }

    private void genrFragment() {
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().
                        findFragmentById(R.id.framntContenedor);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap paramgoogleMap) {
        googleMap = paramgoogleMap;

        googleMap.setInfoWindowAdapter(new GoogleAdapter(MainActivity.this));
        googleMap.setOnMarkerClickListener(this);

        CameraPosition cameraPosition = new
    CameraPosition.Builder().target(new LatLng(-1.012825525175467, -79.46956324737249)).
                zoom(15).bearing(0).tilt(0).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.animateCamera(cameraUpdate);
        positioinMaps();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    private void pointmarkers(LatLng points, String data) {
        MarkerOptions opMark = new MarkerOptions().position(points).draggable(true).title(data);
        Marker marker = googleMap.addMarker(opMark);
        marker.setTag(jsonFileString);
    }

    public String loadJSONFromAsset(String flName) {
        String jsn = null;
        try {
            InputStream is = this.getAssets().open(flName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsn = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsn;
    }
}