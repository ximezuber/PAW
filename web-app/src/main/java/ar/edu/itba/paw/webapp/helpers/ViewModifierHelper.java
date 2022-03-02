package ar.edu.itba.paw.webapp.helpers;

import java.util.*;

public class ViewModifierHelper {

    // Private constructor to prevent instantiation
    private ViewModifierHelper() {
        throw new UnsupportedOperationException();
    }

    public static List<Integer> getDays() {
        List<Integer> days= new LinkedList<>();
        days.add(Calendar.MONDAY);
        days.add(Calendar.TUESDAY);
        days.add(Calendar.WEDNESDAY);
        days.add(Calendar.THURSDAY);
        days.add(Calendar.FRIDAY);
        return days;
    }

    public static List<Integer> getTimes() {
        List<Integer> times= new LinkedList<>();
        times.add(8);
        times.add(9);
        times.add(10);
        times.add(11);
        times.add(12);
        times.add(13);
        times.add(14);
        times.add(15);
        times.add(16);
        times.add(17);
        times.add(18);
        times.add(19);
        return times;
    }

    public static List<Calendar> getMonth(int week){
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, 7 * (week - 1));
        Calendar first;
        List<Calendar> month = new ArrayList<>();
        if(date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            first = date;
            first.add(Calendar.DATE, 1);
        }else if(date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            first = date;
            first.add(Calendar.DATE, 2);
        }else{
            first = date;
            first.add(Calendar.DATE, -(date.get(Calendar.DAY_OF_WEEK)) + 2);
        }
        for (int i = 0; i < 7; i++){
            Calendar day = Calendar.getInstance();
            day.setTime(first.getTime());
            day.add(Calendar.DATE, i);
            month.add(day);
        }
        return month;
    }
}
