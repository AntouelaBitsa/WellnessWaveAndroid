package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.annotation.SuppressLint;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.CalendarConstraints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressLint("ParcelCreator")
public class CustomDateValidator implements  CalendarConstraints.DateValidator{
    private final List<Long> holidaysDates = new ArrayList<>();

    public CustomDateValidator() {
        holidaysDates.add(getDateInMillis(2025,1,1));    //New year
        holidaysDates.add(getDateInMillis(2025,1,6));    //Theofania
        holidaysDates.add(getDateInMillis(2025,3,3));    //Kathara Deytera
        holidaysDates.add(getDateInMillis(2025,3,25));   //25h Martioy
        holidaysDates.add(getDateInMillis(2025,4,18));   //M. Paraskeyi
        holidaysDates.add(getDateInMillis(2025,4,19));   //M. Sabbato
        holidaysDates.add(getDateInMillis(2025,4,20));   //Easter
        holidaysDates.add(getDateInMillis(2025,4,21));   //Deytera toy Pasxa
        holidaysDates.add(getDateInMillis(2025,5,1));    //Protomagia
        holidaysDates.add(getDateInMillis(2025,6, 8));   //Penthkosth
        holidaysDates.add(getDateInMillis(2025,6,9));    //Agioy Pneymatos
        holidaysDates.add(getDateInMillis(2025,8,15));   //Dekapentaygoystos
        holidaysDates.add(getDateInMillis(2025,10,28));  //28h Oktobrioy
        holidaysDates.add(getDateInMillis(2025,12,25));  //Christmas
        holidaysDates.add(getDateInMillis(2025,12,26));  //Day after Christmas
    }

    private Long getDateInMillis(int year, int month, int day) {
        /**Holiday Dates Set to Calendar*/
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


    @Override
    public boolean isValid(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        /**Disable Weekends*/
        int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayofWeek == Calendar.SATURDAY || dayofWeek == Calendar.SUNDAY){
            return false;
        }

        /**Disable Holidays*/
        return !holidaysDates.contains(date);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
