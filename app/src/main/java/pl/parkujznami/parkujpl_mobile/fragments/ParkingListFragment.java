package pl.parkujznami.parkujpl_mobile.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.models.parking.Availabilty;
import pl.parkujznami.parkujpl_mobile.models.parking.Parking;
import pl.parkujznami.parkujpl_mobile.models.parking.ResponseWithParking;
import pl.parkujznami.parkujpl_mobile.models.shared.Coords;
import pl.parkujznami.parkujpl_mobile.network.ApiClient;
import pl.parkujznami.parkujpl_mobile.utils.Navigation;
import pl.parkujznami.parkujpl_mobile.views.adapters.ParkingAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParkingListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private Activity mActivity;
    private Integer mCityId;
    private String mCityName;
    private String mSearchedPhrase;
    private EditText mDestinationEditText;
    private ImageButton mImageButton;
    private ListView mListView;
    private Spinner mFiltersSpinner;
    private List<Parking> mParkings;

    public ParkingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_parking, container, false);
        initialize(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search();
    }

    private void initialize(View view) {
        mActivity = getActivity();

        Intent intent = mActivity.getIntent();
        mCityId = intent.getIntExtra(StartFragment.CITY_ID, -1);
        mCityName = intent.getStringExtra(StartFragment.CITY_NAME);
        mSearchedPhrase = intent.getStringExtra(StartFragment.SEARCHED_PHRASE);

        mImageButton = (ImageButton) view.findViewById(R.id.ib_look_for);
        mImageButton.setOnClickListener(this);

        mDestinationEditText = (EditText) view.findViewById(R.id.et_destination);
        mDestinationEditText.setText(mSearchedPhrase);
        //start search on "done" key on keyboard
        mDestinationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)
                        || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    onClick(mImageButton);
                }
                return false;
            }
        });

        mFiltersSpinner = (Spinner) view.findViewById(R.id.s_filters);
        mFiltersSpinner.setAdapter(new ArrayAdapter<>(
                mActivity,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.sa_filters)
        ));
        mFiltersSpinner.setOnItemSelectedListener(this);
        mFiltersSpinner.getBackground().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);


        mListView = (ListView) view.findViewById(R.id.lv_list_of_parking);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_look_for:
                search();
                break;
        }
    }

    private void search() {
        ApiClient.getParkujPlApiClient(mActivity).parkings(
                mCityId,
                mCityName + mDestinationEditText.getText().toString(),
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
                        mParkings = responseWithParking.getParkings();
                        if (mParkings != null && !mParkings.isEmpty()) {
                            Collections.sort(mParkings, getFilter());
                            mListView.setAdapter(new ParkingAdapter(
                                    mActivity,
                                    R.layout.list_view_with_parkings_item,
                                    mParkings
                            ));
                        } else {
                            Toast.makeText(mActivity, mActivity.getString(R.string.find_parking_fail), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //Toast.makeText(mActivity, mActivity.getString(R.string.findParkingFail), Toast.LENGTH_LONG).show();
                        Timber.i(mActivity.getString(R.string.find_parking_fail));

                        //Tylko do testów
                        Parking parking1 = new Parking();
                        parking1.setName("Dąbrowskiego 1");
                        parking1.setAddress("Dąbrowskiego 1");
                        Coords coords1 = new Coords();
                        coords1.setLatitude("52.41063962");
                        coords1.setLongitude("16.91310883");
                        parking1.setCoords(coords1);
                        Availabilty availabilty1 = new Availabilty();
                        availabilty1.setHigh(0);
                        availabilty1.setMedium(0);
                        availabilty1.setLow(1);
                        parking1.setAvailabilty(availabilty1);
                        parking1.setDistance("500m");
                        parking1.setPrice("5.0");
                        parking1.setId(1);

                        Parking parking2 = new Parking();
                        parking2.setName("Plac Wolności");
                        parking2.setAddress("Poznań, Plac Wolności 19");
                        Coords coords2 = new Coords();
                        coords2.setLatitude("52.41084904");
                        coords2.setLongitude("16.91186428");
                        parking2.setCoords(coords2);
                        Availabilty availabilty2 = new Availabilty();
                        availabilty2.setHigh(0);
                        availabilty2.setMedium(0);
                        availabilty2.setLow(1);
                        parking2.setAvailabilty(availabilty2);
                        parking2.setDistance("1200m");
                        parking2.setPrice("4.0");
                        parking2.setId(2);

                        mParkings = new ArrayList<>();
                        mParkings.add(parking1);
                        mParkings.add(parking2);
                        Collections.sort(mParkings, getFilter());
                        mListView.setAdapter(new ParkingAdapter(
                                mActivity,
                                android.R.layout.simple_list_item_1,
                                mParkings
                        ));
                    }
                }
        );
    }

    private Comparator<? super Parking> getFilter() {
        String selectedItem = (String) mFiltersSpinner.getSelectedItem();
        if (selectedItem.equals(mActivity.getString(R.string.s_costs_of_parking))) {
            return Parking.costsComparator;
        } else if (selectedItem.equals(mActivity.getString(R.string.s_number_of_free_spots))) {
            return Parking.spotsAvailabilityComparator;
        } else if (selectedItem.equals(mActivity.getString(R.string.s_distance_to_parking))) {
            return Parking.distanceComparator;
        }
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_list_of_parking:
                Parking selectedItem = (Parking) parent.getItemAtPosition(position);
                Timber.d("SelectedItem name: " + selectedItem.toString(mActivity));
                Navigation.startNavigation(
                        selectedItem.getId(),
                        selectedItem.getCoords(),
                        mActivity,
                        !selectedItem.getAvailabilty().toString(mActivity).equals(mActivity.getString(R.string.s_really_little_space))
                );
                break;
        }
    }

    private void updateList() {
        if (mParkings != null && !mParkings.isEmpty()) {
            Collections.sort(mParkings, getFilter());
            ((ParkingAdapter) mListView.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.s_filters:
                updateList();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
