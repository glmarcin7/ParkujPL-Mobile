package pl.parkujznami.parkujpl_mobile.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.activities.ParkingListActivity;
import pl.parkujznami.parkujpl_mobile.models.city.RespondedCity;
import pl.parkujznami.parkujpl_mobile.models.parking.Parking;
import pl.parkujznami.parkujpl_mobile.models.parking.ResponseWithParking;
import pl.parkujznami.parkujpl_mobile.network.ApiClient;
import pl.parkujznami.parkujpl_mobile.utils.GPS;
import pl.parkujznami.parkujpl_mobile.utils.Navigation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass that manages screen: Start.
 *
 * @author Marcin Głowacki
 */
public class StartFragment extends Fragment {

    public static final String CITY_ID = "CITY_ID";
    public static final String CITY_NAME = "CITY_NAME";
    public static final String SEARCHED_PHRASE = "SEARCHED_PHRASE";

    private Activity mActivity;
    @Bind(R.id.s_city_chooser)
    Spinner mCitiesChooser;
    @Bind(R.id.btn_navigate)
    Button mNavigateButton;
    @Bind(R.id.et_destination)
    EditText mDestinationEditText;
    @Bind(R.id.pb_loading)
    ProgressBar mLoadingProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_start, container, false);
        initialize(view);
        if (isInternet()) {
            findCities();
        } else {
            Toast.makeText(mActivity, R.string.enable_internet_toast_error_no_internet, Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private boolean isInternet() {
        //author: Squonk
        //http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
        boolean isWifiConnected = false;
        boolean isMobileConnected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo ni : networkInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    isWifiConnected = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    isMobileConnected = true;
        }
        return isWifiConnected || isMobileConnected;
    }

    private void initialize(View view) {
        ButterKnife.bind(this, view);

        mActivity = getActivity();

        mCitiesChooser.getBackground().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        mDestinationEditText.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
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
                Toast.makeText(mActivity, mActivity.getString(R.string.start_screen_toast_error_get_cities_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void activateButton() {
        mNavigateButton.setVisibility(View.VISIBLE);

        //start search on "done" key on keyboard
        mDestinationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)
                        || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    onNavigateClick();
                }
                return false;
            }
        });

        mLoadingProgressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_navigate)
    public void onNavigateClick() {
        if (mDestinationEditText != null
                && mDestinationEditText.getText() != null
                && !mDestinationEditText.getText().toString().isEmpty()) {
            startParkingListActivity();
        } else {
            if (GPS.enableGPS(mActivity)) {
                startLocating();
            }
        }
    }

    private void startLocating() {
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

    private void findParking(final Double latitude, final Double longitude) {

        ApiClient.getParkujPlApiClient(mActivity).parkings(
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
                                    mActivity,
                                    !parking.getAvailabilty().toString(mActivity).equals(mActivity.getString(R.string.parking_list_screen_item_value_tv_really_little_space))
                            );
                        } else {
                            Toast.makeText(mActivity, mActivity.getString(R.string.start_screen_toast_error_find_parking_fail), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(mActivity, mActivity.getString(R.string.start_screen_toast_error_find_parking_fail), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    private void startParkingListActivity() {
        RespondedCity respondedCity = (RespondedCity) ((HashMap<String, Object>) mCitiesChooser.getSelectedItem()).get("city");
        Integer cityId = respondedCity.getId();
        String cityName = respondedCity.getName() + ", ";

        Intent intent = new Intent(mActivity, ParkingListActivity.class);
        intent.putExtra(StartFragment.CITY_ID, cityId);
        intent.putExtra(StartFragment.CITY_NAME, cityName);
        intent.putExtra(StartFragment.SEARCHED_PHRASE, mDestinationEditText.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
}
