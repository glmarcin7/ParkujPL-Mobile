package pl.parkujznami.parkujpl_mobile.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.activities.StartActivity;
import pl.parkujznami.parkujpl_mobile.models.report.ReportInRequest;
import pl.parkujznami.parkujpl_mobile.models.report.RequestForReport;
import pl.parkujznami.parkujpl_mobile.models.report.ResponseWithReport;
import pl.parkujznami.parkujpl_mobile.network.ApiClient;
import pl.parkujznami.parkujpl_mobile.views.notifications.ChooseNumberOfFreeSpotsNotification;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by Marcin on 2015-04-19.
 */
public class ChooseNumberOfFreeSpotsFragment extends Fragment implements View.OnClickListener {

    public enum NumberOfFreeSpots {
        LITTLE_SPACE(1),
        THE_AVERAGE_AMOUNT_OF_SPACE(2),
        A_LOT_OF_SPACE(3);

        private final int mRequestedCode;

        NumberOfFreeSpots(int requestedCode) {
            this.mRequestedCode = requestedCode;
        }
    }

    private Activity mActivity;
    private Integer mParkingId;
    private List<View> mListOfButtons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_number_of_free_spots, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        mActivity = getActivity();

        mParkingId = mActivity.getIntent().getIntExtra(ChooseNumberOfFreeSpotsNotification.NOTIFICATION_TAG, -1);

        mListOfButtons = new ArrayList<>();
        mListOfButtons.add(view.findViewById(R.id.btn_little_space));
        mListOfButtons.add(view.findViewById(R.id.btn_the_average_amount_of_space));
        mListOfButtons.add(view.findViewById(R.id.btn_a_lot_of_space));

        for (View button : mListOfButtons) {
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_little_space:
                sendInformationAboutNumberOfFreeSpaces(NumberOfFreeSpots.LITTLE_SPACE);
                break;
            case R.id.btn_the_average_amount_of_space:
                sendInformationAboutNumberOfFreeSpaces(NumberOfFreeSpots.THE_AVERAGE_AMOUNT_OF_SPACE);
                break;
            case R.id.btn_a_lot_of_space:
                sendInformationAboutNumberOfFreeSpaces(NumberOfFreeSpots.A_LOT_OF_SPACE);
                break;
        }
    }

    private void sendInformationAboutNumberOfFreeSpaces(NumberOfFreeSpots numberOfFreeSpots) {
        int requestedCode = numberOfFreeSpots.mRequestedCode;

        Timber.d("Number of free spots: %d", requestedCode);

        RequestForReport requestForReport = new RequestForReport();
        ReportInRequest reportInRequest = new ReportInRequest();
        reportInRequest.setAvailabilty(requestedCode);
        requestForReport.setReportInRequest(reportInRequest);

        ApiClient.getParkujPlApiClient(mActivity).report(
                mParkingId,
                requestForReport,
                new Callback<ResponseWithReport>() {
                    @Override
                    public void success(ResponseWithReport responseWithReport, Response response) {
                        Toast.makeText(mActivity, mActivity.getString(R.string.thanks_for_sharing), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(mActivity, StartActivity.class));
                        mActivity.finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(mActivity, mActivity.getString(R.string.reporting_failed), Toast.LENGTH_LONG).show();
                    }
                }
        );
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
