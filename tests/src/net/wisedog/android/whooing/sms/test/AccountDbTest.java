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

import java.util.ArrayList;

import net.wisedog.android.whooing.sms.br.SmsFilterData;
import net.wisedog.android.whooing.sms.db.AccountInfo;
import net.wisedog.android.whooing.sms.db.CardAccountDbOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class AccountDbTest extends AndroidTestCase {
    private CardAccountDbOpenHelper mDb;
    private ArrayList<AccountInfo> mData;

    protected void setUp() throws Exception {
        super.setUp();
        mContext.deleteDatabase(CardAccountDbOpenHelper.DATABASE_NAME);
        mDb = new CardAccountDbOpenHelper(mContext);
        mData = new ArrayList<AccountInfo>();
        AccountInfo info = new AccountInfo("abc", "X21", "creditcard", 
        		20990101, -1);
        AccountInfo info2 = new AccountInfo("내카드", "X77", "creditcard", 
        		20990101, SmsFilterData.CODE_SAMSUNG);
        AccountInfo info3 = new AccountInfo("내카드2", "X66", "checkcard", 
        		20990101, SmsFilterData.CODE_KB);
        AccountInfo info4 = new AccountInfo("내카드3", "X55", "creditcard", 
        		20111212, -1);
        mData.add(info);
        mData.add(info2);
        mData.add(info3);
        mData.add(info4);
    }
    
    @Override
    protected void tearDown() throws Exception {
        SQLiteDatabase db = mDb.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + CardAccountDbOpenHelper.TABLE_ACCOUNTS);
        db.close();
        mContext.deleteDatabase(CardAccountDbOpenHelper.DATABASE_NAME);
        mData.clear();
        super.tearDown();
    }
    

    public void testPreconditions(){
        assertNotNull(mDb);
    }
    
    public void testOnCreate(){
        SQLiteDatabase db = mDb.getReadableDatabase();
        if(db == null){
            fail("DB is not created!");
        }
        
        String tableName = SQLiteDatabase.findEditTable(
        		CardAccountDbOpenHelper.TABLE_ACCOUNTS); 
        if(tableName == null){
            fail("Table name'"+ CardAccountDbOpenHelper.TABLE_ACCOUNTS 
            		+ "' cannot be found!");
        }
    }
    
    public void testClearTable(){
    	mDb.clearTable();
    	ArrayList<AccountInfo> array =  mDb.getAllRecord();
    	assertNotNull(array);
    	assertEquals(0, array.size());
    }
    
    public void testAddAllAccountEntity(){
    	assertEquals(true, mDb.addAllAccountEntity(mData));
    	AccountInfo info = mDb.getAccountByCardCode(SmsFilterData.CODE_SAMSUNG);
    	assertNotNull(info);
    	assertEquals("내카드", info.title);
    	assertEquals("creditcard", info.category);
    	assertEquals(20990101, info.close_date);
    }
    
    public void testGetAllRecord(){
    	ArrayList<AccountInfo> array = mDb.getAllRecord();
    	assertEquals(4, array.size());
    }
}
