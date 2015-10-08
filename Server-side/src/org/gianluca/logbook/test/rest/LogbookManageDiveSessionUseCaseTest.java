package org.gianluca.logbook.test.rest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
	private String externalToken=LogbookConstant.FACEBOOK_ACCESSS_TOKEN;
	//externalId associated to "freediving logbook" user on Facebook
	@SuppressWarnings("unused")
	private String externalId = "125927547759071";
	@SuppressWarnings("unused")
	private String externalName ="freediving logbook";
	@SuppressWarnings("unused")
	private String email = null;
	private String freediverId = null;
	private String sessionId=null;
	
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
  	private String ds2_date="08-03-2015";
  	private double ds2_deep = 35.6;
  	private String ds2_equipment = "mask only"; 
  	private String ds2_location = "Giglio Island";
  	private String ds2_meteo=  "cloudy";
  	private String ds2_note= "note 2";
  	private String ds2_waterTemp = "20.0";
    private double ds2_weight =5.0;
  	
    
    
    private int divePageSize1=1;
   	private String freediverLoginRequest=LogbookConstant.HOST_NAME+"/app/freediver/login?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	private String freediverRemoveRequest=LogbookConstant.HOST_NAME+"/app/freediver/remove?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	private String diveSessionAddRequest =LogbookConstant.HOST_NAME+"/app/freediver/divesession/add";
	private String diveSessionUpdateRequest =LogbookConstant.HOST_NAME+"/app/freediver/divesession/update";
	private String diveSessionRemoveRequest =LogbookConstant.HOST_NAME+"/app/freediver/divesession/remove";
	
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
		   
		    System.out.println("Executing POST "+ diveSessionAddRequest);
		    Client addClient = 	new Client(Protocol.HTTP);		
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = addClient.handle(providerRequest);
			jsonobj = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj.toString());
			sessionId = (String)jsonobj.get("id");
			
			
			
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
			
			System.out.println("-----Start addDiveSession()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds2_date);
			fParam_prov.add("deep", Double.toString(ds2_deep));
			fParam_prov.add("equipment", ds2_equipment); 
			fParam_prov.add("location", ds2_location);
			fParam_prov.add("meteo",ds2_meteo);
			fParam_prov.add("note", ds2_note);
			fParam_prov.add("water_temp", ds2_waterTemp);
		    fParam_prov.add("weight", Double.toString(ds2_weight));
		    fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(((String)jsonobj_prov.get("note")).equals(ds2_note));
			assertTrue(((Double)jsonobj_prov.getDouble("waterTempAsCelsius")).doubleValue()==new Double(ds2_waterTemp));
			assertTrue(jsonobj_prov.getDouble("waterTempAsFahrehneit")==68.0);
			assertTrue(((String)jsonobj_prov.get("externalToken")).equals(externalToken));
			assertTrue(((String)jsonobj_prov.get("equipment")).equals(ds2_equipment));
			assertTrue(((Double)jsonobj_prov.getDouble("deepAsMeter")).doubleValue()==new Double(ds2_deep));
			assertTrue(jsonobj_prov.getDouble("deepAsFeet")==116.79790026200001);
			assertTrue(((String)jsonobj_prov.get("meteoDesc")).equals(ds2_meteo));
			assertTrue(((String)jsonobj_prov.get("locationDesc")).equals(ds2_location));
			assertTrue(((String)jsonobj_prov.get("diveDate")).equals("Sun Mar 08 00:00:00 CET 2015"));
			assertTrue(jsonobj_prov.getDouble("weightAsKilogram")== ds2_weight);
			assertTrue(jsonobj_prov.getDouble("weightAsPound")== 11.023);
			assertTrue(jsonobj_prov.get("message").equals("Dive session added"));
			assertTrue(jsonobj_prov.get("result").equals("OK"));
			assertTrue(providerResponse.getStatus().getCode()==Status.SUCCESS_OK.getCode());
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSession()---------");
		
	}
	
	@Test
	public void addDiveSessionMissingExternalPlatformId() {
		try {
			
			System.out.println("-----Start addDiveSessionMissingExternalPlatformId()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_platform_id missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionErrorMissingExernalPlatormId()---------");
		
	}
	
	
	@Test
	public void updateAndRemoveDiveSession() {
		try {
			
			/*System.out.println("-----Start addDiveSession()---------");
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
			
			//get divesession of session added
			String divesessionId = jsonobj_prov.getString("id");
			System.out.println("-----End addDiveSession()---------");
			*/
			System.out.println("-----Start updateDiveSession()---------");
			//changed data
			String ds2_date = "07-04-2015";
			double ds2_deep = 40.1;
			String ds2_equipment = "nothing";
			String ds2_location = "rome";
			String ds2_meteo ="cludly";
			String ds2_note =" new note";
			String ds2_waterTemp = "23";
			double ds2_weight= 1;
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			//change date
			fParam_prov.add("dive_date", ds2_date);
			//change deep
			fParam_prov.add("deep", Double.toString(ds2_deep));
			//change equipment
			fParam_prov.add("equipment", ds2_equipment); 
			//change location
			fParam_prov.add("location", ds2_location);
			
			//change meteo
			fParam_prov.add("meteo",ds2_meteo);
			//change note
			fParam_prov.add("note", ds2_note);
			//change water temp
			fParam_prov.add("water_temp", ds2_waterTemp);
		    //change weight
			fParam_prov.add("weight", Double.toString(ds2_weight));
		    
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			
		
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			
			assertTrue(((String)jsonobj_prov.get("note")).equals(ds2_note));
			assertTrue(((Double)jsonobj_prov.getDouble("waterTempAsCelsius")).doubleValue()==new Double(ds2_waterTemp));
			
			assertTrue(((String)jsonobj_prov.get("externalToken")).equals(externalToken));
			assertTrue(((String)jsonobj_prov.get("equipment")).equals(ds2_equipment));
			assertTrue(((Double)jsonobj_prov.getDouble("deepAsMeter")).doubleValue()==new Double(ds2_deep));
			
			assertTrue(((String)jsonobj_prov.get("meteoDesc")).equals(ds2_meteo));
			assertTrue(((String)jsonobj_prov.get("locationDesc")).equals(ds2_location));
			assertTrue(((String)jsonobj_prov.get("diveDate")).equals("Tue Apr 07 00:00:00 CEST 2015"));
			assertTrue(jsonobj_prov.getDouble("weightAsKilogram")== ds2_weight);
			
			assertTrue(jsonobj_prov.get("message").equals("Dive session updated"));
			assertTrue(jsonobj_prov.get("result").equals("OK"));
			
			assertTrue(providerResponse.getStatus().getCode()==Status.SUCCESS_OK.getCode());
			System.out.println("-----End updateDiveSession()---------");
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSession()---------");
		
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

	@Test
	public void addDiveSessionMissingExternalToken() {
		try {
			
			System.out.println("-----Start addDiveSessionMissingExternalToken()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			//fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_token missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionMissingExernalToken()---------");
		
	}
	
	@Test
	public void addDiveSessionMissingFreediverId() {
		try {
			
			System.out.println("-----Start addDiveSessionMissingFreediverId()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			//fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter freediver_id missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionMissingFreediverId()---------");
		
	}
	
	@Test
	public void addDiveSessionMissingDiveDate() {
		try {
			
			System.out.println("-----Start addDiveSessionMissingDiveDate()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", freediverId);
			//fParam_prov.add("dive_date", ds1_date);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter dive_date missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionMissingDiveDate()---------");
		
	}
	
	@Test
	public void addDiveSessionWrongToken() {
		try {
			
			System.out.println("-----Start addDiveSessionWrongToken()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", "wrong");
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(jsonobj_prov.getInt("errorCode")==-7);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionWrongToken()---------");
		
	}
	
	@Test
	public void addDiveSessionWrongPlatformId() {
		try {
			
			System.out.println("-----Start addDiveSessionWrongPlatformId()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", "666");
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.get("ErrorMessage").equals("Platform with code:8 is not managed!"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-9);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionWrongPlatformId()---------");
		
	}
	
	@Test
	public void addDiveSessionWrongDate() {
		try {
			
			System.out.println("-----Start addDiveSessionWrongDate()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", "wrong-date");
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.get("ErrorMessage").equals("Platform with code:8 is not managed!"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionWrongDate()---------");
		
	}

	@Test
	public void addDiveSessionMissingDeepUnit() {
		try {
			
			System.out.println("-----Start addDiveSessionDeepUnitMissing()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			//fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter deep_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionDeepUnitMissing()---------");
		
	}

	@Test
	public void addDiveSessionWrongWeightUnit() {
		try {
			
			System.out.println("-----Start addDiveSessionWrongWeightUnit()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(10));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter weight_unit wrong value"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionMissingWeightUnit()---------");
		
	}

	@Test
	public void addDiveSessionMissingTempUnit() {
		try {
			
			System.out.println("-----Start addDiveSessionMissingTempUnit()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    //fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter temp_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionMissingTempUnit()---------");
		
	}

	@Test
	public void addDiveSessionMissingWeightUnit() {
		try {
			
			System.out.println("-----Start addDiveSessionMissingWeightUnit()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    //fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter weight_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionMissingWeightUnit()---------");
		
	}

	@Test
	public void addDiveSessionWrongTempUnit() {
		try {
			
			System.out.println("-----Start addDiveSessionWrongTempUnit()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(10));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter temp_unit wrong value"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionWrongTempUnit()---------");
		
	}

	@Test
	public void addDiveSessionWrongDeepUnit() {
		try {
			
			System.out.println("-----Start addDiveSessionWrongDeepUnit()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", "10");
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter  deep_unit  wrong value"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionWrongDeepUnit()---------");
		
	}

	@Test
	public void addDiveSessionWrongIntegerValue() {
		try {
			
			System.out.println("-----Start addDiveSessionWrongIntegerValue()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", "not integer");
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").contains("not integer"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionWrongIntegerValue()---------");
		
	}

	@Test
	public void addDiveSessionWrongDoubleValue() {
		try {
			
			System.out.println("-----Start addDiveSessionWrongDoubleValue()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    fParam_prov.add("deep", "NOT DOUBLE");
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").contains("not integer"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionWrongDoubleValue()---------");
		
	}

	@Test
	public void addDiveSessionNotExistFreediver() {
		try {
			
			System.out.println("-----Start addDiveSessionNotExistFreediver()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", "aglub19hcHBfazRyJwsSCUZyZWVkaXZlciIYRkFDRUJPT0stMTI1OTI3NTQ3NzU5MDcxDB");
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").contains("Cannot parse:"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-12);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionNotExistFreediver()---------");
		
	}

	@Test
	public void addDiveSessionWrongFreediverId() {
		try {
			
			System.out.println("-----Start addDiveSessionWrongFreediverId()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("freediver_id", "wrongfreediver");
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").contains("Cannot parse:"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-12);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End addDiveSessionWrongFreediverId()---------");
		
	}

	@Test
	public void updateDiveSessionMissingDeepUnit() {
		try {
			
			System.out.println("-----Start updateDiveSessionMissingDeepUnit()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_date", ds1_date);
			//fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter deep_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionMissingDeepUnit()---------");
		
	}

	@Test
	public void updateDiveSessionMissingDiveDate() {
		try {
			
			System.out.println("-----Start updateDiveSessionMissingDiveDate()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", sessionId);
			//fParam_prov.add("dive_date", ds1_date);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter dive_date missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionMissingDiveDate()---------");
		
	}

	@Test
	public void updateDiveSessionMissingExternalPlatformId() {
		try {
			
			System.out.println("-----Start updateDiveSessionMissingExternalPlatformId()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_date", ds1_date);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_platform_id missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionMissingExternalPlatformId()---------");
		
	}

	@Test
	public void updateDiveSessionMissingExternalToken() {
		try {
			
			System.out.println("-----Start updateDiveSessionMissingExternalToken()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			//fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_date", ds1_date);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_token missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionMissingExternalToken()---------");
		
	}

	@Test
	public void updateDiveSessionMissingDivesessionId() {
		try {
			
			System.out.println("-----Start updateDiveSessionMissingDivesessionId()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			//fParam_prov.add("freediver_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter divesession_id missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionMissingDivesessionId()---------");
		
	}

	@Test
	public void updateDiveSessionMissingTempUnit() {
		try {
			
			System.out.println("-----Start updateDiveSessionMissingTempUnit()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    //fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter temp_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionMissingTempUnit()---------");
		
	}

	@Test
	public void updateDiveSessionMissingWeightUnit() {
		try {
			
			System.out.println("-----Start updateDiveSessionMissingWeightUnit()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    //fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter weight_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionMissingWeightUnit()---------");
		
	}

	@Test
	public void updateDiveSessionNotExistSession() {
		try {
			
			System.out.println("-----Start UpdateDiveSessionNotExistSession()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", "aglub19hcHBfazRyJwsSCUZyZWVkaXZlciIYRkFDRUJPT0stMTI1OTI3NTQ3NzU5MDcxDB");
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").contains("Cannot parse:"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-13);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End UpdateDiveSessionNotExistSession()---------");
		
	}

	@Test
	public void updateDiveSessionWrongDate() {
		try {
			
			System.out.println("-----Start updateDiveSessionWrongDate()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_date", "wrong-date");
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.get("ErrorMessage").equals("Platform with code:8 is not managed!"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionWrongDate()---------");
		
	}

	@Test
	public void updateDiveSessionWrongDeepUnit() {
		try {
			
			System.out.println("-----Start updateDiveSessionWrongDeepUnit()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", "10");
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter  deep_unit  wrong value"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionWrongDeepUnit()---------");
		
	}

	@Test
	public void updateDiveSessionWrongDoubleValue() {
		try {
			
			System.out.println("-----Start updateDiveSessionWrongDoubleValue()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    fParam_prov.add("deep", "NOT DOUBLE");
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").contains("not integer"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionWrongDoubleValue()---------");
		
	}

	@Test
	public void updateDiveSessionWrongDivesessionId() {
		try {
			
			System.out.println("-----Start updateDiveSessionWrongDivesessionId()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", "wrongfreediver");
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").contains("Cannot parse:"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-13);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionWrongDivesessionId()---------");
		
	}

	@Test
	public void updateDiveSessionWrongIntegerValue() {
		try {
			
			System.out.println("-----Start updateDiveSessionWrongIntegerValue()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", freediverId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", "not integer");
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").contains("not integer"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionWrongIntegerValue()---------");
		
	}

	@Test
	public void updateDiveSessionWrongPlatformId() {
		try {
			
			System.out.println("-----Start updateDiveSessionWrongPlatformId()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", "666");
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_date", ds1_date);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.get("ErrorMessage").equals("Platform with code:8 is not managed!"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-9);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionWrongPlatformId()---------");
		
	}

	@Test
	public void updateDiveSessionWrongTempUnit() {
		try {
			
			System.out.println("-----Start updateDiveSessionWrongTempUnit()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
				
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(10));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter temp_unit wrong value"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionWrongTempUnit()---------");
		
	}

	@Test
	public void updateDiveSessionWrongToken() {
		try {
			
			System.out.println("-----Start updateDiveSessionWrongToken()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", "wrong");
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_date", ds1_date);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(jsonobj_prov.getInt("errorCode")==-7);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionWrongToken()---------");
		
	}

	@Test
	public void updateDiveSessionWrongWeightUnit() {
		try {
			
			System.out.println("-----Start updateDiveSessionWrongWeightUnit()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			
			
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("external_platform_id", Integer.toString(externalPlatform));
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_date", ds1_date);
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(10));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov;
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter weight_unit wrong value"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End updateDiveSessionWrongWeightUnit()---------");
		
	}
	
	@Test
	public void removeDiveSession() {
		try {
			
			System.out.println("-----Start removeDiveSession()---------");		
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
	
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			
		
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.get("message").equals("Dive session removed"));
			assertTrue(jsonobj_prov.get("result").equals("OK"));
			
			assertTrue(providerResponse.getStatus().getCode()==Status.SUCCESS_OK.getCode());
			
	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End RemoveDiveSession()---------");
	}	

	@Test
	public void removeDiveSessionMissinngExternalPlatform() {
		try {
			
			System.out.println("-----Start removeDiveSession()---------");		
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
	
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			fParam_prov = new Form();
			//fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			
		
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_platform_id missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End RemoveDiveSession()---------");
	}	

	@Test
	public void removeDiveSessionMissinngExternalToken() {
		try {
			
			System.out.println("-----Start removeDiveSession()---------");		
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
	
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			//fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			
		
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_token missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End removeDiveSessionMissinngExternalToken()---------");
	}	

	@Test
	public void removeDiveSessionMissingDiveSessionId() {
		try {
			
			System.out.println("-----Start removeDiveSessionMissingDiveSessionId()---------");		
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
	
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			//fParam_prov.add("divesession_id", sessionId);
			
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			
		
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter divesession_id missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End removeDiveSessionMissingDiveSessionId()---------");
	}

	@Test
	public void removeDiveSessionWrongToken() {
		try {
			
			System.out.println("-----Start removeDiveSessionWrongToken()---------");		
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
	
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", "wrongtoken");
			fParam_prov.add("divesession_id", sessionId);
			
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			
		
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(jsonobj_prov.getInt("errorCode")==-7);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End removeDiveSessionWrongToken()---------");
	}

	@Test
	public void removeDiveSessionNotExistSession() {
		try {
			
			System.out.println("-----Start removeDiveSessionNotExistSession()---------");		
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
	
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", "aglub19hcHBfazRyJwsSCUZyZWVkaXZlciIYRkFDRUJPT0stMTI1OTI3NTQ3NzU5MDcxDB");
			
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			
		
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(jsonobj_prov.getInt("errorCode")==-13);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		System.out.println("-----End removeDiveSessionNotExistSession()---------");
	}	

}
