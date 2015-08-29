package org.gianluca.logbook.rest.resource;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.representation.Representation;
import org.restlet.resource.*; 
import org.restlet.data.Status;
import org.restlet.ext.json.*;




public class FreediverLoginResource<K> extends ServerResource{
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
				//get parameter
				String p_externalId=this.getRequest().getResourceRef().getQueryAsForm().getFirstValue("external_id");
				String p_externalPlatformId= this.getRequest().getResourceRef().getQueryAsForm().getFirstValue("external_platform_id");
				String p_externalToken = this.getRequest().getResourceRef().getQueryAsForm().getFirstValue("external_token");
				
				log.info("start GET login for Freediver");
				log.info("p_externalId:"+p_externalId);
				log.info("p_externalPlatfomId:"+p_externalPlatformId);
				log.info("p_externalToken:"+p_externalToken);
				
				//check token against platform
				
				//if token ok
					//check if p_externalId exists
					//if exists return instance
					//else create new instance
					//return instance created
				//else
				
				//get customer
				/*Customer customer = RestoDAO.getCustomerByEmail(p_email, p_password);
				
				
				//create an auth Token
				AuthToken token = new AuthToken();
				//set an infinite expiration 
				token.setExpiration(UtilHelper.getExpiration30Minute());
				token.setUserEmail(customer.getEmail());
				//persist token 
				RestoDAO.addAuthToken(token);
				
				
				//create dto token object
				TokenDTO tokenDto= new TokenDTO();
				tokenDto.expiration = token.getExpiration();
				tokenDto.id = token.getTokenId().getId();
				tokenDto.email = token.getUserEmail();
				
				//get all resti of customer and put in the DTO List
				List<Resto> resti = RestoDAO.getResto(customer.getId().getId());
				List<RestoDTO> restiDto = new ArrayList<RestoDTO>();
				for (int i=0; i<resti.size(); i++) {
					RestoDTO restoDto = new RestoDTO();
					restoDto.amount = resti.get(i).getAmount();
					restoDto.expirationDate = resti.get(i).getExpirationDate();
					restoDto.id = resti.get(i).getId().getId();
					Provider provider = resti.get(i).getProvider();
					ProviderDTO providerDto = new ProviderDTO();
					providerDto.email = provider.getEmail();
					providerDto.description = provider.getName();
					providerDto.address = provider.getAddress();
					providerDto.id = provider.getId().getId();
					restoDto.provider = providerDto;
					//add resto
					restiDto.add(restoDto);					
					
				}
				
				//create customer DTO
				CustomerDTO customerDto = new CustomerDTO();
				
				customerDto.email= customer.getEmail();
				customerDto.id = customer.getId().getId();
				customerDto.resti = restiDto;
				customerDto.token = tokenDto;
					
				if (customer !=null)	{
					representation= new JsonRepresentation(customerDto);
					representation.setIndenting(true);
				}
				return representation;
				
			*/
				return null;
		/*	}catch (WrongUserOrPasswordException e) {
				/*setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				ErrorResource error = new ErrorResource();
				error.setErrorCode(ErrorResource.WRONG_USER_OR_PASSWORD);
				error.setErrorMessage(e.getMessage());
				JsonRepresentation errorRepresentation = new JsonRepresentation(error);
				return errorRepresentation;
				*/
			}	finally {
				log.info("end  GET login for Freediver");
				
			}
			
			
		}

	
}
