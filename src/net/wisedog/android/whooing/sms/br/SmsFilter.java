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
package net.wisedog.android.whooing.sms.br;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.wisedog.android.whooing.sms.util.WhooingSmsUtil;

import android.os.Bundle;

public class SmsFilter {
    static public final String KEY_ITEM = "item";
    static public final String KEY_CARDNAME = "cardname";
    static public final String KEY_DATE = "date";
    static public final String KEY_MONEY = "money";
    static public final String KEY_CHECKCARD = "checkcard";

    /**
     * This is important method. Take information like amount, date, item
     * from the given message. All SMS format of card company is different each other,
     * it was suffering. Especially, what the f*** Hyundai card 
     * */
    public Bundle filterMessage(String msg, int cardCode){
        String normalizedMsg = normalizeMsg(msg);
        Bundle b = null;
        String[] tokens = normalizedMsg.split(" ");
        if(tokens == null){
            return null;
        }
        switch(cardCode){
        case SmsFilterData.CODE_KEB:
            b = new Bundle();
            b.putInt(KEY_DATE, WhooingSmsUtil.convertWhooingDate(tokens[6]));
            b.putString(KEY_ITEM, tokens[5]);
            b.putInt(KEY_MONEY, convertToInt(tokens[4]));            
            break;
            
        case SmsFilterData.CODE_LOTTE:
        	b = new Bundle();
        	b.putInt(KEY_DATE, WhooingSmsUtil.convertWhooingDate(tokens[4]));
            b.putString(KEY_ITEM, tokens[6]);
            b.putInt(KEY_MONEY, convertToInt(tokens[2]));
            break;
            
        case SmsFilterData.CODE_SAMSUNG:
	    	b = new Bundle();
	    	b.putInt(KEY_DATE, WhooingSmsUtil.convertWhooingDate(tokens[1]));
	    	if(tokens[3].contains("원")){
	    	    b.putInt(KEY_MONEY, convertToInt(tokens[3]));
	    	    b.putString(KEY_ITEM, tokens[5]);
	    	}
	    	else{
	    	    b.putInt(KEY_MONEY, convertToInt(tokens[4]));
	    	    b.putString(KEY_ITEM, tokens[3]);
	    	    b.putBoolean(KEY_CHECKCARD, true);
	    	}
	        break;
	        
        case SmsFilterData.CODE_KB:
	    	b = new Bundle();
	    	b.putInt(KEY_DATE, WhooingSmsUtil.convertWhooingDate(tokens[3]));
	        b.putString(KEY_ITEM, tokens[6]);
	        b.putInt(KEY_MONEY, convertToInt(tokens[5]));
        	break;
        	
        case SmsFilterData.CODE_HANA:
        	String msgBulk = tokens[4];
        	String msgToken[] = msgBulk.split("/");
        	if(msgToken == null){
        		break;
        	}

	    	b = new Bundle();
	    	b.putInt(KEY_DATE, WhooingSmsUtil.convertWhooingDate(tokens[1]));
	        b.putString(KEY_ITEM, tokens[3]);
	        b.putInt(KEY_MONEY, convertToInt(msgToken[1]));
        	break;
        	
        case SmsFilterData.CODE_SHINHAN:
        	String msgBulk1 = tokens[3];
        	String msgToken1[] = msgBulk1.split("\\)");
        	if(msgToken1 == null){
        		break;
        	}
        	b = new Bundle();
	    	b.putInt(KEY_DATE, WhooingSmsUtil.convertWhooingDate(tokens[1]));
	        b.putString(KEY_ITEM, tokens[4]);
	        b.putInt(KEY_MONEY, convertToInt(msgToken1[1]));
        	break;
        	
        case SmsFilterData.CODE_WOORI:
        	b = new Bundle();
        	b.putInt(KEY_DATE, WhooingSmsUtil.convertWhooingDate(tokens[1]));
            b.putString(KEY_ITEM, tokens[5]);
            Pattern pattern  = Pattern.compile("[\\d,]*원");
            Matcher matcher = pattern.matcher(tokens[3]);
            if(matcher.find()){
                String str = matcher.group(0);
                b.putInt(KEY_MONEY, convertToInt(str));
            } 	
	        
        	break;
        case SmsFilterData.CODE_WOORICHECK:
        	b = new Bundle();
            b.putInt(KEY_DATE, WhooingSmsUtil.convertWhooingDate(tokens[4]));
            b.putString(KEY_ITEM, tokens[6]);
            b.putInt(KEY_MONEY, convertToInt(tokens[2]));
            b.putBoolean(KEY_CHECKCARD, true);
        	break;
        	
        case SmsFilterData.CODE_HYUNDAI:
        	b = new Bundle();
        	String amountStr = null;
        	//"[현대카드C]-승인\n정원석님\n03/25 07:35\n13,500원(일시불)\n택시(서울)",
        	if(tokens[2].contains("/")){
        		b.putString(KEY_ITEM, tokens[5]);
        		b.putInt(KEY_DATE, WhooingSmsUtil.convertWhooingDate(tokens[2]));
        		b.putBoolean(KEY_CHECKCARD, true);
        		amountStr = tokens[4];
        	}
        	//"[현대카드M3]-승인\n홍길*님\n37,500원(일시불)\n서울불고기\n누적:1,189,280원",
        	else{
        		b.putString(KEY_ITEM, tokens[3]);
        		b.putInt(KEY_DATE, WhooingSmsUtil.getTodayYYYYMMDDInt());
        		amountStr = tokens[2];
        		b.putBoolean(KEY_CHECKCARD, false);
        	}
        	if(amountStr != null){
        		String[] amountTokens = amountStr.split("\\(");
        		b.putInt(KEY_MONEY, convertToInt(amountTokens[0]));
        	}
        	break;
	    
	    default:
	    	break;	        	
        }
		    
        if(b != null){
        	b.putInt(KEY_CARDNAME, cardCode);
        }
        
        return b;
    }
    
	/**
     * Normalize message. replace carrage return, dot, more than 2 spaces to one space
     * @param	msg		SMS message
     * @return	Returns normalized text
     * */
    private String normalizeMsg(String msg) {
    	if(msg == null){
    		return null;
    	}
		String resultText = msg.replace("\r\n", " ");
		resultText = resultText.replace('\n', ' ');
		resultText = resultText.replace('.', ' ');
		
		resultText = resultText.replace("      ", " ");
		resultText = resultText.replace("    ", " ");
		resultText = resultText.replace("  ", " ");
		resultText = resultText.replace("  ", " ");
		 
		return resultText;
	}
    
    /**
     * Convert to amount string to int
     * @param	amount	amount string like "20000원"
     * @return	amount integer value
     * */
    private int convertToInt(String amount){
        String tmp = amount.replace("원", "");
        tmp = tmp.replace(",", "");
        int amountInt = 0;
        try{
            amountInt = Integer.valueOf(tmp);
        }
        catch(NumberFormatException e){
            e.printStackTrace();
        }
        return amountInt;
    }
    
    

	/**
     * Filter sender address. This is Positive system(Originally prohibit, excepts are allowed).
     * If the address is not in our list, the filter do not accept the message.
     * @see	SmsFilterData
     * @return	Returns card company number if it is acceptable number, or -1.
     * */
	public int filteringSender(String originatingAddress) {
		if(originatingAddress == null){
			return -1;
		}
		for(int i = 0; i < SmsFilterData.SENDER_NUMBER.length; i++){
			if(originatingAddress.compareTo(SmsFilterData.SENDER_NUMBER[i]) == 0){
        		return i;
        	}
		}
		return -1;
	}
	
}
