package pl.parkujznami.parkujpl_mobile.network;

import android.content.Context;

import java.util.List;

import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.models.city.RespondedCity;
import pl.parkujznami.parkujpl_mobile.models.parkandride.WarsawParkAndRide;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

/**
 * Created by Marcin on 2016-01-12.
 */
public class WarsawParkAndRideApi {
    private static WarsawParkAndRideApiInterface sWarsawParkAndRideService;

    public static WarsawParkAndRideApiInterface getWarsawParkAndRideApiClient(Context context) {

        if (sWarsawParkAndRideService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            final String authorizationValue = "Basic Z2xvd2FtYToyRVRHc1o5Yg==";
                            request.addHeader("Authorization", authorizationValue);
                        }
                    })
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(context.getString(R.string.warsaw_park_and_ride_api_url))
                    .build();

            // Create an instance of our ApiClient API interface.
            sWarsawParkAndRideService = restAdapter.create(WarsawParkAndRideApiInterface.class);
        }

        return sWarsawParkAndRideService;
    }

    public interface WarsawParkAndRideApiInterface {
        @GET("/wfs/warszawa/parkAndRide")
        void parkings(
                @Query("circle") String circle,
                Callback<WarsawParkAndRide> callback
        );
    }
}
