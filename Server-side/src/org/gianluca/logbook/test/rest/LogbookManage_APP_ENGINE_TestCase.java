package org.gianluca.logbook.test.rest;


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

public class LogbookManage_APP_ENGINE_TestCase {
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
    private int divePageSize100=100;
   	private String freediverLoginRequest=LogbookConstant.HOST_NAME+"/app/freediver/login?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	private String freediverRemoveRequest=LogbookConstant.HOST_NAME+"/app/freediver/remove?external_platform_id="+externalPlatform+"&external_token="+externalToken;
	private String diveSessionAddRequest =LogbookConstant.HOST_NAME+"/app/freediver/divesession/add";
	private String diveSessionGetRequest = LogbookConstant.HOST_NAME+"/app/freediver/divesession/get";
	private String diveAddRequest =LogbookConstant.HOST_NAME+"/app/freediver/divesession/dive/add";
	private String diveUpdateRequest =LogbookConstant.HOST_NAME+"/app/freediver/divesession/dive/update";
	private String diveRemoveRequest =LogbookConstant.HOST_NAME+"/app/freediver/divesession/dive/remove";
	
	//@Before
	/*Executing login will create a user*/
	public void setUp(){
		try {
					
			System.out.println("-------START setUp()--------");
			/*----START Login of a  freediver-----*/ 
			
			
			System.out.println("Add dive session");
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
			JSONObject jsonobj2 = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj2.toString());
			System.out.println("sessionId:"+jsonobj2.getString("id"));
			sessionId = (String)jsonobj2.getString("id");
			
			System.out.println("Add dive");
			Client providerClient = new Client(Protocol.HTTP);
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
					
			JSONObject jsonobj_prov = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
			System.out.println(jsonobj_prov);
			diveId2 = jsonobj_prov.getString("id");
			
			
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
	public void login() {
		System.out.println("Login as a freediver");
		Client loginClient = new Client(Protocol.HTTP);
		Request loginRequest = new Request(Method.GET, freediverLoginRequest+"&dive_page_size="+divePageSize100);
		System.out.println("Executing GET "+ freediverLoginRequest+"&dive_page_size="+divePageSize100);
		
		Response loginResponse = loginClient.handle(loginRequest);
		JSONObject jsonobj = null;
		JSONObject detailobj = null;
		try {
			jsonobj = new JsonRepresentation(loginResponse.getEntityAsText()).getJsonObject();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(jsonobj.toString());
		try {
			detailobj = jsonobj.getJSONObject("detail");
			freediverId = (String)detailobj.get("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void addDiveSession() throws JSONException {
		System.out.println("Login as a freediver");
		Client loginClient = new Client(Protocol.HTTP);
		Request loginRequest = new Request(Method.GET, freediverLoginRequest+"&dive_page_size="+divePageSize100);
		System.out.println("Executing GET "+ freediverLoginRequest+"&dive_page_size="+divePageSize100);
		
		Response loginResponse = loginClient.handle(loginRequest);
		JSONObject jsonobj = null;
		JSONObject detailobj = null;
		jsonobj = new JsonRepresentation(loginResponse.getEntityAsText()).getJsonObject();
		System.out.println(jsonobj.toString());
		detailobj = jsonobj.getJSONObject("detail");
		freediverId = (String)detailobj.get("id");
		
		System.out.println("Add dive session");
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
		JSONObject jsonobj2 = new JsonRepresentation(providerResponse.getEntityAsText()).getJsonObject();
		
		System.out.println(jsonobj2.toString());
		detailobj = jsonobj2.getJSONObject("detail");
		System.out.println("sessionId:"+detailobj.getString("id"));
		sessionId = (String)detailobj.getString("id");
		
		
	}
}
