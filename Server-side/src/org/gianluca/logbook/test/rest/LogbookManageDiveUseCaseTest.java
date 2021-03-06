package org.gianluca.logbook.test.rest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;

public class LogbookManageDiveUseCaseTest {
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
	private String sessionId = null;
	private String diveId2=null;
	
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
	
    //dive d1 data
    private int d1_diveTime= 608;
    private int d1_duration= 120;
    private String d1_diveType ="constant";
    private double d1_neutralBuoyance = 10.0;
    private double d1_maxDeep = 20.0;
    private String d1_equipment = "mask, lanyard";
    private String d1_note = "tuffo di riscaldamento";
    private double d1_depthWaterTemp = 16.5;
    private double d1_weight = 5.5;
    
    
  //dive d2 data
    private int d2_diveTime= 608;
    private int d2_duration= 120;
    private String d2_diveType ="constant";
    private double d2_neutralBuoyance = 10.0;
    private double d2_maxDeep = 20.0;
    private String d2_equipment = "mask, lanyard";
    private String d2_note = "tuffo di riscaldamento";
    private double d2_depthWaterTemp = 16.5;
    private double d2_weight = 5.5;
    
    
    private int divePageSize1=1;
   	private String freediverLoginRequest=LogbookConstant.HOST_NAME+"/app/freediver/login?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	private String freediverRemoveRequest=LogbookConstant.HOST_NAME+"/app/freediver/remove?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	private String diveSessionAddRequest =LogbookConstant.HOST_NAME+"/app/freediver/divesession/add";
	private String diveSessionGetRequest = LogbookConstant.HOST_NAME+"/app/freediver/divesession/get";
	private String diveAddRequest =LogbookConstant.HOST_NAME+"/app/freediver/divesession/dive/add";
	private String diveUpdateRequest =LogbookConstant.HOST_NAME+"/app/freediver/divesession/dive/update";
	private String diveRemoveRequest =LogbookConstant.HOST_NAME+"/app/freediver/divesession/dive/remove";
	private String freediverUpdateSettingRequest = LogbookConstant.HOST_NAME+"/app/freediver/updatesetting";
	
	@Test
	public void addDive() {
		try {
				
			System.out.println("-----Start addDive()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			fParam_prov.add("duration", Integer.toString(d1_duration));
		    fParam_prov.add("max_deep", Double.toString(d1_maxDeep));
			fParam_prov.add("equipment", d1_equipment); 
			fParam_prov.add("dive_type", d1_diveType);
			fParam_prov.add("note", d1_note);
			fParam_prov.add("depth_water_temp", Double.toString(d1_depthWaterTemp));
		    fParam_prov.add("weight", Double.toString(d1_weight));
		    fParam_prov.add("neutral_buoyance", Double.toString(d1_neutralBuoyance));
		    
		    //add custom params admitted
		    fParam_prov.add("parametro_uno","uno");
		    fParam_prov.add("parametro_due","due");
		    //add custom params not admitted (will be ignored)
		    fParam_prov.add("parametro_tre", "tre");
		    
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			JSONObject detailobj = jsonobj_prov.getJSONObject("detail");
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(((String)jsonobj_prov.get("externalToken")).equals(externalToken));
			assertTrue(detailobj.getInt("diveTime")==new Integer(d1_diveTime));	
			assertTrue(((String)detailobj.get("diveType")).equals(d1_diveType));
			assertTrue(((Integer)detailobj.get("duration")).intValue()==d1_duration);
			assertTrue(((String)detailobj.get("note")).equals(d1_note));
			assertTrue(((String)detailobj.get("equipment")).equals(d1_equipment));
			assertTrue(detailobj.getDouble("depthWaterTempAsCelsius")== d1_depthWaterTemp);
			assertTrue(detailobj.getDouble("depthWaterTempAsFahrehneit")==61.7);
			assertTrue(detailobj.getDouble("neutralBuoyanceAsFeet")==32.808398950000004);										   
			assertTrue(detailobj.getDouble("neutralBuoyanceAsMeter")==10);
			assertTrue(detailobj.getDouble("maxDeepAsMeter")==new Double(0));
			assertTrue(detailobj.getDouble("maxDeepAsFeet")==0);
			assertTrue(detailobj.getDouble("weightAsKilogram")== d1_weight);
			assertTrue(detailobj.getDouble("weightAsPound")== 12.125300000000001);
			
			assertTrue(jsonobj_prov.get("message").equals("Dive added"));
			assertTrue(jsonobj_prov.get("result").equals("OK"));
			assertTrue(providerResponse.getStatus().getCode()==Status.SUCCESS_OK.getCode());
			System.out.println("-----End addDive()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
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
			//insert waiting 5 second for eventually consistence.
			Thread.sleep(5000);
			
		}catch (Exception e) {
			e.printStackTrace();
			//TODO manage all excpetions
			
		}finally {
			
				
			
			System.out.println("-------END tearDown()--------");
		}
	}


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
			JSONObject detailobj = jsonobj.getJSONObject("detail");
			freediverId = (String)detailobj.get("id");
			
			
			/*add some custom param for a dive to the freediver settings*/
			System.out.println("------start update setting freediver----------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, freediverUpdateSettingRequest);
			//create a post entity for Representation
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("freediver_id", freediverId);
			
			//add custom field
			fParam_prov.add("custom_field_of_dive", "parametro_uno");
			fParam_prov.add("custom_field_of_dive", "parametro_due");
						
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			
			System.out.println(jsonobj_prov.toString());
			
			
			System.out.println("Add dive session");
			providerRequest = new Request(Method.POST, diveSessionAddRequest);
			//create a post entity for Representation
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
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		   
		    System.out.println("Executing POST "+ diveSessionAddRequest);
		    Client addClient = 	new Client(Protocol.HTTP);		
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			providerResponse = addClient.handle(providerRequest);
			JSONObject jsonobj2 = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj2.toString());
			
			JSONObject detail2 = (JSONObject) jsonobj2.get("detail");
			sessionId = (String)detail2.getString("id");
			System.out.println("sessionId:"+detail2.getString("id"));
			
			System.out.println("Add dive");
			providerClient = new Client(Protocol.HTTP);
			providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
			
			
			fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time", Integer.toString(d2_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			fParam_prov.add("duration", Integer.toString(d2_duration));
		    fParam_prov.add("max_deep", Double.toString(d2_maxDeep));
			fParam_prov.add("equipment", d2_equipment); 
			fParam_prov.add("dive_type", d2_diveType);
			fParam_prov.add("note", d2_note);
			fParam_prov.add("depth_water_temp", Double.toString(d2_depthWaterTemp));
		    fParam_prov.add("weight", Double.toString(d2_weight));
		    fParam_prov.add("neutral_buoyance", Double.toString(d2_neutralBuoyance));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			providerResponse = providerClient.handle(providerRequest);
					
			jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov);
			detailobj = jsonobj_prov.getJSONObject("detail");
			diveId2 = detailobj.getString("id");
			
			
			System.out.println("Login executed by external free diver:"+ freediverId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}finally {
			System.out.println("-------END setUp()--------");
			
		}
	}


	@Test
	public void updateDive() {
		try {
				
			System.out.println("-----Start updateDive()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
			
			
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			fParam_prov.add("duration", Integer.toString(d1_duration));
		    fParam_prov.add("max_deep", Double.toString(d1_maxDeep));
			fParam_prov.add("equipment", d1_equipment); 
			fParam_prov.add("dive_type", d1_diveType);
			fParam_prov.add("note", d1_note);
			fParam_prov.add("depth_water_temp", Double.toString(d1_depthWaterTemp));
		    fParam_prov.add("weight", Double.toString(ds1_weight));
		    fParam_prov.add("neutral_buoyance", Double.toString(d1_neutralBuoyance));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			JSONObject detailobj = jsonobj_prov.getJSONObject("detail");
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(((String)jsonobj_prov.get("externalToken")).equals(externalToken));
			assertTrue(detailobj.getInt("diveTime")==new Integer(d1_diveTime));	
			assertTrue(((String)detailobj.get("diveType")).equals(d1_diveType));
			assertTrue(((Integer)detailobj.get("duration")).intValue()==d1_duration);
			assertTrue(((String)detailobj.get("note")).equals(d1_note));
			assertTrue(((String)detailobj.get("equipment")).equals(d1_equipment));
			assertTrue(detailobj.getDouble("depthWaterTempAsCelsius")==new Double(d1_depthWaterTemp));
			assertTrue(detailobj.getDouble("depthWaterTempAsFahrehneit")==61.7);
			assertTrue(((Double)detailobj.get("neutralBuoyanceAsFeet")).doubleValue()==65.61679790000001);										   
			assertTrue(((Integer)detailobj.get("neutralBuoyanceAsMeter")).doubleValue()==20.0);
			assertTrue(detailobj.getDouble("maxDeepAsMeter")==new Double(d1_maxDeep));
			assertTrue(detailobj.getDouble("maxDeepAsFeet")==65.61679790000001);
			assertTrue(detailobj.getDouble("weightAsKilogram")== ds1_weight);
			assertTrue(detailobj.getDouble("weightAsPound")== 11.023);
			
			assertTrue(jsonobj_prov.get("message").equals("Dive updated"));
			assertTrue(jsonobj_prov.get("result").equals("OK"));
			assertTrue(providerResponse.getStatus().getCode()==Status.SUCCESS_OK.getCode());
			System.out.println("-----End updateDive()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void removeDive() {
		try {
				
			System.out.println("-----Start removeDive()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveRemoveRequest);
			//create a post entity for Representation
			
			
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
						
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			
			assertTrue(jsonobj_prov.get("message").equals("Dive removed"));
			assertTrue(jsonobj_prov.get("result").equals("OK"));
			assertTrue(providerResponse.getStatus().getCode()==Status.SUCCESS_OK.getCode());
			System.out.println("-----End removeDive()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveMissingExternalPlatformId() {
		try {
				
			System.out.println("-----Start addDiveMissingExternalPlatformId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			//fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_platform_id missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveMissingExternalPlatformId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveMissingExternalToken() {
		try {
				
			System.out.println("-----Start addDiveMissingExternalToken()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			//fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_token missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveMissingExternalToken()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveMissingDiveTime() {
		try {
				
			System.out.println("-----Start addDiveMissingDiveTime()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			//fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter dive_time missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveMissingDiveTime()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveMissingDeepUnit() {
		try {
				
			System.out.println("-----Start addDiveMissingDeepUnit()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			//fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter deep_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveMissingDeepUnit()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveMissingWeightUnit() {
		try {
				
			System.out.println("-----Start addDiveMissingWeightUnit()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    //fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter weight_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveMissingWeightUnit()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveMissingTempUnit() {
		try {
				
			System.out.println("-----Start addDiveMissingTempUnit()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    //fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter temp_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveMissingTempUnit()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveWrongPlatformId() {
		try {
				
			System.out.println("-----Start addDiveWrongPlatformId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id","99");
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(jsonobj_prov.getInt("errorCode")==-9);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveWrongPlatformId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveWrongToken() {
		try {
				
			System.out.println("-----Start addDiveWrongToken()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", "WRONGTOKEN");
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("wrong token"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-7);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveWrongToken()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveWrongSessionId() {
		try {
				
			System.out.println("-----Start addDiveWrongSessionId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", "wrong");
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(jsonobj_prov.getInt("errorCode")==-12);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveWrongSessionId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveNotExistsSessionId() {
		try {
				
			System.out.println("-----Start addDiveNotExistsSessionId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", "aglub19hcHBfazRyJwsSCUZyZWVkaXZlciIYRkFDRUJPT0stMTI1OTI3NTQ3NzU5MDcxDB");
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("transaction has expired or is invalid"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-12);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveNotExistsSessionId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveWrongDiveTime() {
		try {
				
			System.out.println("-----Start addDiveWrongDiveTime()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time","9000");
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter dive_time  must be a value between 0 and 1440"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveWrongDiveTime()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveWrongIntValue() {
		try {
				
			System.out.println("-----Start addDiveWrongIntValue()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", "WRONG INTEGER");
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			fParam_prov.add("duration", Integer.toString(d1_duration));
		    fParam_prov.add("max_deep", Double.toString(d1_maxDeep));
			fParam_prov.add("equipment", d1_equipment); 
			fParam_prov.add("dive_type", d1_diveType);
			fParam_prov.add("note", d1_note);
			fParam_prov.add("depth_water_temp", Double.toString(d1_depthWaterTemp));
		    fParam_prov.add("weight", Double.toString(d1_weight));
		    fParam_prov.add("neutral_buoyance", Double.toString(d1_neutralBuoyance));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			//System.out.println(jsonobj_prov.toString());
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
						
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("wrong token"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveWrongIntValue()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveWrongDoubleValue() {
		try {
				
			System.out.println("-----Start addDiveWrongDoubleValue()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			fParam_prov.add("duration", Integer.toString(d1_duration));
		    fParam_prov.add("max_deep", "WRONG DOUBLE");
			fParam_prov.add("equipment", d1_equipment); 
			fParam_prov.add("dive_type", d1_diveType);
			fParam_prov.add("note", d1_note);
			fParam_prov.add("depth_water_temp", Double.toString(d1_depthWaterTemp));
		    fParam_prov.add("weight", Double.toString(d1_weight));
		    fParam_prov.add("neutral_buoyance", Double.toString(d1_neutralBuoyance));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
						
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("wrong token"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End addDiveWrongDoubleValue()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void addDiveMissingNote() {
		try {
				
			System.out.println("-----Start addDivemissingNote()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("divesession_id", sessionId);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			fParam_prov.add("duration", Integer.toString(d1_duration));
		    fParam_prov.add("max_deep", Double.toString(d1_maxDeep));
			fParam_prov.add("equipment", d1_equipment); 
			fParam_prov.add("dive_type", d1_diveType);
			//fParam_prov.add("note", d1_note);
			fParam_prov.add("depth_water_temp", Double.toString(d1_depthWaterTemp));
		    fParam_prov.add("weight", Double.toString(d1_weight));
		    fParam_prov.add("neutral_buoyance", Double.toString(d1_neutralBuoyance));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
						
			assertTrue(jsonobj_prov.get("message").equals("Dive added"));
			assertTrue(jsonobj_prov.get("result").equals("OK"));
			assertTrue(providerResponse.getStatus().getCode()==Status.SUCCESS_OK.getCode());
			System.out.println("-----End addDivemissingNote()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveMissingExternalPlatformId() {
		try {
				
			System.out.println("-----Start updateDiveMissingExternalPlatformId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			//fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_platform_id missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveMissingExternalPlatformId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveMissingExternalToken() {
		try {
				
			System.out.println("-----Start updateDiveMissingExternalToken()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveAddRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			//fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_token missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveMissingExternalToken()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveMissingDiveTime() {
		try {
				
			System.out.println("-----Start updateDiveMissingDiveTime()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			//fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter dive_time missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveMissingDiveTime()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveMissingDeepUnit() {
		try {
				
			System.out.println("-----Start updateDiveMissingDeepUnit()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			//fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter deep_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveMissingDeepUnit()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveMissingWeightUnit() {
		try {
				
			System.out.println("-----Start updateDiveMissingWeightUnit()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    //fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter weight_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveMissingWeightUnit()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveMissingTempUnit() {
		try {
				
			System.out.println("-----Start updateDiveMissingTempUnit()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    //fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter temp_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveMissingTempUnit()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveWrongPlatformId() {
		try {
				
			System.out.println("-----Start updateDiveWrongPlatformId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id","99");
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(jsonobj_prov.getInt("errorCode")==-9);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveWrongPlatformId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveWrongToken() {
		try {
				
			System.out.println("-----Start updateDiveWrongToken()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", "WRONGTOKEN");
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("wrong token"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-7);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveWrongToken()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveWrongDiveId() {
		try {
				
			System.out.println("-----Start updateDiveWrongSessionId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", "wrong");
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(jsonobj_prov.getInt("errorCode")==-12);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveWrongDiveId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveNotExistsDiveId() {
		try {
				
			System.out.println("-----Start updateDiveNotExistsDiveId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", "aglub19hcHBfazRyJwsSCUZyZWVkaXZlciIYRkFDRUJPT0stMTI1OTI3NTQ3NzU5MDcxDB");
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("transaction has expired or is invalid"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-12);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveNotExistsDiveId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveWrongDiveTime() {
		try {
				
			System.out.println("-----Start updateDiveWrongDiveTime()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time","9000");
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter dive_time  must be a value between 0 and 1440"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveWrongDiveTime()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveWrongIntValue() {
		try {
				
			System.out.println("-----Start updateDiveWrongIntValue()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", "WRONG INTEGER");
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			fParam_prov.add("duration", Integer.toString(d1_duration));
		    fParam_prov.add("max_deep", Double.toString(d1_maxDeep));
			fParam_prov.add("equipment", d1_equipment); 
			fParam_prov.add("dive_type", d1_diveType);
			fParam_prov.add("note", d1_note);
			fParam_prov.add("depth_water_temp", Double.toString(d1_depthWaterTemp));
		    fParam_prov.add("weight", Double.toString(d1_weight));
		    fParam_prov.add("neutral_buoyance", Double.toString(d1_neutralBuoyance));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			//System.out.println(jsonobj_prov.toString());
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
						
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("wrong token"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveWrongIntValue()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveWrongDoubleValue() {
		try {
				
			System.out.println("-----Start updateDiveWrongDoubleValue()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			fParam_prov.add("duration", Integer.toString(d1_duration));
		    fParam_prov.add("max_deep", "WRONG DOUBLE");
			fParam_prov.add("equipment", d1_equipment); 
			fParam_prov.add("dive_type", d1_diveType);
			fParam_prov.add("note", d1_note);
			fParam_prov.add("depth_water_temp", Double.toString(d1_depthWaterTemp));
		    fParam_prov.add("weight", Double.toString(d1_weight));
		    fParam_prov.add("neutral_buoyance", Double.toString(d1_neutralBuoyance));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
						
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("wrong token"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End updateDiveWrongDoubleValue()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void updateDiveMissingNote() {
		try {
				
			System.out.println("-----Start updateDiveMissingNote()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveUpdateRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    
			fParam_prov.add("duration", Integer.toString(d1_duration));
		    fParam_prov.add("max_deep", Double.toString(d1_maxDeep));
			fParam_prov.add("equipment", d1_equipment); 
			fParam_prov.add("dive_type", d1_diveType);
			//fParam_prov.add("note", d1_note);
			fParam_prov.add("depth_water_temp", Double.toString(d1_depthWaterTemp));
		    fParam_prov.add("weight", Double.toString(d1_weight));
		    fParam_prov.add("neutral_buoyance", Double.toString(d1_neutralBuoyance));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
						
			assertTrue(jsonobj_prov.get("message").equals("Dive updated"));
			assertTrue(jsonobj_prov.get("result").equals("OK"));
			assertTrue(providerResponse.getStatus().getCode()==Status.SUCCESS_OK.getCode());
			System.out.println("-----End updateDiveMissingNote()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void removeDiveMissingExternalPlatformId() {
		try {
				
			System.out.println("-----Start removeDiveMissingExternalPlatformId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveRemoveRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			//fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			fParam_prov.add("deep_unit", Integer.toString(deepUnit));
		    fParam_prov.add("weight_unit", Integer.toString(weightUnit));
		    fParam_prov.add("temp_unit", Integer.toString(tempUnit));
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_platform_id missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End removeDiveMissingExternalPlatformId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void removeDiveMissingExternalToken() {
		try {
				
			System.out.println("-----Start removeDiveMissingExternalToken()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveRemoveRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			//fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			fParam_prov.add("dive_time", Integer.toString(d1_diveTime));
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter external_token missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End removeDiveMissingExternalToken()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void removeDiveNotExistsDiveId() {
		try {
				
			System.out.println("-----Start removeDiveNotExistsDiveId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveRemoveRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", "aglub19hcHBfazRyJwsSCUZyZWVkaXZlciIYRkFDRUJPT0stMTI1OTI3NTQ3NzU5MDcxDB");
						
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("transaction has expired or is invalid"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-13);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End removeDiveNotExistsDiveId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void removeDiveWrongDiveId() {
		try {
				
			System.out.println("-----Start removeDiveWrongDiveId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveRemoveRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id",Integer.toString(LogbookConstant.FACEBOOK_PLATFORM));
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", "wrong");
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(jsonobj_prov.getInt("errorCode")==-13);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End removeDiveWrongDiveId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void removeDiveWrongPlatformId() {
		try {
				
			System.out.println("-----Start removeDiveWrongPlatformId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.POST, diveRemoveRequest);
			//create a post entity for Representation
						
			Form fParam_prov = new Form();
			fParam_prov.add("external_platform_id","99");
			fParam_prov.add("external_token", externalToken);
			fParam_prov.add("dive_id", diveId2);
			
		    			
			providerRequest.setEntity(fParam_prov.getWebRepresentation());
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(jsonobj_prov.getInt("errorCode")==-9);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			System.out.println("-----End removeDiveWrongPlatformId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void getDiveSession() {
		try {
				
			System.out.println("-----Start getDiveSession()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.GET, diveSessionGetRequest+
												"?external_platform_id="+Integer.toString(LogbookConstant.FACEBOOK_PLATFORM)+
												"&external_token="+externalToken+
												"&divesession_id="+sessionId);
			System.out.println("executing "+diveSessionGetRequest);
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			JSONObject jsondetail = jsonobj_prov.getJSONObject("detail");
			System.out.println(jsonobj_prov.toString());
			
			assertTrue(((String)jsondetail.get("id")).equals(sessionId));
			assertTrue(jsonobj_prov.get("message").equals("Dive session found"));
			assertTrue(jsonobj_prov.get("result").equals("OK"));
			
			//check if there is the dive session added during setup
			JSONArray jsonDives = jsondetail.getJSONArray("dives");
			assertTrue(jsonDives.length()==1);
			JSONObject jsonD1 = jsonDives.getJSONObject(0);
			assertTrue(jsonD1.get("id").equals(diveId2));
			assertTrue(providerResponse.getStatus().getCode()==Status.SUCCESS_OK.getCode());
			System.out.println("-----End getDiveSession()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void getDiveSessionNotExists() {
		try {
				
			System.out.println("-----Start getDiveSessionNotExists()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.GET, diveSessionGetRequest+
												"?external_platform_id="+Integer.toString(LogbookConstant.FACEBOOK_PLATFORM)+
												"&external_token="+externalToken+
												"&divesession_id="+"notexists");
			System.out.println("executing "+diveSessionGetRequest);
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter weight_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-13);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
			System.out.println("-----End getDiveSessionNotExists()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void getDiveSessionMissingExternalPlatform() {
		try {
				
			System.out.println("-----Start getDiveSessionMissingExternalPlatform()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.GET, diveSessionGetRequest+
												"?external_token="+externalToken+
												"&divesession_id="+sessionId);
			System.out.println("executing "+diveSessionGetRequest);
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter weight_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
			System.out.println("-----End getDiveSessionMissingExternalPlatform()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void getDiveSessionMissingExternalToken() {
		try {
				
			System.out.println("-----Start getDiveSessionMissingExternalToken()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.GET, diveSessionGetRequest+
												"?external_platform_id="+Integer.toString(LogbookConstant.FACEBOOK_PLATFORM)+
												//"&external_token="+externalToken+
												"&divesession_id="+sessionId);
			System.out.println("executing "+diveSessionGetRequest);
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter weight_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
			System.out.println("-----End getDiveSessionMissingExternalToken()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void getDiveSessionMissingSessionId() {
		try {
				
			System.out.println("-----Start getDiveSessionMissingSessionId()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.GET, diveSessionGetRequest+
												"?external_platform_id="+Integer.toString(LogbookConstant.FACEBOOK_PLATFORM)+
												"&external_token="+externalToken);
												//"&divesession_id="+"notexists");
			System.out.println("executing "+diveSessionGetRequest);
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			
			assertTrue(jsonobj_prov.getInt("errorCode")==-11);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
			System.out.println("-----End getDiveSessionMissingSessionId()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}


	@Test
	public void getDiveSessionWrongExternalToken() {
		try {
				
			System.out.println("-----Start getDiveSessionWrongExternalToken()---------");
			Client providerClient = new Client(Protocol.HTTP);
			Request providerRequest = new Request(Method.GET, diveSessionGetRequest+
												"?external_platform_id="+Integer.toString(LogbookConstant.FACEBOOK_PLATFORM)+
												"&external_token="+"WRONGTOKEN"+
												"&divesession_id="+sessionId);
			System.out.println("executing "+diveSessionGetRequest);
			Response providerResponse = providerClient.handle(providerRequest);
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov.toString());
			
			//assertTrue(jsonobj_prov.getString("errorMessage").equals("Parameter weight_unit missing"));
			assertTrue(jsonobj_prov.getInt("errorCode")==-7);
			
			assertTrue(providerResponse.getStatus().getCode()==Status.CLIENT_ERROR_BAD_REQUEST.getCode());
			
			
			System.out.println("-----End getDiveSessionWrongExternalToken()---------");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
			
		}
		
		
		
	}
}
