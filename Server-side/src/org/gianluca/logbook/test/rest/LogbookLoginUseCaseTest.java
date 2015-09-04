package org.gianluca.logbook.test.rest;

import static org.junit.Assert.*;

import java.util.Date;

import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;
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

public class LogbookLoginUseCaseTest {
	//set test constants
	//get new toke from FB https://developers.facebook.com/tools/explorer
	private String externalToken="CAAB4GhgAGN0BAB0zTiGmkKHF2ZBkPix61O7CA4a8W8ZCqxiXGHFc06yuoAazZCEc5qHKnpTmPQtTmoLVaeTSar03FyTkHuKZCDpAMdvlcdzI3eTt8HSfId4F6tqxNHAr82WdK7idvmueCc7dCYsODFwZBlIKZBC0Tve1r5ZCvL89VkYZBFc1h3BlnqdnAlBbnnkO8azU3M2pL8dIn6GqZCpn9";
	//externalId associated to "freediving logbook" user on Facebook
	private String externalId = "125927547759071";
	private String externalName ="freediving logbook";
	private String email = null;
	
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
	private String diveSessionAddRequest ="http://localhost:8888/app/freediver/divesession/add";
	@Before  
    public void setUp() {  
         
        //create an entity instance of Freediver and 3 entity of DiveSession
        try {
    	
        	Freediver fd = LogbookDAO.addFreediver(externalId, externalName, email, LogbookConstant.FACEBOOK_PLATFORM);
         	System.out.println("freediver created:" + fd);
         	DiveSession ds1 = LogbookDAO.addDiveSession(fd.getId(), ds1_date, ds1_deep, ds1_equipment, ds1_location, null, ds1_meteo, ds1_note, ds1_waterTemp, ds1_weight, deepUnit, tempUnit, weightUnit);
         	System.out.println("dive session added:" + ds1);
         	DiveSession ds2 = LogbookDAO.addDiveSession(fd.getId(), ds2_date, ds2_deep, ds2_equipment, ds2_location, null, ds2_meteo, ds2_note, ds2_waterTemp, ds2_weight, deepUnit, tempUnit, weightUnit);
         	System.out.println("dive session added:" + ds2);
         	DiveSession ds3 = LogbookDAO.addDiveSession(fd.getId(), ds3_date, ds3_deep, ds3_equipment, ds3_location, null, ds3_meteo, ds3_note, ds3_waterTemp, ds3_weight, deepUnit, tempUnit, weightUnit);
         	System.out.println("dive session added:" + ds3);
        }catch (Exception e) {
        	
        	
        }
    }  
	

	@Test
	public void test() {
		System.out.println("-->Start test");
		//fail("Not yet implemented");
		try {
			
			
			/*----START Login of a  freediver-----*/ 
			System.out.println("Login as a freediver");
			Client loginClient = new Client(Protocol.HTTP);
			Request loginRequest = new Request(Method.GET, freediverLoginRequest);
			System.out.println("Executing GET "+ freediverLoginRequest);
			
			Response loginResponse = loginClient.handle(loginRequest);
			JSONObject jsonobj = new JsonRepresentation(loginResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj.toString());
			
			String freediverId = (String)jsonobj.get("id");
			String externalId = (String)jsonobj.get("externalId");
			
			System.out.println("Login executed by external free diver:"+ freediverId);
			/*----END Login of a freediver----*/
			
			
			/*----Start adding some dives-- */
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date","100000");
			fParam_prov.add("deep", Double.toString(ds1_deep));
			fParam_prov.add("equipment", ds1_equipment); 
			fParam_prov.add("location", ds1_location);
			fParam_prov.add("meteo",ds1_meteo);
			fParam_prov.add("note", ds1_note);
			fParam_prov.add("waterTemp", Double.toString(ds1_waterTemp));
		    fParam_prov.add("weight", Double.toString(ds1_weight));
		    fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			/* END adding some dives--*/
			
			System.out.println("-->End test");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(new JsonRepresentation(customerResponse.getEntityAsText()) );
	}

}
