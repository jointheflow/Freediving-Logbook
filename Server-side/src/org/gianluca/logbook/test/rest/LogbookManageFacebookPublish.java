package org.gianluca.logbook.test.rest;

import static org.junit.Assert.*;

import org.gianluca.logbook.external.integration.ExternalUserFactory;
import org.gianluca.logbook.external.integration.PlatformNotManagedException;
import org.gianluca.logbook.helper.LogbookConstant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LogbookManageFacebookPublish {
	//get new toke from FB https://developers.facebook.com/tools/explorer
	private String externalToken= LogbookConstant.FACEBOOK_ACCESSS_TOKEN;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
		try {
			ExternalUserFactory.facebookPublishMsg(externalToken);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
