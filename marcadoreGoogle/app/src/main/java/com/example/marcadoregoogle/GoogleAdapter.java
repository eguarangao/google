package com.example.marcadoregoogle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class GoogleAdapter implements GoogleMap.InfoWindowAdapter, Callback {
    Context context;
    ImageView img;
    TextView name;
    TextView decano;
    TextView direccion;
    TextView phone;
    TextView lat;
    TextView lotd;
    Marker markerGoogle;

    public GoogleAdapter(Context context) {
        this.context = context;
    }
    private void inicializar(View view) {
        img = view.findViewById(R.id.imgFacultad);
        name = view.findViewById(R.id.txtTitulo);
        decano = view.findViewById(R.id.txtDecano);
        direccion = view.findViewById(R.id.txtDireccion);
        phone = view.findViewById(R.id.txtPhone);
        lat = view.findViewById(R.id.txtLatitud);
        lotd = view.findViewById(R.id.txtAltitud);
    }

    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        Log.i("Logs", "getInfoWindow " + marker.getTitle());
        View view = LayoutInflater.from(context).inflate(R.layout.contenedor, null);

        inicializar(view);
        loadData(marker);

        return view;
    }
    private void loadData(Marker marker) {
        markerGoogle = marker;
        try {
            JSONObject json = new JSONObject(marker.getTitle());
            Log.i("Logs", marker.getTitle());

            name.setText(json.get("name").toString());
            decano.setText(json.get("authority").toString());
            direccion.setText(json.get("direction").toString());
            phone.setText(json.get("contact").toString());
            lat.setText("Lat: "+json.get("latitude").toString());
            lotd.setText("Long: "+json.get("longitude").toString());

            Picasso.get().load(json.get("logo").toString()).resize(200, 200).centerCrop().into(img, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Override
    public void onSuccess() {
        if (markerGoogle != null && markerGoogle.isInfoWindowShown()) {
            markerGoogle.hideInfoWindow();
            markerGoogle.showInfoWindow();
        }
    }

    @Override
    public void onError(Exception e) {

    }
}