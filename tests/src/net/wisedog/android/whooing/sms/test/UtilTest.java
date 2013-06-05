package net.wisedog.android.whooing.sms.test;

import net.wisedog.android.whooing.sms.util.WhooingSmsUtil;
import junit.framework.TestCase;

public class UtilTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testConvertWhooingDate(){
		assertEquals(20130605, WhooingSmsUtil.convertWhooingDate("06/05"));
		assertEquals(20131231, WhooingSmsUtil.convertWhooingDate("12/31"));
	}

}
