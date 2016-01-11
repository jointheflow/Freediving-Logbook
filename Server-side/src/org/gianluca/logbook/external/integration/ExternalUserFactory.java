package org.gianluca.logbook.external.integration;

import org.gianluca.logbook.helper.LogbookConstant;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.FacebookType;
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
	
	public static void facebookPublishMsg(String token, String link, String title, String msg) throws PlatformNotManagedException {
		//create Facebook client
		FacebookClient facebookClient = new DefaultFacebookClient(token, LogbookConstant.FACEBOOK_SECRET_APP, Version.VERSION_2_4);
		FacebookType publishMessageResponse =
				/*  facebookClient.publish("me/feed", FacebookType.class,
				    Parameter.with("message", "RestFB test"));*/
		
		/*facebookClient.publish("me/feed", FacebookType.class,
			    Parameter.with("link", "https://app-nea-it.appspot.com/webclient/index.html"),
			    Parameter.with("name", title),
			    //Parameter.with("caption", msg)
			    Parameter.with("description", msg)
			    );*/
				facebookClient.publish("me/freedivinglogbook:complete", FacebookType.class,
					    Parameter.with("dive_session", "http://samples.ogp.me/200248120322899")  
					    );

				System.out.println("Published message ID: " + publishMessageResponse.getId());
	}
}
