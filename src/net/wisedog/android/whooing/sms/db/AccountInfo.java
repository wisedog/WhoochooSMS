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
package net.wisedog.android.whooing.sms.db;

public class AccountInfo {
	
	/** account name like '삼송카드'*/
	public String title = null;
	/** account ID like X1, X11*/
	public String account_id = null;
	
	/** card type - credit or check*/
	public String category = null;
	
	/** Close date */
	public int close_date = 29991231;
	
	/** Card name. @see SmsFilterData*/
	public int card_code;
	
	public AccountInfo(){
	    ; //Do nothing
	}
	
	public AccountInfo(String title, String account_id, 
			String category, int close_date, int card_code){
	    this.title = title;
	    this.account_id = account_id;
	    this.close_date = close_date;
	    this.category = category;
	    this.card_code = card_code;
	}
}
