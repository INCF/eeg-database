package cz.zcu.kiv.eegdatabase.wui.components.utils;

import java.sql.Timestamp;

import com.ibm.icu.util.Calendar;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;

public class DateUtils {
    
    public static int getPersonAge(Person per, Timestamp scenarioStartTime) {
        
        if(scenarioStartTime == null || per == null || per.getDateOfBirth() == null)
            return -1;
        
        Calendar toTime = Calendar.getInstance();
        toTime.setTimeInMillis(scenarioStartTime.getTime());
        
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTimeInMillis(per.getDateOfBirth().getTime());
        
        int years = toTime.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        int currMonth = toTime.get(Calendar.MONTH) + 1;
        int birthMonth = birthDate.get(Calendar.MONTH) + 1;
        int months = currMonth - birthMonth;
        
        if (months < 0)
           years--;
        return years;
    }
}
