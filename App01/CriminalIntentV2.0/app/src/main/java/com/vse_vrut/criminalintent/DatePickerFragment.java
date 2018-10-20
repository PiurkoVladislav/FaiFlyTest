package com.vse_vrut.criminalintent;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Влад on 01.02.2018.
 */

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";

    public static final String EXTRA_DATE = "com.vse_vrut.criminalintent.date";

    private DatePicker mDatePicker;

    public static DatePickerFragment newIntent(Date date){              //Метод сохраняет значения даты в диалоговом окне
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);                             //Тоесть фрагмент запускается уже с значением даты, переданой в аргс

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date,null);

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year,month,day,null);

        return new AlertDialog.Builder(getActivity())               //создание АД с заголовком и кнопкой ОК
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();

                        Date date = new GregorianCalendar(year,month,day).getTime();
                        setResult(Activity.RESULT_OK, date);
                    }
                })       //Позитив - положение кнопки(может быть Негатив и Неитрал)
                .create();                                                  //возвращает настроеный экземпляр диалоговоо окна
    }

    private void setResult(int resultCode, Date date){
        if (getTargetFragment()==null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);  //передает интент целевому фрагменту(Дату в КФ)
    }
}
