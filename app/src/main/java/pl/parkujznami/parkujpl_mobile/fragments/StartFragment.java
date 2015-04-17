package pl.parkujznami.parkujpl_mobile.fragments;


import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.activities.GetPointsActivity;
import pl.parkujznami.parkujpl_mobile.models.city.RespondedCity;
import pl.parkujznami.parkujpl_mobile.models.parking.Parking;
import pl.parkujznami.parkujpl_mobile.models.parking.ResponseWithParking;
import pl.parkujznami.parkujpl_mobile.models.shared.Coords;
import pl.parkujznami.parkujpl_mobile.network.ApiClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment implements Button.OnClickListener {

    public static final String CITY_ID = "CITY_ID";
    private Spinner mCitiesChooser;

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_start, container, false);

        ApiClient.getParkujPlApiClient(getActivity()).cities(new Callback<List<RespondedCity>>() {
            @Override
            public void success(List<RespondedCity> respondedCities, Response response) {
                mCitiesChooser = (Spinner) view.findViewById(R.id.s_city_chooser);

                String[] from = new String[]{"text", "city"};
                int[] to = new int[]{android.R.id.text1};
                List<HashMap<String, Object>> fillMaps = new ArrayList<>();
                for (RespondedCity respondedCity : respondedCities) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("text", respondedCity.getName());
                    map.put("city", respondedCity);
                    fillMaps.add(map);
                }

                SimpleAdapter spinnerArrayAdapter = new SimpleAdapter(
                        getActivity(),
                        fillMaps,
                        android.R.layout.simple_spinner_dropdown_item,
                        from,
                        to
                );

                mCitiesChooser.setAdapter(spinnerArrayAdapter);
                mCitiesChooser.setVisibility(View.VISIBLE);

                activateButtons(view);
            }

            @Override
            public void failure(RetrofitError error) {
                Context context = getActivity();
                Toast.makeText(context, context.getString(R.string.gettingAvailableCitiesFailed), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void activateButtons(View view) {
        Button leadToNearestParkingButton = (Button) view.findViewById(R.id.btn_lead_to_nearest_parking);
        leadToNearestParkingButton.setOnClickListener(this);
        leadToNearestParkingButton.setEnabled(true);

        Button leadToAPointButton = (Button) view.findViewById(R.id.btn_lead_to_a_point);
        leadToAPointButton.setOnClickListener(this);
        leadToAPointButton.setEnabled(true);

        Button showMapButton = (Button) view.findViewById(R.id.btn_show_map);
        showMapButton.setOnClickListener(this);
        showMapButton.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_lead_to_nearest_parking:
                SmartLocation smartLocation = new SmartLocation.Builder(getActivity()).logging(true).build();
                smartLocation
                        .location()
                        .oneFix()
                        .config(LocationParams.NAVIGATION)
                        .start(new OnLocationUpdatedListener() {
                            @Override
                            public void onLocationUpdated(Location location) {
                                Timber.d("OneTimeLocationUpdate");
                                findParking(location.getLatitude(), location.getLongitude());
                                //findParking(52.39792823, 16.9029808);
                            }
                        });
                break;
            case R.id.btn_lead_to_a_point:
                startGetPointsActivity();
                break;
        }
    }

    private void findParking(Double latitude, Double longitude) {
        ApiClient.getParkujPlApiClient(getActivity()).parking(
                ((RespondedCity) ((HashMap<String, Object>) mCitiesChooser.getSelectedItem()).get("city")).getId(),
                latitude + "," + longitude,
                1500.0,
                0.0,
                0.0,
                10.0,
                50.0, //in km
                0,
                1500,
                new Callback<ResponseWithParking>() {
                    @Override
                    public void success(ResponseWithParking responseWithParking, Response response) {
                        List<Parking> parkings = responseWithParking.getParkings();
                        if (parkings != null && !parkings.isEmpty()) {
                            Coords coords = parkings.get(0).getCoords();
                            startNavigation(coords);
                        } else {
                            Context context = getActivity();
                            Toast.makeText(context, context.getString(R.string.findNearestParkingFail), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Context context = getActivity();
                        Toast.makeText(context, context.getString(R.string.findNearestParkingFail), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void startNavigation(Coords coords) {
        Uri gmmIntentUri = Uri.parse(
                "google.navigation:q="
                + coords.getLatitude()
                + ","
                + coords.getLongitude()
        );
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(mapIntent);
            startNavigationStopService(coords);
        } catch (ActivityNotFoundException e) {
            Timber.i("Brak zainstalowanych map google");
            //TODO
        }
    }

    private void startNavigationStopService(final Coords coords) {
        final SmartLocation smartLocation = new SmartLocation.Builder(getActivity()).logging(true).build();
        smartLocation
                .location()
                .config(LocationParams.NAVIGATION)
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        Timber.d("LocationUpdate");
                        if(onPosition(location, coords, 0.1)){
                            bringToForeground();
                            smartLocation.location().stop();
                        }
                    }
                });
    }

    private void bringToForeground() {
        Timber.i("OnPosition");
    }

    /**
     *
     * @param myLocation - my location
     * @param parkingsLocation - parking's location
     * @param accuracy - in km
     * @return
     */
    private boolean onPosition(Location myLocation, Coords parkingsLocation, Double accuracy) {
        if(distance(
                myLocation.getLatitude(),
                myLocation.getLongitude(),
                Double.parseDouble(parkingsLocation.getLatitude()),
                Double.parseDouble(parkingsLocation.getLongitude())
        ) <= accuracy){
            return true;
        }
        return false;
    }

    /**
     * Calculate distance between two points
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return distance in km
     */
    private Double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.853159616;
        return dist;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void startGetPointsActivity() {
        Integer cityId = ((RespondedCity) ((HashMap<String, Object>) mCitiesChooser.getSelectedItem()).get("city")).getId();
        Intent intent = new Intent(getActivity(), GetPointsActivity.class);
        intent.putExtra(StartFragment.CITY_ID, cityId);
        startActivity(intent);
    }
}
