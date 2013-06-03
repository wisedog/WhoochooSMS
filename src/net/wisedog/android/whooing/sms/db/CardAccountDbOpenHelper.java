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

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CardAccountDbOpenHelper extends SQLiteOpenHelper {
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    public static final String DATABASE_NAME = "whooing_sms";
 
    // Contacts table name
    public static final String TABLE_ACCOUNTS = "card_account";
 
    // Contacts Table Columns names
    private static final String KEY_ACCOUNT_ID = "account_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CLOSE_DATE = "close_date";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_CARDCODE = "cardcode";

	public CardAccountDbOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

    public CardAccountDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "("
                + KEY_ACCOUNT_ID + " TEXT PRIMARY KEY," 
                + KEY_TITLE + " TEXT,"
                + KEY_CLOSE_DATE + " INTEGER,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_CARDCODE + " INTEGER"
                +")"; 
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Add all item of the give array
	 * @param	array		AccountInfo array to add to the table
	 * @return	Returns true if it success, or false
	 * */
	public boolean addAllAccountEntity(ArrayList<AccountInfo> array){
		for(AccountInfo info : array){
			if(addAccountEntity(info) == false){
				return false;
			}
		}
		return true;
	}


    /**
     * Add an account information
    * @param       info     Data class of account
    * @return   Return true if it success, or false         
     * */
    public boolean addAccountEntity(AccountInfo info){
        if(info == null){
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ACCOUNT_ID, info.account_id);
        values.put(KEY_TITLE, info.title);
        values.put(KEY_CLOSE_DATE, info.close_date);
        values.put(KEY_CATEGORY, info.category);
        values.put(KEY_CARDCODE, info.card_code);
        
        long result = db.insert(TABLE_ACCOUNTS, null, values);
        if(result == -1){
            return false;
        }
        db.close();
        return true;
    }
    
    
    /**
     * Find account entity by given title
     * @param       accountTitle        Account id
     * @return      Return AccountsEntity when DB helper found appreciate record
     * */
    public AccountInfo getAccountByCardCode(int cardcode){
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ACCOUNTS + 
        		" WHERE " + KEY_CARDCODE + " = '"+ cardcode + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        AccountInfo entityInfo = null;
        try{
            cursor = db.rawQuery(selectQuery, null);
        }catch(SQLiteException e){
            db.close();
            return null;
        }
        if(cursor.moveToFirst()){
            entityInfo = new AccountInfo();
            entityInfo.account_id = cursor.getString(0);
            entityInfo.title = cursor.getString(1);
            entityInfo.close_date = cursor.getInt(2);
            entityInfo.category = cursor.getString(3);
            entityInfo.card_code = cursor.getInt(4);
            
        }
        cursor.close();
        db.close();
        return entityInfo;
    }


    /**
     * Re-create table after drop table 
     * */
    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Get All records
     * @return	Returns all record of the table
     * @see	AccountInfo
     * */
	public ArrayList<AccountInfo> getAllRecord() {
		// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ACCOUNTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery(selectQuery, null);
        }catch(SQLiteException e){
            db.close();
            return null;
        }
        ArrayList<AccountInfo> array = new ArrayList<AccountInfo>();
        if(cursor.moveToFirst()){
        	do{
        		AccountInfo entityInfo = new AccountInfo();
                entityInfo.account_id = cursor.getString(0);
                entityInfo.title = cursor.getString(1);
                entityInfo.close_date = cursor.getInt(2);
                entityInfo.category = cursor.getString(3);
                entityInfo.card_code = cursor.getInt(4);
                array.add(entityInfo);
        	}while(cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return array;
	}
}
