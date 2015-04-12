package pl.parkujznami.parkujpl_mobile.network;

import java.util.List;

import pl.parkujznami.parkujpl_mobile.models.city.RespondedCity;
import pl.parkujznami.parkujpl_mobile.models.parking.ResponseWithParking;
import pl.parkujznami.parkujpl_mobile.models.report.RequestForReport;
import pl.parkujznami.parkujpl_mobile.models.report.ResponseWithReport;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Marcin on 2015-04-11.
 */
public interface Client {

    @Headers("Content-Type: application/json")
    @GET("/api/v1/cities")
    List<RespondedCity> cities();

    @Headers("Content-Type: application/json")
    @GET("/api/v1/parkings/search")
    ResponseWithParking parking(
            @Query("city_id") Integer cityId,
            @Query("destination") String destination,
            @Query("max_parking_time_max") Double maxParkingTimeMax,
            @Query("min_parking_time_min") Double minParkingTimeMin,
            @Query("price_min") Double priceMin,
            @Query("price_max") Double priceMax,
            @Query("radius") Double radius,
            @Query("spots_min") Integer spotsMin,
            @Query("spots_max") Integer spotsMax
    );

    @Headers("Content-Type: application/json")
    @POST("/api/v1/parkings/{id}/reports")
    ResponseWithReport report(
            @Path("id") int parkingId,
            @Body RequestForReport requestForReport
    );
}

