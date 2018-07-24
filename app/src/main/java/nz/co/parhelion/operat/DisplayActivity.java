package nz.co.parhelion.operat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.vividsolutions.jts.io.ParseException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nz.co.parhelion.operat.dto.Result;
import nz.co.parhelion.operat.model.DialogMenu;
import nz.co.parhelion.operat.model.MeshblockPolygon;
import nz.co.parhelion.operat.service.OperatManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private OperatManager service = new OperatManager();

    private static final String TAG = "MainActivity";

    public enum Scores {
        OPERAT, INCIVILITIES, NATURAL_ELEMENTS, TERRITORIAL, NAVIGATION
    };

    private Scores showingScores = Scores.OPERAT;

    private Map<Integer, MeshblockPolygon> onScreenPolygons;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
//        tb.inflateMenu(R.menu.scores_menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.scores_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getApplicationContext(), "Option selected", Toast.LENGTH_SHORT).show();
        int resId = item.getItemId();
        showPolys(resId);
        return true;
    }

    private void showPolys(int resId) {
        switch (resId) {
            case R.id.action_incivilities_score:
                showingScores = Scores.INCIVILITIES;
                break;
            case R.id.action_navigation_score:
                showingScores = Scores.NAVIGATION;
                break;
            case R.id.action_operat_score:
                showingScores = Scores.OPERAT;
                break;
        }

        mMap.clear();
        for (MeshblockPolygon poly : onScreenPolygons.values()) {
            poly.showScore(showingScores);
            mMap.addPolygon(poly.getPolygon());
        }
    }

    private void showPolys() {
        mMap.clear();
        for (MeshblockPolygon poly : onScreenPolygons.values()) {
            poly.showScore(showingScores);
            mMap.addPolygon(poly.getPolygon());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Operat");
        setContentView(R.layout.activity_display);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.score_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call our Dialogmenu
                DialogMenu dialogMenu = new DialogMenu(DisplayActivity.this);
                dialogMenu.setListener(new DialogMenu.OnDialogMenuListener() {
                    @Override
                    public void onOperatPress() {
                        showingScores = Scores.OPERAT;
                        showPolys();
                    }

                    @Override
                    public void onNaturalElementsPress() {
                        showingScores = Scores.NATURAL_ELEMENTS;
                        showPolys();
                    }

                    @Override
                    public void onIncivilitiesPress() {
                        showingScores = Scores.INCIVILITIES;
                        showPolys();
                    }

                    @Override
                    public void onTerritorialPress() {
                        showingScores = Scores.TERRITORIAL;
                        showPolys();
                    }

                    @Override
                    public void onNavigationPress() {
                        showingScores = Scores.NAVIGATION;
                        showPolys();
                    }


                });
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        onScreenPolygons = new HashMap<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapStyle(
                MapStyleOptions
                        .loadRawResourceStyle(
                        this, R.raw.map_style_json));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            LatLng wgtn = new LatLng(-41.290675, 174.752887);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(wgtn));
        }

        mMap.moveCamera(CameraUpdateFactory.zoomTo( 17.0f ));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(DisplayActivity.this, DetailsActivity.class);

                intent.putExtra(DetailsActivity.LAT, latLng.latitude);
                intent.putExtra(DetailsActivity.LNG, latLng.longitude);
                startActivity(intent);
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                service.getResults(bounds, new Callback<List<Result>>() {
                    @Override
                    public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {

//                        Toast.makeText(DisplayActivity.this, "Got "+response.body().size()+" results!", Toast.LENGTH_SHORT).show();
                        List<Result> results = response.body();

                        Set<Integer> keys = new HashSet<>(onScreenPolygons.keySet());

                        for (Result result : response.body()) {
                            try {
                                if (onScreenPolygons.containsKey(result.getResultId())) {
                                    keys.remove(result.getResultId());
                                } else {
                                    MeshblockPolygon poly = new MeshblockPolygon(result);
                                    onScreenPolygons.put(result.getResultId(), poly);
                                    poly.showScore(showingScores);
                                    mMap.addPolygon(poly.getPolygon());
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        for (int extraneous : keys) {
                            onScreenPolygons.remove(extraneous);
                        }
//                        Toast.makeText(getApplicationContext(), "Now have "+onScreenPolygons.size()+" polys", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<List<Result>> call, Throwable t) {
                        System.out.println("CSJM - failure" + call.toString());
                        Toast.makeText(DisplayActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });



            }

            public void draw(List<Result> results) {
                for (Result result : results) {
                    try {

                        MeshblockPolygon poly = new MeshblockPolygon(result);
                        poly.showNaturalElementsScore();
                        mMap.addPolygon(poly.getPolygon());

//                        drawPolygons(result.getGeom(), mMap);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }





    /*private void drawPolygons(String wkt, GoogleMap map) throws ParseException {
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader reader = new WKTReader(geometryFactory);
        PolygonOptions polyOptions = null;
        List<String> featureList = new ArrayList<>();
        Geometry geom = null;
        Polygon polygon = null;
        Coordinate[] outerCoordinates = null;
        Coordinate[] innerCoordinates = null;
        ArrayList<LatLng> outer = null;
        ArrayList<LatLng> inner = null;

        geom = reader.read(wkt);

        //Gets each polygon of a multipolygon
        for (int i = 0; i < geom.getNumGeometries(); i++) {
            outer = new ArrayList<LatLng>();
            polyOptions = new PolygonOptions();
            polygon = (Polygon) geom.getGeometryN(i);

            //Gets each polygon outer coordinates
            outerCoordinates = polygon.getExteriorRing().getCoordinates();
            for (Coordinate outerCoordinate : outerCoordinates) {
                outer.add(new LatLng(outerCoordinate.y, outerCoordinate.x));
            }
            polyOptions.addAll(outer);

            //Getting each polygon interior coordinates (hole)  if they exist
            if (polygon.getNumInteriorRing() > 0) {
                for (int j = 0; j < polygon.getNumInteriorRing(); j++) {
                    inner = new ArrayList<LatLng>();
                    innerCoordinates = polygon.getInteriorRingN(j).getCoordinates();
                    for (Coordinate innerCoordinate : innerCoordinates) {
                        inner.add(new LatLng(innerCoordinate.y, innerCoordinate.x));
                    }
                    polyOptions.addHole(inner);
                }

            }
            polyOptions.strokeColor(Color.rgb(30, 30, 30));
            polyOptions.strokeWidth(2);
            polyOptions.fillColor(Color.argb(255, 255, 0, 0));
            map.addPolygon(polyOptions);
        }
    }*/


}
