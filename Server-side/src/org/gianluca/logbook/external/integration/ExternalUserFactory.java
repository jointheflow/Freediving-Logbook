package org.gianluca.logbook.external.integration;

import org.gianluca.logbook.helper.LogbookConstant;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.User;

/*Creates a user accessing specific external platform, using OAuth token*/
public class ExternalUserFactory {
	public static ExternalUser createExternalUser(String token, int platform) throws PlatformNotManagedException {
		ExternalUser extUser = null;
		switch (platform) {
			
			case LogbookConstant.FACEBOOK_PLATFORM: {
				//using specific API to connect Facebook
				FacebookClient facebookClient = new DefaultFacebookClient(token, LogbookConstant.FACEBOOK_SECRET_APP, Version.VERSION_2_4);
				User user = facebookClient.fetchObject("me", User.class);
				//instantiate FacebookUser
				FacebookUser fbUser = new FacebookUser();
				fbUser.setId(user.getId());
				fbUser.setName(user.getName());
				//assign fbUser to extUser
				extUser = fbUser;
			}
			break;
			
			default:
				throw new PlatformNotManagedException("Platform with code:"+ platform +" is not managed!");
			}
		
		
		return extUser;
	}
	
	/*Check if external token is vaild*/
	public static void checkExternalToken(String token, int platform) throws PlatformNotManagedException {
		
		switch (platform) {
			
			case LogbookConstant.FACEBOOK_PLATFORM: {
				//using specific API to connect Facebook
				FacebookClient facebookClient = new DefaultFacebookClient(token, LogbookConstant.FACEBOOK_SECRET_APP, Version.VERSION_2_4);
				@SuppressWarnings("unused")
				User user = facebookClient.fetchObject("me", User.class);
				
			}
			break;
			
			default:
				throw new PlatformNotManagedException("Error checking Token:"+ token +" for platform "+ platform);
			}
		
		
		
	}
}
