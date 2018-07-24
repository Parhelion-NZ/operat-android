package nz.co.parhelion.operat.service;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.vividsolutions.jts.geom.Coordinate;

import java.io.IOException;
import java.util.List;

import nz.co.parhelion.operat.dto.Meshblock;
import nz.co.parhelion.operat.dto.Result;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OperatManager {

    public void getResults(LatLngBounds bounds, Callback<List<Result>> callback) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://operat.co.nz/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OperatService service = retrofit.create(OperatService.class);

        double llLng = bounds.southwest.longitude;
        double llLat = bounds.southwest.latitude;

        double urLng = bounds.northeast.longitude;
        double urLat = bounds.northeast.latitude;

        String ll = llLng + "," + llLat;
        String ur = urLng + "," + urLat;

        service.listResults(ll, ur).enqueue(callback);

    }

    public void getMeshblock(LatLng point, Callback<Meshblock> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://operat.co.nz/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OperatService service = retrofit.create(OperatService.class);

        service.getMeshblock(point.latitude, point.longitude).enqueue(callback);

    }

    public List<Result> getResults(Coordinate lowerLeft, Coordinate upperRight) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:82/operat/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OperatService service = retrofit.create(OperatService.class);

        double llLng = lowerLeft.x;
        double llLat = lowerLeft.y;

        double urLng = upperRight.x;
        double urLat = upperRight.y;

        String ll = llLng + "," + llLat;
        String ur = urLng + "," + urLat;

        try {
            return service.listResults(ll, ur).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
