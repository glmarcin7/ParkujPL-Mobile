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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.models.parking.Parking;
import pl.parkujznami.parkujpl_mobile.models.parking.ResponseWithParking;
import pl.parkujznami.parkujpl_mobile.network.ApiClient;
import pl.parkujznami.parkujpl_mobile.utils.Navigation;
import pl.parkujznami.parkujpl_mobile.views.adapters.ParkingAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass that manages screen: ParkingListFragment.
 *
 * @author Marcin Głowacki
 */
public class ParkingListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private Activity mActivity;
    private Integer mCityId;
    private String mCityName;
    private String mSearchedPhrase;
    private List<Parking> mParkings;

    @Bind(R.id.et_destination)
    EditText mDestinationEditText;
    @Bind(R.id.lv_list_of_parking)
    ListView mListView;
    @Bind(R.id.s_filters)
    Spinner mFiltersSpinner;


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
        ButterKnife.bind(this, view);

        mActivity = getActivity();

        Intent intent = mActivity.getIntent();
        mCityId = intent.getIntExtra(StartFragment.CITY_ID, -1);
        mCityName = intent.getStringExtra(StartFragment.CITY_NAME);
        mSearchedPhrase = intent.getStringExtra(StartFragment.SEARCHED_PHRASE);

        mDestinationEditText.getBackground().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        mDestinationEditText.setText(mSearchedPhrase);
        //start search on "done" key on keyboard
        mDestinationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)
                        || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    onLookForClick();
                }
                return false;
            }
        });

        mFiltersSpinner.setAdapter(new ArrayAdapter<>(
                mActivity,
                R.layout.spinner_dropdown_item,
                getResources().getStringArray(R.array.sa_filters)
        ));
        mFiltersSpinner.setOnItemSelectedListener(this);
        mFiltersSpinner.getBackground().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        mListView.setOnItemClickListener(this);

    }

    @OnClick(R.id.ib_look_for)
    public void onLookForClick() {
        search();
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

    private Comparator<? super Parking> getFilter() {
        String selectedItem = (String) mFiltersSpinner.getSelectedItem();
        if (selectedItem.equals(mActivity.getString(R.string.parking_list_screen_s_costs_of_parking))) {
            return Parking.costsComparator;
        } else if (selectedItem.equals(mActivity.getString(R.string.parking_list_screen_s_number_of_free_spots))) {
            return Parking.spotsAvailabilityComparator;
        } else if (selectedItem.equals(mActivity.getString(R.string.parking_list_screen_s_distance_to_parking))) {
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
                        !selectedItem.getAvailabilty().toString(mActivity).equals(mActivity.getString(R.string.parking_list_screen_item_value_tv_really_little_space))
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
        //Do nothing
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
}
