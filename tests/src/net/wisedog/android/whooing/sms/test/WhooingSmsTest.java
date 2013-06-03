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
package net.wisedog.android.whooing.sms.test;

import net.wisedog.android.whooing.sms.activity.WhooingSms;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import android.widget.TextView;

/**
 * WhooingSms activity testing.
 * There is hard to test the activity. Because, the data depends on Whoochoo's account 
 * and is different from each other. So I have no idea how can I normalize pre-required data 
 * 
 * @author Wisedog(me@wisedog.net)
 * */
public class WhooingSmsTest extends ActivityInstrumentationTestCase2<WhooingSms> {
    
    private WhooingSms mActivity;
	private ListView mListView;
	private TextView mTextBoard;

	public WhooingSmsTest(){
        super("net.wisedog.android.whooing.sms.activity.WhooingSms", WhooingSms.class);
    }

    public WhooingSmsTest(Class<WhooingSms> activityClass) {
        super(activityClass);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mListView = (ListView)mActivity.findViewById(
        		net.wisedog.android.whooing.sms.R.id.whoochoo_sms_listview_account);
        mTextBoard = (TextView)mActivity.findViewById(
        		net.wisedog.android.whooing.sms.R.id.whoochoo_sms_text_board);
        
    }
    
    public void testPreconditions(){
    	assertNotNull(mActivity);
    	assertNotNull(mListView);
    	assertNotNull(mTextBoard);
    }
    
    public void testBoardText(){
    	int itemCount = mListView.getAdapter().getCount();
    	String msg = mTextBoard.getText().toString();
    	String msgNoData = mActivity.getString(
				net.wisedog.android.whooing.sms.R.string.whoochoo_sms_board_no_data);
    	String msgHelp = mActivity.getString(
				net.wisedog.android.whooing.sms.R.string.whoochoo_sms_board_help);
    	if(itemCount == 0){
    		assertEquals(0, msg.compareTo(msgNoData));
    	}else if(itemCount > 0){
    		assertEquals(0, msg.compareTo(msgHelp));
    	}else {
    		fail("Wrong item count in the listview");
    	}
    }
}
