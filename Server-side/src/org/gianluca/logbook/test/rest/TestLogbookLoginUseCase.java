package org.gianluca.logbook.test.rest;

import static org.junit.Assert.*;

import java.util.Date;

import org.gianluca.logbook.helper.LogbookConstant;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.ext.json.JsonRepresentation;

public class TestLogbookLoginUseCase {
	//set test constants
	//get new toke from FB https://developers.facebook.com/tools/explorer
	private String externalToken="CAANb5zgEgacBAKZCGqHQAhQYYaVoYw0J3IWFlV9MmfsEbLlKbX25BEzbNmwKZBDMUIFv9KbZBVTf7ZBuWNA5HFyz4hVbyDKGg7fGxsT3jZCudq1AR0LXkdrr3G7jAtVWY5p6rLAPzZBvlLwggLg9I1XUvpZBLaolxPtnanwime1cKmC7O0bcSlEN4smIKSlTqucrUMIu9GwoAZDZD";
	private int externalPlatform=LogbookConstant.FACEBOOK_PLATFORM;
	//Dive sessions data global
	private int deepUnit = LogbookConstant.DEEP_METER;
	private int tempUnit = LogbookConstant.TEMPERATURE_CELSIUS;
	private int weightUnit = LogbookConstant.WEIGHT_KILOGRAM;
	
	//Dive session1
	private Date ds1_date=new Date(100000);
	private double ds1_deep = 35.6;
	private String ds1_equipment = "mask, lanyard, dive suit 5.5 mm"; 
	private String ds1_location = "Elba Island - margidore";
	private String ds1_meteo=  "sunny";
	private String ds1_note= "katabasis course ssi level 3";
	private double ds1_waterTemp = 20.0;
    private double ds1_weight =5.0;
	
    //Dive session2
  	private Date ds2_date=new Date(200000);
  	private double ds2_deep = 40.0;
  	private String ds2_equipment = "mask, lanyard, dive suit 6.0 mm"; 
  	private String ds2_location = "Giglio Island";
  	private String ds2_meteo=  "cloudy";
  	private String ds2_note= "training";
  	private double ds2_waterTemp = 20.0;
    private double ds2_weight =5.5;
  	
    //Dive session3
  	private Date ds3_date=new Date(300000);
  	private double ds3_deep = 35.0;
  	private String ds3_equipment = "mask, dive suit 4.5 mm"; 
  	private String ds3_location = "Giglio Island";
  	private String ds3_meteo=  "windy";
  	private String ds3_note= "training";
  	private double ds3_waterTemp = 21.0;
    private double ds3_weight =5.5;
  	    
	private String freediverLoginRequest="http://localhost:8888/app/freediver/login?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	
	
	

	@Test
	public void test() {
		System.out.println("-->Start test");
		//fail("Not yet implemented");
		try {
			
			
			/*----START Creation of a  new customer-----*/ 
			System.out.println("Login as a freediver");
			Client loginClient = new Client(Protocol.HTTP);
			Request loginRequest = new Request(Method.GET, freediverLoginRequest);
			System.out.println("Executing GET "+ freediverLoginRequest);
			//create a post entity for Representation
			Form fParam = new Form();
			Response loginResponse = loginClient.handle(loginRequest);
			JSONObject jsonobj = new JsonRepresentation(loginResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj.toString());
			
			String freediverId = (String)jsonobj.get("id");
			String externalId = (String)jsonobj.get("externalId");
			
			System.out.println("Login executed by external free diver:"+ freediverId);
			/*----END Creation of a new customer----*/
			
			/*----START creation  of new provider ----*/
			/*System.out.println("Creating a new provider");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, providerSignupRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			fParam_prov.add("email", providerEmail);
			fParam_prov.add("password", providerPassword);
			fParam_prov.add("name", providerName);	
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//gett the customerID
			providerId = (Long)jsonobj_prov.get("id");
			providerToken = jsonobj_prov.getJSONObject("token").getLong("id");
			System.out.println("Provider created "+providerId);
			/*----END creation of a new provider-----*/
			System.out.println("-->End test");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(new JsonRepresentation(customerResponse.getEntityAsText()) );
	}

}
