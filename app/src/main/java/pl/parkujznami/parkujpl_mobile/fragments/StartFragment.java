package pl.parkujznami.parkujpl_mobile.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.activities.ParkingListActivity;
import pl.parkujznami.parkujpl_mobile.models.city.RespondedCity;
import pl.parkujznami.parkujpl_mobile.models.parking.Parking;
import pl.parkujznami.parkujpl_mobile.models.parking.ResponseWithParking;
import pl.parkujznami.parkujpl_mobile.models.shared.Coords;
import pl.parkujznami.parkujpl_mobile.network.ApiClient;
import pl.parkujznami.parkujpl_mobile.utils.GPS;
import pl.parkujznami.parkujpl_mobile.utils.Navigation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment implements Button.OnClickListener {

    public static final String CITY_ID = "CITY_ID";
    public static final String SEARCHED_PHRASE = "SEARCHED_PHRASE";
    private Activity mActivity;
    private Spinner mCitiesChooser;
    private Button mNavigateButton;
    private EditText mDestinationEditText;
    private ProgressBar mLoadingProgressBar;


    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_start, container, false);
        initialize(view);
        findCities();
        return view;
    }

    private void initialize(View view) {
        mActivity = getActivity();

        // Initialize
        mCitiesChooser = (Spinner) view.findViewById(R.id.s_city_chooser);

        mNavigateButton = (Button) view.findViewById(R.id.btn_navigate);

        mDestinationEditText = (EditText) view.findViewById(R.id.et_destination);

        mLoadingProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
    }

    private void findCities() {
        ApiClient.getParkujPlApiClient(mActivity).cities(new Callback<List<RespondedCity>>() {
            @Override
            public void success(List<RespondedCity> respondedCities, Response response) {

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
                        mActivity,
                        fillMaps,
                        R.layout.spinner_dropdown_item,
                        from,
                        to
                );

                mCitiesChooser.setAdapter(spinnerArrayAdapter);
                mCitiesChooser.setVisibility(View.VISIBLE);

                activateButton();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mActivity, mActivity.getString(R.string.getting_available_cities_failed), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void activateButton() {
        mNavigateButton.setOnClickListener(this);
        mNavigateButton.setVisibility(View.VISIBLE);

        //start search on "done" key on keyboard
        mDestinationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)
                        || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    onClick(mNavigateButton);
                }
                return false;
            }
        });

        mLoadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_navigate:
                if (mDestinationEditText != null
                        && mDestinationEditText.getText() != null
                        && !mDestinationEditText.getText().toString().isEmpty()) {
                    startParkingListActivity();
                } else {
                    GPS.enableGPS(mActivity);
                    SmartLocation smartLocation = new SmartLocation.Builder(mActivity).logging(true).build();
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
                }
                break;
        }
    }

    private void findParking(Double latitude, Double longitude) {
        ApiClient.getParkujPlApiClient(mActivity).parking(
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
                            Parking parking = parkings.get(0);
                            Navigation.startNavigation(
                                    parking.getId(),
                                    parking.getCoords(),
                                    mActivity);
                        } else {
                            Toast.makeText(mActivity, mActivity.getString(R.string.find_parking_fail), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //Toast.makeText(mActivity, mActivity.getString(R.string.findNearestParkingFail), Toast.LENGTH_LONG).show();

                        //Tylko do testów
                        Coords coords = new Coords();
                        coords.setLatitude("52.40622836");
                        coords.setLongitude("16.92763567");
                        Navigation.startNavigation(0, coords, mActivity);
                    }
                }
        );
    }


    private void startParkingListActivity() {
        Integer cityId = ((RespondedCity) ((HashMap<String, Object>) mCitiesChooser.getSelectedItem()).get("city")).getId();
        Intent intent = new Intent(mActivity, ParkingListActivity.class);
        intent.putExtra(StartFragment.CITY_ID, cityId);
        intent.putExtra(StartFragment.SEARCHED_PHRASE, mDestinationEditText.getText().toString());
        startActivity(intent);
    }
}
