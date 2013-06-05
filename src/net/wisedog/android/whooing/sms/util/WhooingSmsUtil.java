/*
  Copyright 2013 Jongha Kim(me@wisedog.net)
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
      http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package net.wisedog.android.whooing.sms.util;

import java.util.Calendar;

public class WhooingSmsUtil {

	/**
	 * Get today date with YYYYMMDD format in integer
	 * @return	Returns today date in Integer YYYYMMDD format
	 * */
	static public int getTodayYYYYMMDDInt(){
		Calendar rightNow = Calendar.getInstance(); 
		
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);
		int day = rightNow.get(Calendar.DAY_OF_MONTH);
		month = month + 1;    //달은 0~11이다.
		String monthString = "";
		String dayString = "";
		if(day < 10)
			dayString += "0";
		if(month < 10)
			monthString += "0";
		monthString +=month;
		dayString += day;
		
		int date = 0;
        try{
            date = Integer.parseInt(""+year+monthString+dayString);
        }catch(NumberFormatException e){
            return 0;
        }
        return date;
	}
	/**
     * Convert string format date data to whooing date format integer
     * @param   dateStr     Date data formatted string like "05/21"
     * @return   Return whooing style integer date like 20121212 otherwise -1
     * */
    static public int convertWhooingDate(String dateStr){
        String convertDate = dateStr.replace("/","");
        if(convertDate.length() == 3){
            convertDate = "0" + convertDate;
        }
        Calendar rightNow = Calendar.getInstance(); 
        
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH) + 1;
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        
        int today = year * 10000 + month * 100 + day;
        convertDate = year + convertDate;
        int convertDateInt = 0;
        try{
             convertDateInt = Integer.valueOf(convertDate);
             // In case of receiving message 12/31 on 1 Jan
             if((today / 10000) < (convertDateInt / 10000)){
                 convertDateInt -= 10000;
             }
             
        }
        catch(NumberFormatException e){
            e.printStackTrace();
        }
        
        //guard wrong date
        if(convertDateInt < ((year * 10000) + 101)){
        	convertDateInt = today;
        }
        return convertDateInt; 
    }
}
