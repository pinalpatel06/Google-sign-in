package com.bayer.ah.bayertekenscanner.fragment.teekMelden;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bayer.ah.bayertekenscanner.R;
import com.bayer.ah.bayertekenscanner.activity.HitStatusActivity;
import com.bayer.ah.bayertekenscanner.utils.DateUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tejas Sherdiwala on 7/5/2017.
 * &copy; Knoxpo
 */

public class DrugDateSelectionFragment extends Fragment {
    private static final String
            TAG = DrugDateSelectionFragment.class.getSimpleName(),
            ARGS_BUNDLE = TAG + ".ARGS_BUNDLE";


    @BindView(R.id.tv_drug_date)
    TextView mDateTV;
    @BindView(R.id.tv_confirm)
    TextView mConfirmTV;

    public static DrugDateSelectionFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        args.putBundle(ARGS_BUNDLE, bundle);
        DrugDateSelectionFragment fragment = new DrugDateSelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drug_date_selection, container, false);
        ButterKnife.bind(this, v);
        updateUI();
        return v;
    }

    private void updateUI() {
        Calendar c = Calendar.getInstance();
        mDateTV.setText(DateUtils.toDrugDate(c.getTime()));
    }

    @OnClick({R.id.tv_confirm, R.id.tv_drug_date})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_drug_date:
                datePicker();
                break;
            case R.id.tv_confirm:
                Intent intent = new Intent(getActivity(), HitStatusActivity.class);
                Bundle bundle = getArguments().getBundle(ARGS_BUNDLE);
                intent.putExtra(HitStatusActivity.EXTRA_PET_BUNDLE, bundle);
                startActivity(intent);
        }
    }

    private void datePicker() {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DATE, dayOfMonth);
                        mDateTV.setText(DateUtils.toDrugDate(calendar.getTime()));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
