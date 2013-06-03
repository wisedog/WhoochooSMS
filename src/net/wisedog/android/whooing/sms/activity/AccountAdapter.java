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
import net.wisedog.android.whooing.sms.br.SmsFilterData;
import net.wisedog.android.whooing.sms.db.AccountInfo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter {
	private ArrayList<AccountInfo> mDataArray;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public AccountAdapter(Context context, ArrayList<AccountInfo> array){
		mDataArray = array;
		mContext = context;
		mInflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mDataArray.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public ArrayList<AccountInfo> getData(){
		return mDataArray;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) { 
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.account_item, parent, false);
           }
		final int listPosition = position;
		TextView textWhooingAccount = (TextView)convertView.findViewById(
				R.id.account_item_whooing_account_text);
		AccountInfo info = mDataArray.get(position);
		textWhooingAccount.setText(info.title);
		
		Spinner cardNameSpinner = (Spinner)convertView.findViewById(
				R.id.account_item_card_spinner);
        ArrayAdapter<String> cardAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.select_dialog_item, SmsFilterData.CARD_COMPANY_NAME) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
                return v;
            }

        };
        cardNameSpinner.setAdapter(cardAdapter);
        cardNameSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, 
		            int pos, long id) {
				AccountInfo info = mDataArray.get(listPosition);
				info.card_code = pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				;				
			}
        	
		});
        
		return convertView;
	}
}
