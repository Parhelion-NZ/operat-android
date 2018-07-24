package nz.co.parhelion.operat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import java.util.ArrayList;
import java.util.List;

import nz.co.parhelion.operat.dto.Meshblock;
import nz.co.parhelion.operat.form.ViewPagerFragmentActivity;
import nz.co.parhelion.operat.service.OperatManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback/*OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener*/ {

    private GoogleMap mMap;

    public static final String LAT = "latitude";
    public static final String LNG = "longitude";

    double latitude;
    double longitude;

    OperatManager mgr = new OperatManager();

    Meshblock block;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(LAT, 0);
        longitude = intent.getDoubleExtra(LNG, 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                if (block == null) {
                    return;
                }
                Intent intent = new Intent(DetailsActivity.this, ViewPagerFragmentActivity.class);
                intent.putExtra("MESHBLOCK", block.getId());
                intent.putExtra("NUM_PROPERTIES", block.getAddresses().size());
                startActivity(intent);
            }
        });

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.details_map);
        //new OnMapAndViewReadyListener(mapFragment, this);
        System.out.println("Map fragment: "+mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18f));
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mgr.getMeshblock(new LatLng(latitude, longitude), new Callback<Meshblock>() {
            @Override
            public void onResponse(Call<Meshblock> call, Response<Meshblock> response) {
                try {
                    block = response.body();
                    PolygonOptions options = createPoly(block.getWkt());
                    options.fillColor(R.color.overlay);
                    mMap.addPolygon(options);

                    LatLngBounds.Builder builder = LatLngBounds.builder();

                    for (LatLng point : options.getPoints()) {
                        builder.include(point);
                    }

                    LatLngBounds bounds = builder.build();

                    int padding = 5; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    mMap.moveCamera(cu);
                    DetailsActivity.this.setTitle("Meshblock "+block.getId());

                    String display = "<h2>Meshblock "+block.getId()+"</h2>";
                    TextView detailsView = findViewById(R.id.details_text);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        detailsView.setText(Html.fromHtml(display, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        detailsView.setText(Html.fromHtml(display));
                    }

                    TextView numProps = (TextView)findViewById(R.id.details_no_of_properties);
                    numProps.setText(Integer.toString(block.getAddresses().size()));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Meshblock> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

//        addPolyObjects();
    }

    private PolygonOptions createPoly(String wkt) throws ParseException {
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

            polyOptions.strokeWidth(2);

            return polyOptions;
        }
        //TODO we're assuming that they'll always be a single poly... hopefully!!
        return null;
    }
}
