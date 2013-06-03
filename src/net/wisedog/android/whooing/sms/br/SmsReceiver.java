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

import net.wisedog.android.whooing.sms.db.AccountInfo;
import net.wisedog.android.whooing.sms.db.CardAccountDbOpenHelper;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	static final boolean IS_DEBUG = false;
	static final String WHOOING_SMS_URI = "content://net.wisedog.android.whooing.contentprovider/sms";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";            
        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];     
            SmsFilter filter = new SmsFilter();
            for (int i=0; i<msgs.length; i++){
            	msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
            	if(IS_DEBUG){
            		str += "SMS from " + msgs[i].getOriginatingAddress();                     
                    str += " :";
                    str += msgs[i].getMessageBody().toString();
                    str += "\n";
            	}
                
                int result = filter.filteringSender(msgs[i].getOriginatingAddress()); 
                if(result == -1){
                    if(IS_DEBUG){
                        if(msgs[i].getOriginatingAddress().compareTo("01011112222") == 0){
                            result = SmsFilterData.CODE_KEB;
                        }                        
                    }else{
                        continue;
                    }                	
                }
                CardAccountDbOpenHelper db = new CardAccountDbOpenHelper(context);
                AccountInfo info = db.getAccountByCardCode(result);
                
                
                // The card code is not set there may be no result from DB
                // then discard this message.
                if(info == null){
                    return;
                }
                Bundle b = filter.filterMessage(msgs[i].getMessageBody(), result);
                if(b != null){
                	ContentValues values = new ContentValues();
                	values.put("account_id", info.account_id);
                	values.put("date", b.getInt(SmsFilter.KEY_DATE));
                	values.put("amount", b.getInt(SmsFilter.KEY_MONEY));
                	try{
                		values.put("msg", msgs[i].getMessageBody().toString());
                	}catch(NullPointerException e){
                		values.put("msg", "");
                	}
                	ContentResolver cr = context.getContentResolver();
                	cr.insert(Uri.parse(WHOOING_SMS_URI), values);
                }
            }
            if(IS_DEBUG){
            	Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
