package pl.parkujznami.parkujpl_mobile.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pl.parkujznami.parkujpl_mobile.R;
import pl.parkujznami.parkujpl_mobile.activities.GetPointsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment implements Button.OnClickListener {


    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        Button leadToAPointButton = (Button) view.findViewById(R.id.btn_lead_to_a_point);
        leadToAPointButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_lead_to_a_point:
                startGetPointsActivity();
                break;
        }
    }

    private void startGetPointsActivity() {
        startActivity(new Intent(getActivity(), GetPointsActivity.class));
    }
}
