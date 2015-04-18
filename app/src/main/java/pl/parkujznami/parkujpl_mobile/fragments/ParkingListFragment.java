package pl.parkujznami.parkujpl_mobile.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
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
import java.util.HashMap;
import java.util.List;

import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.models.city.RespondedCity;
import pl.parkujznami.parkujpl_mobile.models.parking.Parking;
import pl.parkujznami.parkujpl_mobile.models.parking.ResponseWithParking;
import pl.parkujznami.parkujpl_mobile.models.report.ReportInRequest;
import pl.parkujznami.parkujpl_mobile.models.report.RequestForReport;
import pl.parkujznami.parkujpl_mobile.models.report.ResponseWithReport;
import pl.parkujznami.parkujpl_mobile.models.shared.Coords;
import pl.parkujznami.parkujpl_mobile.network.ApiClient;
import pl.parkujznami.parkujpl_mobile.utils.Navigation;
import pl.parkujznami.parkujpl_mobile.views.adapters.ParkingAdapter;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParkingListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private Integer cityId;
    private EditText destinationEditText;
    private ImageButton imageButton;
    private ListView listView;
    private Spinner spinner;

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

        cityId = getActivity().getIntent().getIntExtra(StartFragment.CITY_ID, -1);

        destinationEditText = (EditText) view.findViewById(R.id.et_end_point);

        imageButton = (ImageButton) view.findViewById(R.id.ib_look_for);
        imageButton.setOnClickListener(this);

        spinner = (Spinner) view.findViewById(R.id.s_filters);
        spinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.sa_filters)
        ));
        spinner.setOnItemSelectedListener(this);

        listView = (ListView) view.findViewById(R.id.lv_list_of_parking);
        listView.setOnItemClickListener(this);

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
        ApiClient.getParkujPlApiClient(getActivity()).parking(
                cityId,
                destinationEditText.getText().toString(),
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
                            listView.setAdapter(new ParkingAdapter(
                                    getActivity(),
                                    android.R.layout.simple_list_item_1,
                                    parkings
                            ));
                        } else {
                            Context context = getActivity();
                            Toast.makeText(context, context.getString(R.string.findNearestParkingFail), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Context context = getActivity();
                        //Toast.makeText(context, context.getString(R.string.findParkingFail), Toast.LENGTH_LONG).show();

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
                        listView.setAdapter(new ParkingAdapter(
                                getActivity(),
                                android.R.layout.simple_list_item_1,
                                parkings
                        ));
                    }
                }
        );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_list_of_parking:
                Parking selectedItem = (Parking) parent.getItemAtPosition(position);
                Navigation.startNavigation(selectedItem.getCoords(), getActivity());
                break;
        }
    }

    private void updateList() {

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


    /*private void getReport() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint(getActivity().getString(R.string.api_url))
                .build();

        // Create an instance of our ApiClient API interface.
        ApiClient apiClient = restAdapter.create(ApiClient.class);

        // Fetch a list of cities.
        RequestForReport requestForReport = new RequestForReport();
        ReportInRequest reportInRequest = new ReportInRequest();
        reportInRequest.setAvailabilty(3);
        requestForReport.setReportInRequest(reportInRequest);
        ResponseWithReport report = apiClient.report(4, requestForReport);
    }*/
}
