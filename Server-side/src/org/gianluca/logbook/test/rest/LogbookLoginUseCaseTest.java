package org.gianluca.logbook.test.rest;

import static org.junit.Assert.*;

import org.gianluca.logbook.helper.LogbookConstant;
import org.json.JSONArray;
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
	private String externalToken="CAAB4GhgAGN0BAAo4nXGkbG3Als92du72M5ciUcxmewbl5eNLGbZBGX85Abw3LMTa9pkqqQkdMnZB8DTyRp8jrM9DM6HrVxOLZCBZCDVpQ9cxyxORUyQK8AV7pVzDOdAZAUjXgaN1sKzM2ZBQiZAixJgSA69b2Rp8ZCwIPLvS3Dp0vM50WQqM9CSak18L0ZCUX7G66mZAJIVjyTUeiNqvfTDMVU";
	//externalId associated to "freediving logbook" user on Facebook
	private String externalId = "125927547759071";
	private String externalName ="freediving logbook";
	private String email = null;
	private String freediverId = null;
	
	private int externalPlatform=LogbookConstant.FACEBOOK_PLATFORM;
	//Dive sessions data global
	private int deepUnit = LogbookConstant.DEEP_METER;
	private int tempUnit = LogbookConstant.TEMPERATURE_CELSIUS;
	private int weightUnit = LogbookConstant.WEIGHT_KILOGRAM;
	
	//Dive session1
	private String ds1_date="100000";
	private double ds1_deep = 35.6;
	private String ds1_equipment = "mask, lanyard, dive suit 5.5 mm"; 
	private String ds1_location = "Elba Island - margidore";
	private String ds1_meteo=  "sunny";
	private String ds1_note= "note 1";
	private String ds1_waterTemp = "20.0";
    private double ds1_weight =5.0;
	
    //Dive session2
  	private String ds2_date="200000";
  	private double ds2_deep = 40.0;
  	private String ds2_equipment = "mask, lanyard, dive suit 6.0 mm"; 
  	private String ds2_location = "Giglio Island";
  	private String ds2_meteo=  "cloudy";
  	private String ds2_note= "note 2";
  	private String ds2_waterTemp = "20.0";
    private double ds2_weight =5.5;
  	
    //Dive session3
  	private String ds3_date="300000";
  	private double ds3_deep = 35.0;
  	private String ds3_equipment = "mask, dive suit 4.5 mm"; 
  	private String ds3_location = "Giglio Island";
  	private String ds3_meteo=  "windy";
  	private String ds3_note= "note 3";
  	private String ds3_waterTemp = "21.0";
    private double ds3_weight =5.5;
  	    
	private String freediverLoginRequest="http://localhost:8888/app/freediver/login?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	private String freediverRemoveRequest="http://localhost:8888/app/freediver/remove?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	private String diveSessionAddRequest ="http://localhost:8888/app/freediver/divesession/add";
	
	/*Add a freediver and 3 dive sessions*/
	@Before
	public void setUp(){
		try {
			System.out.println("-------START setUp()--------");
			/*----START Login of a  freediver-----*/ 
			System.out.println("Login as a freediver");
			Client loginClient = new Client(Protocol.HTTP);
			Request loginRequest = new Request(Method.GET, freediverLoginRequest);
			System.out.println("Executing GET "+ freediverLoginRequest);
			
			Response loginResponse = loginClient.handle(loginRequest);
			JSONObject jsonobj = new JsonRepresentation(loginResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj.toString());
			
			freediverId = (String)jsonobj.get("id");
			String externalId = (String)jsonobj.get("externalId");
			
			System.out.println("Login executed by external free diver:"+ freediverId);
			/*----END Login of a freediver----*/
			
			
			/*----Start adding some dives-- */
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep", Double.toString(ds1_deep));
			fParam_prov.add("equipment", ds1_equipment); 
			fParam_prov.add("location", ds1_location);
			fParam_prov.add("meteo",ds1_meteo);
			fParam_prov.add("note", ds1_note);
			fParam_prov.add("water_temp", ds1_waterTemp);
		    fParam_prov.add("weight", Double.toString(ds1_weight));
		    fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds2_date);
			fParam_prov.add("deep", Double.toString(ds2_deep));
			fParam_prov.add("equipment", ds2_equipment); 
			fParam_prov.add("location", ds2_location);
			fParam_prov.add("meteo",ds2_meteo);
			fParam_prov.add("note", ds2_note);
			fParam_prov.add("water_temp",ds2_waterTemp);
		    fParam_prov.add("weight", Double.toString(ds2_weight));
		    fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			providerResponse = providerClient.handle(providerRequest);
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds3_date);
			fParam_prov.add("deep", Double.toString(ds3_deep));
			fParam_prov.add("equipment", ds3_equipment); 
			fParam_prov.add("location", ds3_location);
			fParam_prov.add("meteo",ds3_meteo);
			fParam_prov.add("note", ds3_note);
			fParam_prov.add("water_temp", ds3_waterTemp);
		    fParam_prov.add("weight", Double.toString(ds3_weight));
		    fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			providerResponse = providerClient.handle(providerRequest);
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			/* END adding some dives--*/
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			System.out.println("-------END setUp()--------");
			
		}
		
	}

	@Test
	public void test() {
		try {
			System.out.println("-------START test()--------");	
				
			//insert waiting 5 second for eventually consistence.
			Thread.sleep(5000);	
			
			//execute Login
			System.out.println("Login as a freediver");
			Client loginClient = new Client(Protocol.HTTP);
			Request loginRequest = new Request(Method.GET, freediverLoginRequest);
			System.out.println("Executing GET "+ freediverLoginRequest);
			
			Response loginResponse = loginClient.handle(loginRequest);
			JSONObject jsonobj = new JsonRepresentation(loginResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj.toString());
			
				
			//TODO check freediving data and dive session data
			assertTrue(((String)jsonobj.get("id")).equals(freediverId));
			assertTrue(((String)jsonobj.get("externalId")).equals(externalId));
			assertTrue(((String)jsonobj.get("result")).equals("OK"));
			assertTrue(((String)jsonobj.get("message")).equals("Freediver login executed"));
			assertTrue(((String)jsonobj.get("externalToken")).equals(externalToken));
			
			JSONArray jsonDiveSessions = jsonobj.getJSONArray("diveSessions");
			assertTrue(jsonDiveSessions.length()==3);
			JSONObject jsonDs1 = jsonDiveSessions.getJSONObject(0);
			JSONObject jsonDs2 = jsonDiveSessions.getJSONObject(1);
			JSONObject jsonDs3 = jsonDiveSessions.getJSONObject(2);
			
			assertTrue(((String)jsonDs1.get("note")).equals(ds3_note));
			assertTrue(((Double)jsonDs1.getDouble("waterTempAsCelsius")).doubleValue()==new Double(ds3_waterTemp));
			assertTrue(((String)jsonDs1.get("externalToken")).equals(externalToken));
			assertTrue(((String)jsonDs1.get("equipment")).equals(ds3_equipment));
			assertTrue(((Double)jsonDs1.getDouble("deepAsMeter")).doubleValue()==new Double(ds3_deep));
			assertTrue(((String)jsonDs1.get("meteoDesc")).equals(ds3_meteo));
			assertTrue(((String)jsonDs1.get("locationDesc")).equals(ds3_location));
			assertTrue(((String)jsonDs1.get("diveDate")).equals("Thu Jan 01 01:05:00 CET 1970"));
			assertTrue(((Double)jsonDs1.get("weightAsKilogram")).doubleValue()==new Double(ds3_weight));
			
			assertTrue(((String)jsonDs2.get("note")).equals(ds2_note));
			assertTrue(((Double)jsonDs2.getDouble("waterTempAsCelsius")).doubleValue()==new Double(ds2_waterTemp));
			assertTrue(((String)jsonDs2.get("externalToken")).equals(externalToken));
			assertTrue(((String)jsonDs2.get("equipment")).equals(ds2_equipment));
			assertTrue(((Double)jsonDs2.getDouble("deepAsMeter")).doubleValue()==new Double(ds2_deep));
			assertTrue(((String)jsonDs2.get("meteoDesc")).equals(ds2_meteo));
			assertTrue(((String)jsonDs2.get("locationDesc")).equals(ds2_location));
			assertTrue(((String)jsonDs2.get("diveDate")).equals("Thu Jan 01 01:03:20 CET 1970"));
			assertTrue(((Double)jsonDs2.get("weightAsKilogram")).doubleValue()==new Double(ds2_weight));
			
			
			assertTrue(((String)jsonDs3.get("note")).equals(ds1_note));
			assertTrue(((Double)jsonDs3.getDouble("waterTempAsCelsius")).doubleValue()==new Double(ds1_waterTemp));
			assertTrue(((String)jsonDs3.get("externalToken")).equals(externalToken));
			assertTrue(((String)jsonDs3.get("equipment")).equals(ds1_equipment));
			assertTrue(((Double)jsonDs3.getDouble("deepAsMeter")).doubleValue()==new Double(ds1_deep));
			assertTrue(((String)jsonDs3.get("meteoDesc")).equals(ds1_meteo));
			assertTrue(((String)jsonDs3.get("locationDesc")).equals(ds1_location));
			assertTrue(((String)jsonDs3.get("diveDate")).equals("Thu Jan 01 01:01:40 CET 1970"));
			//assertTrue(((Double)jsonDs3.get("weightAsKilogram")).doubleValue()==new Double(ds1_weight));
			
			System.out.println(jsonDiveSessions.length());
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}finally {		
			System.out.println("-------END test()--------");
		}
	}
	
	/*Remove freediver and all its sessions*/
	@After
	public void tearDown() {
		try {
			System.out.println("-------START tearDown()--------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, freediverRemoveRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("freediver_id", freediverId);
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
		}catch (Exception e) {
			e.printStackTrace();
			//TODO manage all excpetions
			
		}finally {
			System.out.println("-------END tearDown()--------");
		}
	}

}
