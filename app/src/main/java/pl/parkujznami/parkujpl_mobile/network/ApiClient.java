package pl.parkujznami.parkujpl_mobile.network;

import android.content.Context;

import java.util.List;

import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.models.city.RespondedCity;
import pl.parkujznami.parkujpl_mobile.models.parking.ResponseWithParking;
import pl.parkujznami.parkujpl_mobile.models.report.RequestForReport;
import pl.parkujznami.parkujpl_mobile.models.report.ResponseWithReport;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Marcin on 2015-04-11.
 */
public class ApiClient {

    private static ParkujPlApiInterface sParkujPlService;

    public static ParkujPlApiInterface getParkujPlApiClient(Context context){

        if(sParkujPlService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(context.getString(R.string.api_url))
                    .build();

            // Create an instance of our ApiClient API interface.
            sParkujPlService = restAdapter.create(ParkujPlApiInterface.class);
        }

        return sParkujPlService;
    }

    public interface ParkujPlApiInterface {

        @Headers("Content-Type: application/json")
        @GET("/api/v1/cities")
        void cities(Callback<List<RespondedCity>> callback);

        @Headers("Content-Type: application/json")
        @GET("/api/v1/parkings/search")
        void parking(
                @Query("city_id") Integer cityId,
                @Query("destination") String destination,
                @Query("max_parking_time_max") Double maxParkingTimeMax,
                @Query("min_parking_time_min") Double minParkingTimeMin,
                @Query("price_min") Double priceMin,
                @Query("price_max") Double priceMax,
                @Query("radius") Double radius, //in km
                @Query("spots_min") Integer spotsMin,
                @Query("spots_max") Integer spotsMax,
                Callback<ResponseWithParking> callback
        );

        @Headers("Content-Type: application/json")
        @POST("/api/v1/parkings/{id}/reports")
        void report(
                @Path("id") int parkingId,
                @Body RequestForReport requestForReport,
                Callback<ResponseWithReport> callback
        );
    }
}

