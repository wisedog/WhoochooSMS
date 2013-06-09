/*
  Copyright 2012 Jongha Kim(me@wisedog.net)
  
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

import android.os.Bundle;
import net.wisedog.android.whooing.sms.br.SmsFilter;
import net.wisedog.android.whooing.sms.br.SmsFilterData;
import net.wisedog.android.whooing.sms.util.WhooingSmsUtil;
import junit.framework.TestCase;

public class SmsFilterTest extends TestCase {
    private SmsFilter mFilter = null;

    @Override
	protected void setUp() throws Exception {
    	mFilter = new SmsFilter();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		mFilter = null;
		super.tearDown();
	}

	public void testFilterSender() {
        assertEquals(1, mFilter.filteringSender("15886700"));
        assertEquals(-1, mFilter.filteringSender("1112222"));
        assertEquals(-1, mFilter.filteringSender(null));
    }
    
    public void testFilterMsgKEB(){
        Bundle b = mFilter.filterMessage("외환카드 홍길*님 0*0*\n일시불      22,000원\n음식점이여 05/21 19:48\n누적       1,234,567",  SmsFilterData.CODE_KEB);
        assertNotNull(b);
        
        assertEquals(20130521, b.getInt(SmsFilter.KEY_DATE, -1));
        assertEquals("음식점이여", b.getString(SmsFilter.KEY_ITEM));
        assertEquals(22000, b.getInt(SmsFilter.KEY_MONEY));
        assertEquals(SmsFilterData.CODE_KEB, b.getInt(SmsFilter.KEY_CARDNAME, -1));
    }
    
    public void testFilterMsgLotte(){
    	SmsFilter obj = new SmsFilter();
        Bundle b = obj.filterMessage( "롯데 홍길*(0*0*) 6,800원 일시불 05/22 12:38 카페소리 누적1,710,938원", SmsFilterData.CODE_LOTTE);
        assertNotNull(b);
        
        assertEquals(20130522, b.getInt(SmsFilter.KEY_DATE, -1));
        assertEquals("카페소리", b.getString(SmsFilter.KEY_ITEM));
        assertEquals(6800, b.getInt(SmsFilter.KEY_MONEY));
        assertEquals(SmsFilterData.CODE_LOTTE, b.getInt(SmsFilter.KEY_CARDNAME, -1));
    }
    
    //
    public void testFilterMsgKB(){
    	Bundle b = mFilter.filterMessage("KB국민카드 0*0*\n홍길동님\n05/10 09:05\n10,000원\n유니세프기금\n누적 10,000원", SmsFilterData.CODE_KB);
        assertNotNull(b);
        
        assertEquals(20130510, b.getInt(SmsFilter.KEY_DATE, -1));
        assertEquals("유니세프기금", b.getString(SmsFilter.KEY_ITEM));
        assertEquals(10000, b.getInt(SmsFilter.KEY_MONEY));
        assertEquals(SmsFilterData.CODE_KB, b.getInt(SmsFilter.KEY_CARDNAME, -1)); 
    }
    
  //
    public void testFilterMsgWoori(){
    	Bundle b = mFilter.filterMessage("우리카드(0*0*)홍*동님.03/31 23:52.일시불7,040원.누적금액738,285원.이니시스_블리자드", SmsFilterData.CODE_WOORI);
        assertNotNull(b);
        
        assertEquals(20130331, b.getInt(SmsFilter.KEY_DATE, -1));
        assertEquals("이니시스_블리자드", b.getString(SmsFilter.KEY_ITEM));
        assertEquals(7040, b.getInt(SmsFilter.KEY_MONEY));
        assertEquals(SmsFilterData.CODE_WOORI, b.getInt(SmsFilter.KEY_CARDNAME, -1));
    }
    
    public void testFilterMsgWooriCheck(){
        Bundle b = mFilter.filterMessage( "[체크.승인]\n7,000원\n우리카드(0*0*)홍*동님\n05/16 19:49\n파티세리미뇽", 
        		SmsFilterData.CODE_WOORICHECK);
        assertNotNull(b);
        
        assertEquals(20130516, b.getInt(SmsFilter.KEY_DATE, -1));
        assertEquals("파티세리미뇽", b.getString(SmsFilter.KEY_ITEM));
        assertEquals(7000, b.getInt(SmsFilter.KEY_MONEY));
        assertEquals(SmsFilterData.CODE_WOORICHECK, b.getInt(SmsFilter.KEY_CARDNAME, -1));
        assertEquals(true, b.getBoolean(SmsFilter.KEY_CHECKCARD, false));
    }
    
    public void testFilterMsgHana(){
        Bundle b = mFilter.filterMessage("하나SK(9*0*)이*준님 01/29 00:34 제트(Z) 일시불/17,000원/누적1,025,765원", SmsFilterData.CODE_HANA);
        assertNotNull(b);
        
        assertEquals(20130129, b.getInt(SmsFilter.KEY_DATE, -1));
        assertEquals("제트(Z)", b.getString(SmsFilter.KEY_ITEM));
        assertEquals(17000, b.getInt(SmsFilter.KEY_MONEY));
        assertEquals(SmsFilterData.CODE_HANA, b.getInt(SmsFilter.KEY_CARDNAME, -1));
    }
    

    public void testFilterMsgShinhan(){
        Bundle b = mFilter.filterMessage("신한카드승인홍길동님 05/22 22:38 (일시불)4,200원 (주)코리아세 누적586,700원", SmsFilterData.CODE_SHINHAN);
        assertNotNull(b);
        
        assertEquals(20130522, b.getInt(SmsFilter.KEY_DATE, -1));
        assertEquals("(주)코리아세", b.getString(SmsFilter.KEY_ITEM));
        assertEquals(4200, b.getInt(SmsFilter.KEY_MONEY));
        assertEquals(SmsFilterData.CODE_SHINHAN, b.getInt(SmsFilter.KEY_CARDNAME, -1));
    }
    
    public void testFilterMsgSamsung1(){
        Bundle b = mFilter.filterMessage("삼성카드승인\n05/16 08:17\n1,500원\n일시불\n삼성에버랜드(주)\n*누적\n72,600원",SmsFilterData.CODE_SAMSUNG);
        assertNotNull(b);
        
        assertEquals(20130516, b.getInt(SmsFilter.KEY_DATE, -1));
        assertEquals("삼성에버랜드(주)", b.getString(SmsFilter.KEY_ITEM));
        assertEquals(1500, b.getInt(SmsFilter.KEY_MONEY));
        assertEquals(SmsFilterData.CODE_SAMSUNG, b.getInt(SmsFilter.KEY_CARDNAME, -1));
        assertEquals(false, b.getBoolean(SmsFilter.KEY_CHECKCARD, false));
    }
    
    public void testFilterMsgSamsungCheck(){
        Bundle b = mFilter.filterMessage("(삼성체크카드)\n05/23 12:52\nN카페보라매점\n7,500원\n일시불사용\n감사합니다", SmsFilterData.CODE_SAMSUNG);
        assertNotNull(b);
        
        assertEquals(20130523, b.getInt(SmsFilter.KEY_DATE, -1));
        assertEquals("N카페보라매점", b.getString(SmsFilter.KEY_ITEM));
        assertEquals(7500, b.getInt(SmsFilter.KEY_MONEY));
        assertEquals(SmsFilterData.CODE_SAMSUNG, b.getInt(SmsFilter.KEY_CARDNAME, -1));
        assertEquals(true, b.getBoolean(SmsFilter.KEY_CHECKCARD, false));
        
    }
    
    public void testFilterMsgHyundai1(){
    	Bundle b = mFilter.filterMessage("[현대카드M3]-승인\n홍길*님\n37,500원(일시불)\n서울불고기\n누적:1,189,280원", SmsFilterData.CODE_HYUNDAI);
    	assertNotNull(b);
    	
    	assertEquals(WhooingSmsUtil.getTodayYYYYMMDDInt(), b.getInt(SmsFilter.KEY_DATE, -1));
        assertEquals("서울불고기", b.getString(SmsFilter.KEY_ITEM));
        assertEquals(37500, b.getInt(SmsFilter.KEY_MONEY));
        assertEquals(SmsFilterData.CODE_HYUNDAI, b.getInt(SmsFilter.KEY_CARDNAME, -1));
        assertEquals(false, b.getBoolean(SmsFilter.KEY_CHECKCARD, false));
    	
    }
    
    public void testFilterMsgHyundai(){
    	Bundle b = mFilter.filterMessage("[현대카드C]-승인\n정**님\n03/25 07:35\n13,500원(일시불)\n택시(서울)", SmsFilterData.CODE_HYUNDAI);
    	assertNotNull(b);
    	
    	assertEquals(20130325, b.getInt(SmsFilter.KEY_DATE, -1));
        assertEquals("택시(서울)", b.getString(SmsFilter.KEY_ITEM));
        assertEquals(13500, b.getInt(SmsFilter.KEY_MONEY));
        assertEquals(SmsFilterData.CODE_HYUNDAI, b.getInt(SmsFilter.KEY_CARDNAME, -1));
        assertEquals(true, b.getBoolean(SmsFilter.KEY_CHECKCARD, false));
    }
    
}
