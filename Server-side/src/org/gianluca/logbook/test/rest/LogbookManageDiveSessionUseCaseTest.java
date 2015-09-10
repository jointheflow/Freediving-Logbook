package org.gianluca.logbook.test.rest;

import static org.junit.Assert.assertTrue;

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
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;

public class LogbookManageDiveSessionUseCaseTest {
	//set test constants
	//get new toke from FB https://developers.facebook.com/tools/explorer
	private String externalToken="CAAB4GhgAGN0BAJVzcLPkUirXlYbJAYqHgF6lwayXA5L8NHn1LUrpI4AA4zigkNBOpx2W5LOR7duAkRDZA2xA1EAzGA2BZAH3LtQKiobx2vqgprRI63Mmyqr0Jw1sm6SX0L3BWNxJgs95O9gtSUigAPr2nmMHVfKHZBBqj0E1tluG2PJzvlIjeuKWnYBuJKog4PqbMzIZAnYyzIgFFPPK";
	//externalId associated to "freediving logbook" user on Facebook
	private String externalId = "125927547759071";
	@SuppressWarnings("unused")
	private String externalName ="freediving logbook";
	@SuppressWarnings("unused")
	private String email = null;
	private String freediverId = null;
	
	private int externalPlatform=LogbookConstant.FACEBOOK_PLATFORM;
	//Dive sessions data global
	private int deepUnit = LogbookConstant.DEEP_METER;
	private int tempUnit = LogbookConstant.TEMPERATURE_CELSIUS;
	private int weightUnit = LogbookConstant.WEIGHT_KILOGRAM;
	
	//Dive session1
	private String ds1_date="07-03-2015";
	private double ds1_deep = 35.6;
	private String ds1_equipment = "mask, lanyard, dive suit 5.5 mm"; 
	private String ds1_location = "Elba Island - margidore";
	private String ds1_meteo=  "sunny";
	private String ds1_note= "note 1";
	private String ds1_waterTemp = "20.0";
    private double ds1_weight =5.0;
	
    //Dive session2
  	private String ds2_date="08-08-2015";
  	private double ds2_deep = 40.0;
  	private String ds2_equipment = "mask, lanyard, dive suit 6.0 mm"; 
  	private String ds2_location = "Giglio Island";
  	private String ds2_meteo=  "cloudy";
  	private String ds2_note= "note 2";
  	private String ds2_waterTemp = "20.0";
    private double ds2_weight =5.5;
    
    private int divePageSize1=1;
    private int divePageSize2=2;
    private String freediverLoginRequestNoParams="http://localhost:8888/app/freediver/login";    
	private String freediverLoginRequest="http://localhost:8888/app/freediver/login?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	private String freediverRemoveRequest="http://localhost:8888/app/freediver/remove?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	private String diveSessionAddRequest ="http://localhost:8888/app/freediver/divesession/add";
	
	@Before
	/*Executing login will create a user*/
	public void setUp(){
		try {
			
			
			System.out.println("-------START setUp()--------");
			/*----START Login of a  freediver-----*/ 
			System.out.println("Login as a freediver");
			Client loginClient = new Client(Protocol.HTTP);
			Request loginRequest = new Request(Method.GET, freediverLoginRequest+"&dive_page_size="+divePageSize1);
			System.out.println("Executing GET "+ freediverLoginRequest+"&dive_page_size="+divePageSize1);
			
			Response loginResponse = loginClient.handle(loginRequest);
			JSONObject jsonobj = new JsonRepresentation(loginResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj.toString());
			freediverId = (String)jsonobj.get("id");
			
			System.out.println("Login executed by external free diver:"+ freediverId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			System.out.println("-------END setUp()--------");
			
		}
	}
	
	@Test
	public void addDiveSession() {
		try {
			Thread.sleep(5000);
			System.out.println("-----Start addDiveSession()---------");
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
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(((String)jsonobj_prov.get("note")).equals(ds1_note));
			assertTrue(((Double)jsonobj_prov.getDouble("waterTempAsCelsius")).doubleValue()==new Double(ds1_waterTemp));
			assertTrue(jsonobj_prov.getDouble("waterTempAsFahrehneit")==68.0);
			assertTrue(((String)jsonobj_prov.get("externalToken")).equals(externalToken));
			assertTrue(((String)jsonobj_prov.get("equipment")).equals(ds1_equipment));
			assertTrue(((Double)jsonobj_prov.getDouble("deepAsMeter")).doubleValue()==new Double(ds1_deep));
			assertTrue(jsonobj_prov.getDouble("deepAsFeet")==116.79790026200001);
			assertTrue(((String)jsonobj_prov.get("meteoDesc")).equals(ds1_meteo));
			assertTrue(((String)jsonobj_prov.get("locationDesc")).equals(ds1_location));
			assertTrue(((String)jsonobj_prov.get("diveDate")).equals("Sat Mar 07 00:00:00 CET 2015"));
			assertTrue(jsonobj_prov.getDouble("weightAsKilogram")== ds1_weight);
			assertTrue(jsonobj_prov.getDouble("weightAsPound")== 11.023);
			assertTrue(jsonobj_prov.get("message").equals("Dive session added"));
			assertTrue(jsonobj_prov.get("result").equals("OK"));
			
			assertTrue(providerResponse.getStatus().getCode()==Status.SUCCESS_OK.getCode());
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("-----End addDiveSession()---------");
		
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
			//insert waiting 5 second for eventually consistence.
			Thread.sleep(5000);
			
		}catch (Exception e) {
			e.printStackTrace();
			//TODO manage all excpetions
			
		}finally {
			
				
			
			System.out.println("-------END tearDown()--------");
		}
	}
}
