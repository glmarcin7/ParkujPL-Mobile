package pl.parkujznami.parkujpl_mobile.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.models.city.RespondedCity;
import pl.parkujznami.parkujpl_mobile.models.parking.ResponseWithParking;
import pl.parkujznami.parkujpl_mobile.models.report.ReportInRequest;
import pl.parkujznami.parkujpl_mobile.models.report.RequestForReport;
import pl.parkujznami.parkujpl_mobile.models.report.ResponseWithReport;
import pl.parkujznami.parkujpl_mobile.network.ApiClient;
import retrofit.RestAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParkingListFragment extends Fragment {


    public ParkingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parking_list, container, false);
        //getCities();
        //getParking();
        //getReport();
        return view;
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
    }

    private void getParking() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint(getActivity().getString(R.string.api_url))
                .build();

        // Create an instance of our ApiClient API interface.
        ApiClient apiClient = restAdapter.create(ApiClient.class);

        // Fetch a list of cities.
        ResponseWithParking parking = apiClient.parking(
                1,
                "Poznań, Dąbrowskiego 1",
                1000.0,
                50.0,
                2.0,
                5.0,
                1.0,
                0,
                1500
        );
    }

    private void getCities() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint(getActivity().getString(R.string.api_url))
                .build();

        // Create an instance of our ApiClient API interface.
        ApiClient apiClient = restAdapter.create(ApiClient.class);

        // Fetch a list of cities.
        List<RespondedCity> cities = apiClient.cities();

    }*/


}
