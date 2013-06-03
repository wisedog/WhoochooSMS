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

public class SmsFilterData {
    static public final int CODE_NONE = 0;
    static public final int CODE_KEB = 1;
    static public final int CODE_SAMSUNG = 2;
    static public final int CODE_LOTTE = 3;
    static public final int CODE_KB = 4;
    static public final int CODE_WOORI = 5;
    static public final int CODE_WOORICHECK = 6;
    static public final int CODE_HYUNDAI = 7;
    static public final int CODE_SHINHAN = 8;
    static public final int CODE_HANA = 9;
    static public final int CODE_NH = 10;
    
    static public final String[] CARD_COMPANY_NAME = {
        "",
    	"외환카드",
    	"삼성카드",
    	"롯데카드",
    	"KB국민카드",
    	"우리카드",
    	"우리체크카드",
    	"현대카드",
    	"신한카드",
    	"하나SK카드",
    	"농협카드"
    };
    
    
    // Be sure to check that below order and upper constants must be matched.
	static public final String[] SENDER_NUMBER = {
	    "",    // None
		"15886700",	//외환
		"15888900",   //삼성
		"15888300",   //롯데
		"15881688",   //KB
		"15889955",   //우리
		"15884000",    //우리체크
		"15776200",   //현대
		"15447200",   //신한
		"15991155",   //하나
		"15881600"   //농협
	};
}
