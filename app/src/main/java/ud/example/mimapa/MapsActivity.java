package ud.example.mimapa;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private final LatLng SAN = new LatLng(12.587151, -81.698713);
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng miUbicacion;
    private EditText latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        latitud = findViewById(R.id.latitud);
        longitud = findViewById(R.id.longitud);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try {
                    miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
                } catch (Exception Ex) {}
            }
        };
        //RevisarPermisos();
        //RequestPermission();
    }

    private void RevisarPermisos(){
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION)!=
            PackageManager.PERMISSION_GRANTED
            &&
        ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION)!=
        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,

            }, 101);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }
    }//Solicitud de permisos

    private void RequestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION)!=
                            PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                }, 101);
            }
            else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(4.6482837, -74.247894);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Colombia"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 5));
        mMap.setOnMapClickListener((GoogleMap.OnMapClickListener) this);
    }

    public void MapaNormal(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    public void MapaSatelite(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }
    public void MapaHibrido(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
    public void MapaNinguno(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
    }
    public void MapaTerracin(View v){
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    //private final LatLng SAN = new LatLng(12.587151, -81.698713);
    public void IrParaiso(View v){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SAN,15));
    }

    public void IrUD(View v){
        LatLng UD = new LatLng( 4.6281058, -74.0659999);
        Marker miMarker = mMap.addMarker(new MarkerOptions()
          .position(UD)
          .title("UD")
          .snippet("Universidad Distrital FJC"));
        miMarker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UD, 15));
    }

    public void iraUbicacion(View v){
        Double lat, lon;
        lat = Double.parseDouble(latitud.getText().toString());
        lon = Double.parseDouble(longitud.getText().toString());
        LatLng UD = new LatLng( lat , lon);
        Marker miMarker = mMap.addMarker(new MarkerOptions()
                .position(UD)
                .snippet("Lat("+latitud.getText().toString()+") Lon("+longitud.getText().toString()+")")
                .title("Ha llegado a su destino !!!"));
        miMarker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UD, 10));
    }

    public void Limpiar(View v){
        mMap.clear();
    }

    public void addMarker(View v){
        LatLng TempLatLng = mMap.getCameraPosition().target;
        mMap.addMarker(new MarkerOptions()
                .position(TempLatLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        );
    }

    public void iraMiUbicacion(View v){
        if (miUbicacion != null) {
            Marker miMarker = mMap.addMarker(new MarkerOptions()
                    .position(miUbicacion)
                    .title("Mi Ubicacion")
                    .snippet("Usted esta aqui"));
            miMarker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 17));
        }
    }
    @Override
    public void onMapClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng));
    }

}//cierre main activity