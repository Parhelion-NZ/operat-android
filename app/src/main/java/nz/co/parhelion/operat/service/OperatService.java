package nz.co.parhelion.operat.service;

import java.util.List;

import nz.co.parhelion.operat.dto.Meshblock;
import nz.co.parhelion.operat.dto.Result;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OperatService {

    @GET("results/{lowerLeft}/{upperRight}/")
    Call<List<Result>> listResults(@Path("lowerLeft") String lowerLeft, @Path("upperRight") String upperRight);


    @GET("meshblock")//?lat={latitude}&lng={longitude}
    Call<Meshblock> getMeshblock(@Query("lat") double latitude, @Query("lng") double longitude);

}
