package org.gianluca.logbook.rest.resource;


import java.util.ArrayList;
import java.util.logging.Logger;

import org.gianluca.logbook.dao.googledatastore.LogbookDAO;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSession;
import org.gianluca.logbook.dao.googledatastore.entity.DiveSessionsOfFreeediver;
import org.gianluca.logbook.dao.googledatastore.entity.Freediver;
import org.gianluca.logbook.dto.DiveSessionDto;
import org.gianluca.logbook.dto.FreediverDto;
import org.gianluca.logbook.dto.FreediverInputDto;
import org.gianluca.logbook.dto.LogbookDto;
import org.gianluca.logbook.external.integration.ExternalUser;
import org.gianluca.logbook.external.integration.ExternalUserFactory;
import org.gianluca.logbook.external.integration.PlatformNotManagedException;
import org.gianluca.logbook.helper.LogbookConstant;
import org.gianluca.logbook.rest.exception.WrongParameterException;
import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.ext.json.*;

import com.restfb.exception.FacebookOAuthException;

public class FreediverLoginResource<K>  extends ServerResource {
	private static final Logger log = Logger.getLogger(FreediverLoginResource.class.getName());
	
	
		/*Get Freediver instance basing on externalPlatformId and externalId (may be facebook, google etc.). 
		 * Check the token against external platform.
		 * If the instance does not exist
		  creates new instance* 
		 */
		@Get("json")
		public Representation login(){
			//create json response
			JsonRepresentation representation = null;
			try {
				log.info("start GET login() FreediverLoginResource");
				
				 for (Parameter parameter : this.getRequest().getResourceRef().getQueryAsForm()) {
			        	log.info("parameter " + parameter.getName());
			   		  	log.info("/" + parameter.getValue());
			        }	
			         
				
				FreediverInputDto freediverInputDto = new FreediverInputDto();
				LogbookDtoFactory.populateFreediverDtoFromGETRequest(freediverInputDto, this.getRequest().getResourceRef().getQueryAsForm(), LogbookDtoFactory.REQUEST_ADD);
					
								
				//check token against external platform
				ExternalUser extUser= ExternalUserFactory.createExternalUser(freediverInputDto.externalToken, freediverInputDto.externalPlatformId);
				
				
				//if token ok
				//creates instance
				FreediverDto fdDto = new FreediverDto();
				
				//check if p_externalId exists
				Freediver fd = LogbookDAO.getFreediverByExternalId(extUser.getId(), freediverInputDto.externalPlatformId);
				
				//if exists return instance
				if (fd!=null) {
					log.info("Freediver found!");
					fdDto.status=LogbookConstant.FREEDIVER_STATUS_OLD;
					
				}
				//else create new instance
				else {
					log.info("Freediver not found!");
					fd = LogbookDAO.addFreediver(extUser.getId(), extUser.getName(), "null", freediverInputDto.externalPlatformId);
					fdDto.status=LogbookConstant.FREEDIVER_STATUS_NEW;
				}
				
				fdDto.externalId= fd.getExternalId();
				fdDto.externalPlatformId = fd.getExternalPlatformId();
				fdDto.externalToken = freediverInputDto.externalToken;
				fdDto.externalUsername = fd.getExternalName();
				fdDto.id = fd.getId();
				fdDto.deepUnit = fd.getDeepUnit();
				fdDto.tempUnit = fd.getTemperatureUnit();
				
				
				// find all dive session associated
				DiveSessionsOfFreeediver dsOfFree = LogbookDAO.getDiveSessionsByFreediver(fd.getId(), freediverInputDto.pageSize, null);
				//add dive session to dto
				if (dsOfFree != null) {
					fdDto.diveSessions = new ArrayList<DiveSessionDto>();
					
					for (DiveSession ds : dsOfFree.getDiveSessions()) {
						DiveSessionDto dsDto = new DiveSessionDto();
						dsDto.setDeepAsFeet(ds.getDeepAsFeet());
						dsDto.setDeepAsMeter(ds.getDeepAsMeter());
						dsDto.setDiveDate(ds.getDiveDate());
						dsDto.setEquipment(ds.getEquipment());
						dsDto.setExternalToken(freediverInputDto.externalToken);
						dsDto.setId(ds.getId());
						dsDto.setLocationDesc(ds.getLocationDesc());
						if (ds.getLocationGeoPt() != null) {
							dsDto.setLocationLatitude(ds.getLocationGeoPt().getLatitude());
							dsDto.setLocationLongitude(ds.getLocationGeoPt().getLongitude());
						}
						dsDto.setMeteoDesc(ds.getMeteoDesc());
						dsDto.setNote(ds.getNote().getValue());
						dsDto.setWaterTempAsCelsius(ds.getWaterTempAsCelsius());
						dsDto.setWaterTempAsFahrehneit(ds.getWaterTempAsFahrehneit());
						dsDto.setWeightAsKilogram(ds.getWeightAsKilogram());
						dsDto.setWeightAsPound(ds.getWeightAsPound());
					
						fdDto.diveSessions.add(dsDto);
					}
				}
				
				//Set dto status and message
				fdDto.setResult(LogbookDto.RESULT_OK);
				fdDto.setMessage("Freediver login executed");
				
				representation= new JsonRepresentation(fdDto);
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
			}catch (Exception e) {
				e.printStackTrace();
				setStatus(Status.SERVER_ERROR_INTERNAL);
				ErrorResource error = new ErrorResource();
				error.setErrorCode(ErrorResource.INTERNAL_SERVER_ERROR);
				error.setErrorMessage(e.getMessage());
				JsonRepresentation errorRepresentation = new JsonRepresentation(error);
				return errorRepresentation;
				
			}
			
			finally {
				log.info("end GET login() FreediverLoginResource");
				
			}
			
			
		}
	
}
