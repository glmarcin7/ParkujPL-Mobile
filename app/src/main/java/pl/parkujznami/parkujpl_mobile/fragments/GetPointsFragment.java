package pl.parkujznami.parkujpl_mobile.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.activities.ParkingListActivity;
import timber.log.Timber;

/**
 * Created by Marcin on 2015-03-22.
 */
public class GetPointsFragment extends Fragment implements Button.OnClickListener {

    public static String POINTS_LIST = "START_AND_END_POINTS";

    @InjectView(R.id.et_start_point)
    EditText startPointEditText;

    @InjectView(R.id.et_end_point)
    EditText endPointEditText;

    private Integer cityId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_points, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        ButterKnife.inject(this, view);

        Button okButton = (Button) view.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(this);

        ImageButton swapButton = (ImageButton) view.findViewById(R.id.ib_swap_start_and_end_points);
        swapButton.setOnClickListener(this);

        cityId = getActivity().getIntent().getIntExtra(StartFragment.CITY_ID, -1);
        Timber.d("cityId: %d", cityId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                startParkingListActivity();
                break;
            case R.id.ib_swap_start_and_end_points:
                swapStartAndEndPoints();
                break;
        }
    }

    private void startParkingListActivity() {
        if (!areEditTextsNullOrEmpty()) {
            ArrayList<String> arrayList = new ArrayList<>(2);
            arrayList.add(startPointEditText.getText().toString());
            arrayList.add(endPointEditText.getText().toString());

            Bundle bundle = new Bundle();
            bundle.putStringArrayList(POINTS_LIST, arrayList);

            Intent intent = new Intent(getActivity(), ParkingListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Context context = getActivity();
            Toast.makeText(context, context.getString(R.string.some_edit_texts_are_empty), Toast.LENGTH_LONG).show();
        }
    }

    private boolean areEditTextsNullOrEmpty() {
        Editable startPoint = startPointEditText.getText();
        Editable endPoint = endPointEditText.getText();
        if (startPoint == null
                || startPoint.toString().isEmpty()
                || endPoint == null
                || endPoint.toString().isEmpty()
                ) {
            return true;
        }
        return false;
    }

    private void swapStartAndEndPoints() {
        Editable tmp = startPointEditText.getText();
        startPointEditText.setText(endPointEditText.getText());
        endPointEditText.setText(tmp);
    }
}
