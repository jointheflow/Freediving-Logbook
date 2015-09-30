package org.gianluca.logbook.rest.resource;


import java.util.logging.Logger;

import org.gianluca.logbook.dao.exception.DiveIdException;
import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.Dive;
import org.gianluca.logbook.dto.DiveDto;
import org.gianluca.logbook.dto.LogbookDto;
import org.gianluca.logbook.external.integration.ExternalUserFactory;
import org.gianluca.logbook.external.integration.PlatformNotManagedException;
import org.gianluca.logbook.rest.exception.WrongParameterException;
import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.ext.json.*;

import com.restfb.exception.FacebookOAuthException;


public class DiveUpdateResource<K> extends ServerResource implements ILogbookResource{
	private static final Logger log = Logger.getLogger(DiveUpdateResource.class.getName());
	
	
		
	/*Update a Dive of session. Check if token is valid against external platform*/
	@Post
	public Representation updateDive(Representation entity) throws ResourceException {
		
		log.info("start POST updateDive() DiveUpdateResource)");
		//create json response
				JsonRepresentation representation = null;
			    Form form = new Form(entity); 
			   
				try {
					 
			        for (Parameter parameter : form) {
			        	
			        	log.info("parameter " + parameter.getName());
			   		  	log.info("/" + parameter.getValue());
			        }	
			         
			        //check and set all parameters
					
				    String diveId = form.getFirstValue("dive_id");
				    checkMandatory(diveId, "dive_id");
				    
				    String externalToken = form.getFirstValue("external_token");
					checkMandatory(externalToken, "external_token");
					       
					String externalPlatformId = form.getFirstValue("external_platform_id");
					checkMandatory(externalPlatformId, "external_platform_id");
					checkExternalPlatformId(externalPlatformId);
										
			        String s_timeDive = form.getFirstValue("dive_time");
			        checkMandatory(s_timeDive, "dive_time");
			        checkTime(s_timeDive, "dive_time");
			        Integer diveTime = new Integer(s_timeDive);
			        
			        String s_maxDeep = form.getFirstValue("max_deep");
			        checkDouble(s_maxDeep, "max_deep");
			        Double maxDeep = new Double(s_maxDeep);
			        
			        String s_duration = form.getFirstValue("duration");
			        checkInt(s_duration, "duration");
			        checkDuration(s_duration, "duration");
			        Integer duration = new Integer(s_duration);
			        
			        String waterTemp = form.getFirstValue("depth_water_temp");
				    checkDouble(waterTemp, "depth_water_temp");
				            
			        String s_weight = form.getFirstValue("weight");
					checkDouble(s_weight, "weight");
					Double weight = new Double(s_weight);
					
					String equipment = form.getFirstValue("equipment"); 
					String note = form.getFirstValue("note");
					
			        String s_deepUnit = form.getFirstValue("deep_unit");
				    checkMandatory(s_deepUnit, "deep_unit");
				    checkDeepUnit(s_deepUnit, "deep_unit");
				    int deepUnit = Integer.parseInt(s_deepUnit);
				    
				    String s_weightUnit = form.getFirstValue("weight_unit");
				    checkMandatory(s_weightUnit,"weight_unit");
				    checkWeightUnit(s_weightUnit, "weight_unit");
				    int weightUnit = Integer.parseInt(s_weightUnit);
				    
				    String s_tempUnit = form.getFirstValue("temp_unit");
				    checkMandatory(s_tempUnit, "temp_unit");
				    checkTempUnit(s_tempUnit, "temp_unit");
				    int tempUnit = Integer.parseInt(s_tempUnit);
				    
				    String s_neutralBuoyance = form.getFirstValue("neutral_buoyance");
				    checkDouble(s_neutralBuoyance, "neutral_buoyance");
				    Double neutralBuoyance = new Double(s_neutralBuoyance);
				    
			        String diveType =form.getFirstValue("dive_type");
					
			        String s_depthWaterTemp=form.getFirstValue("depth_water_temp");
			        checkDouble(s_depthWaterTemp, "depth_water_temp");
			        Double depthWaterTemp = new Double(s_depthWaterTemp);
					
				    
				    		   
				    //check token against external platform
					ExternalUserFactory.checkExternalToken(externalToken, Integer.parseInt(externalPlatformId));
				    
				    //update dive session
				    Dive d = LogbookDAO.updateDive(diveId, diveTime, diveType, duration, equipment, maxDeep,neutralBuoyance, note, weight, depthWaterTemp, deepUnit, tempUnit, weightUnit);
				  				  
				    //create result dto
				    DiveDto dDto = new DiveDto();
					dDto.setMaxDeepAsFeet(d.getMaxDeepAsFeet());
					dDto.setMaxDeepAsMeter(d.getMaxDeepAsMeter());
					dDto.setDiveTime(d.getDiveTime());
					dDto.setEquipment(d.getEquipment());
					dDto.setExternalToken(externalToken);
					dDto.setId(d.getId());
					dDto.setNote(d.getNote().getValue());
					dDto.setDepthWaterTempAsCelsius(d.getDepthWaterTempAsCelsisus());
					dDto.setDepthWaterTempAsFahrehneit(d.getDepthWaterTempAsfarheneit());
					dDto.setWeightAsKilogram(d.getWeightAsKilogram());
					dDto.setWeightAsPound(d.getWeightAsPound());
					dDto.setDuration(d.getDuration());
					dDto.setNeutralBuoyanceAsFeet(d.getNeutralBuoyancyAsFeet());
					dDto.setNeutralBuoyanceAsMeter(d.getNeutralBuoyancyAsMeter());
					dDto.setDiveType(d.getDiveType());
					
				    //return dive dto
				    
					//Set dto status and message
					dDto.setResult(LogbookDto.RESULT_OK);
					dDto.setMessage("Dive updated");
					
					
					representation= new JsonRepresentation(dDto);
					representation.setIndenting(true);
					
					return representation;
					
				}catch (FacebookOAuthException e_oa) {
					e_oa.printStackTrace();
					setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					ErrorResource error = new ErrorResource();
					error.setErrorCode(ErrorResource.WRONG_OAUTH_TOKEN);
					error.setErrorMessage(e_oa.getMessage());
					JsonRepresentation errorRepresentation = new JsonRepresentation(error);
					return errorRepresentation;
				}catch (PlatformNotManagedException e_pnm) {
					e_pnm.printStackTrace();
					setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					ErrorResource error = new ErrorResource();
					error.setErrorCode(ErrorResource.PLATFORM_NOT_MANAGED_ERROR);
					error.setErrorMessage(e_pnm.getMessage());
					JsonRepresentation errorRepresentation = new JsonRepresentation(error);
					return errorRepresentation;
				
				}catch (NumberFormatException e_ne) {
					e_ne.printStackTrace();
					setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					ErrorResource error = new ErrorResource();
					error.setErrorCode(ErrorResource.NUMBER_FORMAT_ERROR);
					error.setErrorMessage(e_ne.getMessage());
					JsonRepresentation errorRepresentation = new JsonRepresentation(error);
					return errorRepresentation;
					
				}catch (WrongParameterException e_wp) {
					e_wp.printStackTrace();
					setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					ErrorResource error = new ErrorResource();
					error.setErrorCode(ErrorResource.WRONG_PARAMETER_ERROR);
					error.setErrorMessage(e_wp.getMessage());
					JsonRepresentation errorRepresentation = new JsonRepresentation(error);
					return errorRepresentation;	
					
				}catch(DiveIdException a_e) {
					a_e.printStackTrace();
					setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					ErrorResource error = new ErrorResource();
					error.setErrorCode(ErrorResource.FREEDIVER_ID_ERROR);
					error.setErrorMessage(a_e.getMessage());
					JsonRepresentation errorRepresentation = new JsonRepresentation(error);
					return errorRepresentation;		
					
				}catch (Exception e) {
					e.printStackTrace();
					setStatus(Status.SERVER_ERROR_INTERNAL);
					ErrorResource error = new ErrorResource();
					error.setErrorCode(ErrorResource.INTERNAL_SERVER_ERROR);
					error.setErrorMessage(e.getMessage());
					JsonRepresentation errorRepresentation = new JsonRepresentation(error);
					return errorRepresentation;
					
				}finally {
					log.info("end POST updateDive() DiveUpdateResource)");
					
				}   
				
						
			}  
			
			  
			
		}