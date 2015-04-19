package pl.parkujznami.parkujpl_mobile.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.models.parking.Parking;
import pl.parkujznami.parkujpl_mobile.models.parking.ResponseWithParking;
import pl.parkujznami.parkujpl_mobile.models.shared.Coords;
import pl.parkujznami.parkujpl_mobile.network.ApiClient;
import pl.parkujznami.parkujpl_mobile.utils.Navigation;
import pl.parkujznami.parkujpl_mobile.views.adapters.ParkingAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParkingListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private Activity mActivity;
    private Integer mCityId;
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

    private void initialize(View view) {
        mActivity = getActivity();

        mCityId = mActivity.getIntent().getIntExtra(StartFragment.CITY_ID, -1);

        mDestinationEditText = (EditText) view.findViewById(R.id.et_end_point);

        mImageButton = (ImageButton) view.findViewById(R.id.ib_look_for);
        mImageButton.setOnClickListener(this);

        mFiltersSpinner = (Spinner) view.findViewById(R.id.s_filters);
        mFiltersSpinner.setAdapter(new ArrayAdapter<>(
                mActivity,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.sa_filters)
        ));
        mFiltersSpinner.setOnItemSelectedListener(this);

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
        ApiClient.getParkujPlApiClient(mActivity).parking(
                mCityId,
                mDestinationEditText.getText().toString(),
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
                                    android.R.layout.simple_list_item_1,
                                    mParkings
                            ));
                        } else {
                            Toast.makeText(mActivity, mActivity.getString(R.string.find_nearest_parking_fail), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //Toast.makeText(mActivity, mActivity.getString(R.string.findParkingFail), Toast.LENGTH_LONG).show();

                        //Tylko do testów
                        Parking parking1 = new Parking();
                        parking1.setAddress("Dąbrowskiego 1");
                        Coords coords1 = new Coords();
                        coords1.setLatitude("52.41063962");
                        coords1.setLongitude("16.91310883");
                        parking1.setCoords(coords1);

                        Parking parking2 = new Parking();
                        parking2.setAddress("Dąbrowskiego 10");
                        Coords coords2 = new Coords();
                        coords2.setLatitude("52.41084904");
                        coords2.setLongitude("16.91186428");
                        parking2.setCoords(coords2);

                        List<Parking> parkings = new ArrayList<>();
                        parkings.add(parking1);
                        parkings.add(parking2);
                        mListView.setAdapter(new ParkingAdapter(
                                mActivity,
                                android.R.layout.simple_list_item_1,
                                parkings
                        ));
                    }
                }
        );
    }

    private Comparator<? super Parking> getFilter() {
        String selectedItem = (String) mFiltersSpinner.getSelectedItem();
        if (selectedItem.equals("Koszt")) {
            return Parking.CostsComparator;
        } else if (selectedItem.equals("Liczba wolnych miejsc")) {
            return Parking.CostsComparator;
        } else if (selectedItem.equals("Odległość od celu")) {
            return Parking.DistanceComparator;
        }
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_list_of_parking:
                Parking selectedItem = (Parking) parent.getItemAtPosition(position);
                Navigation.startNavigation(selectedItem.getId(), selectedItem.getCoords(), mActivity);
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
