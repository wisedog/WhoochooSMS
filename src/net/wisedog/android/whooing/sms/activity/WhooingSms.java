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
package net.wisedog.android.whooing.sms.activity;

import java.util.ArrayList;

import net.wisedog.android.whooing.sms.R;
import net.wisedog.android.whooing.sms.db.AccountInfo;
import net.wisedog.android.whooing.sms.db.CardAccountDbOpenHelper;
import net.wisedog.android.whooing.sms.util.WhooingSmsUtil;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WhooingSms extends Activity {

	static final String WHOOING_ACCOUNT_URI = "content://net.wisedog.android.whooing.contentprovider/account";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_whooing_sms);
		TextView boardText = (TextView)findViewById(R.id.whoochoo_sms_text_board);
		ContentResolver cr = getContentResolver();
		
		Cursor cursor = cr.query(Uri.parse(WHOOING_ACCOUNT_URI), null, null, null, null);
		if(cursor != null){
			if(cursor.getCount() == 0){
				if(boardText != null){
					boardText.setText(R.string.whoochoo_sms_board_no_data);
				}
			}
			ArrayList<AccountInfo> array = new ArrayList<AccountInfo>();
			while(cursor.moveToNext()){
				//Validate account, not expired
				if(cursor.getInt(6) > WhooingSmsUtil.getTodayYYYYMMDDInt()){
					String category = cursor.getString(7);
					if(category.compareTo("creditcard") == 0 || 
							category.compareTo("checkcard") == 0){
						AccountInfo entity = new AccountInfo();
						entity.account_id = cursor.getString(0);
						entity.title = cursor.getString(3);
						entity.category = category;
						entity.close_date = cursor.getInt(6);
						array.add(entity);
					}
					else{
						; //Discard the account
					}
				}
				else{	//Expired
					; //Discard the account
				}
			}
			cursor.close();

			ListView listView = (ListView)findViewById(R.id.whoochoo_sms_listview_account);
			AccountAdapter adapter = new AccountAdapter(this, array);
			listView.setAdapter(adapter);
		}
		else{
			if(boardText != null){
				boardText.setText(R.string.whoochoo_sms_board_no_data);
			}
		}
	}
	
	/**
	 * Click handler of confirm btn
	 * @param	v	View to be clicked
	 * */
	public void onClickConfirmBtn(View v){
		ListView listView = (ListView)findViewById(R.id.whoochoo_sms_listview_account);
		ArrayList<AccountInfo> array = ((AccountAdapter)listView.getAdapter()).getData();
		
		// This algorithm is O(N^2) complexity. 
		// Need to improve, but card and checkcard account is just a few, 
		// It is not a problem.
		for(int i = 0; i < array.size(); i++){
		    AccountInfo info = array.get(i);
		    if(info.card_code == 0){  // 0 is no selected status
                continue;
            }
		    for(int j = 0; j < array.size(); j++){
		        AccountInfo info2 = array.get(j);
		        if(i != j && info.card_code == info2.card_code){
		            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                    v.startAnimation(shake);
                    Toast.makeText(this, getString(R.string.whoochoo_sms_toast_check_duplication), Toast.LENGTH_LONG).show();
                    return;
		        }		        
		    }
		}
		Toast.makeText(this, getString(R.string.whoochoo_sms_toast_saved), Toast.LENGTH_LONG).show();
		CardAccountDbOpenHelper db = new CardAccountDbOpenHelper(this);
		db.clearTable();
		db.addAllAccountEntity(array);
	}
}
