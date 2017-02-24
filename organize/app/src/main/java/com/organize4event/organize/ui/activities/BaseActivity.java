package com.organize4event.organize.ui.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.organize4event.organize.R;
import com.organize4event.organize.common.WaitDialog;
import com.organize4event.organize.listener.ToolbarListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by marcela on 02/02/17.
 */

public class BaseActivity  extends AppCompatActivity{
    private WaitDialog waitDialog;

    SimpleDateFormat simpleDateFormat;
    DatePickerDialog startDatePickerDialog;
    Calendar birthCalendarStart;
    Calendar birthCalendarFinish;
    Calendar startCalendar;
    Calendar finishCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context context){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    public void showLoading(){
        hideLoading();

        if(waitDialog == null){
            waitDialog = WaitDialog.newInstance();
            waitDialog.show(getFragmentManager(), waitDialog.getTag());
        }
    }

    public void hideLoading(){

        if (waitDialog != null){
            try{
                waitDialog.dismissAllowingStateLoss();
                waitDialog = null;
            }
            catch (Exception ex){
                Log.v("Error: ", ex.getMessage());
            }
        }
    }

    public void configureToolbar(Toolbar toolbar, String title, boolean isHomeAsUpEnabled, final ToolbarListener listener){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            if (isHomeAsUpEnabled){
                actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_48dp);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
            actionBar.setTitle(title);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick();
                }
            });
        }
    }

    public static void hideKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isOline(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }
        return false;
    }

    public void showDialogMessage(String title, String message){
        new MaterialDialog.Builder(this).title(title).content(message).positiveText("Ok").show();
    }

    public void selectDate(Context context, final EditText editText, int mode){
        simpleDateFormat = new SimpleDateFormat(context.getResources().getString(R.string.date_format));

        birthCalendarStart = GregorianCalendar.getInstance();
        birthCalendarFinish = GregorianCalendar.getInstance();
        startCalendar = GregorianCalendar.getInstance();
        finishCalendar = GregorianCalendar.getInstance();

        startDatePickerDialog = new DatePickerDialog(context, null, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));

        finishCalendar.add(Calendar.MONTH, 6);
        birthCalendarStart.add(Calendar.YEAR, -100);
        birthCalendarFinish.add(Calendar.YEAR, -10);

        switch (mode){
            case 1:
                startDatePickerDialog.getDatePicker().setMinDate(birthCalendarStart.getTimeInMillis());
                startDatePickerDialog.getDatePicker().setMaxDate(birthCalendarFinish.getTimeInMillis());

                startDatePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        startCalendar.set(startDatePickerDialog.getDatePicker().getYear(), startDatePickerDialog.getDatePicker().getMonth(), startDatePickerDialog.getDatePicker().getDayOfMonth());
                        editText.setText(simpleDateFormat.format(startCalendar.getTime()));
                    }
                });
                startDatePickerDialog.setTitle(context.getResources().getString(R.string.label_data_nascimento));
                startDatePickerDialog.show();
                break;

            case 2:
                startDatePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        startCalendar.set(startDatePickerDialog.getDatePicker().getYear(), startDatePickerDialog.getDatePicker().getMonth(), startDatePickerDialog.getDatePicker().getDayOfMonth());
                        editText.setText(simpleDateFormat.format(startCalendar.getTime()));
                    }
                });
                startDatePickerDialog.setTitle(context.getResources().getString(R.string.label_data_inicio));
                startDatePickerDialog.show();

                break;

            case 3:
                startDatePickerDialog.getDatePicker().setMinDate(startCalendar.getTimeInMillis());
                startDatePickerDialog.getDatePicker().setMaxDate(finishCalendar.getTimeInMillis());

                startDatePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        startCalendar.set(startDatePickerDialog.getDatePicker().getYear(), startDatePickerDialog.getDatePicker().getMonth(), startDatePickerDialog.getDatePicker().getDayOfMonth());
                        editText.setText(simpleDateFormat.format(startCalendar.getTime()));
                    }
                });
                startDatePickerDialog.setTitle(context.getResources().getString(R.string.label_data_final));
                startDatePickerDialog.show();

                break;
        }
    }
}