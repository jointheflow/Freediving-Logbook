package org.gianluca.logbook.test.rest;

import static org.junit.Assert.assertTrue;

import org.gianluca.logbook.helper.LogbookConstant;
import org.gianluca.logbook.rest.resource.ErrorResource;
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
	private String externalToken="CAAB4GhgAGN0BAHErZCZCIqE8koHihdWStlqyNxSiaIWTTkdq2vYTxUP1rQhx7MzYsKZBylloCE8PmJDKYM2fQddIadOjdiTqtlpyXmwKKKAA8BnxJp18WrClGZBkZAMmalZAddw8M2ZBrP34lyJe8xXNhX02J1YpZBA5ZAr3ec9iBZAN6agPWZAwU7BNjFjNJGSmdznzGeIqOy5uAZDZD";
	//externalId associated to "freediving logbook" user on Facebook
	@SuppressWarnings("unused")
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
	
   
    
    private int divePageSize1=1;
   	private String freediverLoginRequest="http://localhost:8888/app/freediver/login?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	private String freediverRemoveRequest="http://localhost:8888/app/freediver/remove?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	private String diveSessionAddRequest ="http://localhost:8888/app/freediver/divesession/add";
	private String diveSessionUpdateRequest ="http://localhost:8888/app/freediver/divesession/update";
	private String diveSessionRemoveRequest ="http://localhost:8888/app/freediver/divesession/remove";
	
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
	
	@Test
	public void addErrorDiveSession() {
		try {
			Thread.sleep(5000);
			System.out.println("-----Start addErrorDiveSession()---------");
			
			
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			System.out.println("-----missing parameter external_platform_id");
			
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
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_platform_id missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----end missing parameter external_platform_id");
			
			
			System.out.println("------start missing external token-----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			//fParam_prov.add("external_token", externalToken);
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
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_token missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end missing external token-----");
			
			
			
			
			
			System.out.println("------start missing freediver id-----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			//fParam_prov.add("freediver_id", freediverId);
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
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter freediver_id missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end missing free diver id-----");
			
			
			
			System.out.println("------start missing dive date-----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("freediver_id", freediverId);
			//fParam_prov.add("dive_date", ds1_date);
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
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter dive_date missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end missing dive date-----");
			
			
			System.out.println("------start missing deep_unit-----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
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
		    //fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter deep_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end missing deep_unit-----");
			
			
			
			System.out.println("------start missing weight_unit-----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
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
		    //fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter weight_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end missing weight_unit-----");
			
			
			System.out.println("------start missing temp_unit-----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
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
		    //fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter temp_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end missing temp_unit-----");
			
			
			System.out.println("------start wrong platform id-----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
			//here we code 8 as platform Id wich is not managed
			fParam_prov.add("external_platform_id","8");
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
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter temp_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==ErrorResource.PLATFORM_NOT_MANAGED_ERROR);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end wrong platform id-----");
			
			
			System.out.println("------start wrong external token -----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
			
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			//here we pass a wrong token
			fParam_prov.add("external_token", "WRONGTOKEN");
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
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter temp_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==ErrorResource.WRONG_OAUTH_TOKEN);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end wrong external token-----");
			
			System.out.println("------start wrong date format -----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
			
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("freediver_id", freediverId);
			//wrong format date
			fParam_prov.add("dive_date", "2015/01/22");
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
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").startsWith("Parameter dive_date Unparseable date:"));
			assertTrue(jsonobj_prov.getInt("errorCode")==ErrorResource.WRONG_PARAMETER_ERROR);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end wrong external token-----");
			
			
			
			
			System.out.println("------start wrong freediver id -----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
			
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			//wrong feeediver id
			fParam_prov.add("freediver_id", "unkown");
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
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").startsWith("Could not parse Reference"));
			assertTrue(jsonobj_prov.getInt("errorCode")==ErrorResource.FREEDIVER_ID_ERROR);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end wrong freediver id-----");
			
			
			System.out.println("------start wrong deep unit format -----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
			
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
		    //wrong deep unit
		    fParam_prov.add("deep_unit", Integer.toString(5));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").startsWith("Parameter deep_unit wrong value"));
			assertTrue(jsonobj_prov.getInt("errorCode")==ErrorResource.WRONG_PARAMETER_ERROR);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end wrong deep unit format-----");
			
			System.out.println("------start wrong weight unit format -----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
			
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
		    fParam_prov.add("deep_unit", Integer.toString(0));
		    //wrong weight unit
		    fParam_prov.add("weight_unit", Integer.toString(5));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").startsWith("Parameter weight_unit wrong value"));
			assertTrue(jsonobj_prov.getInt("errorCode")==ErrorResource.WRONG_PARAMETER_ERROR);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end wrong weight unit format-----");
			
			
			System.out.println("------start wrong temp unit format -----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
			
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
		    fParam_prov.add("deep_unit", Integer.toString(0));
		   fParam_prov.add("weight_unit", Integer.toString(0));
		    fParam_prov.add("temp_unit", Integer.toString(5));
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").startsWith("Parameter temp_unit wrong value"));
			assertTrue(jsonobj_prov.getInt("errorCode")==ErrorResource.WRONG_PARAMETER_ERROR);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------end wrong temp unit format-----");
			
			
			System.out.println("------start wrong temp unit value number format exception -----");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			fParam_prov = new Form();
			
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
		    fParam_prov.add("deep_unit", Integer.toString(0));
		    fParam_prov.add("weight_unit", Integer.toString(0));
		    fParam_prov.add("temp_unit", "A");
		    
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").startsWith("Parameter temp_unit For input string:"));
			assertTrue(jsonobj_prov.getInt("errorCode")==ErrorResource.WRONG_PARAMETER_ERROR);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
		
			System.out.println("------endwrong temp unit value number format exception -----");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("-----End addErrorDiveSession()---------");
		
	}
	
	
	@Test
	public void updateAndRemoveDiveSession() {
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
			
			//get divesession of session added
			String divesessionId = jsonobj_prov.getString("id");
			System.out.println("-----End addDiveSession()---------");
			Thread.sleep(5000);
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
			
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionUpdateRequest);
			//create a post entity for Representation
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", divesessionId);
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
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
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
			
			
			
			
			System.out.println("-----Start RemoveDiveSession()---------");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveSessionRemoveRequest);
			//create a post entity for Representation
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", divesessionId);
			
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			providerResponse = providerClient.handle(providerRequest);
			
		
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(jsonobj_prov.get("message").equals("Dive session removed"));
			assertTrue(jsonobj_prov.get("result").equals("OK"));
			
			assertTrue(providerResponse.getStatus().getCode()==Status.SUCCESS_OK.getCode());
			System.out.println("-----End RemoveDiveSession()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
