package org.gianluca.logbook.rest.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.gianluca.logbook.helper.LogbookConstant;
import org.gianluca.logbook.rest.exception.WrongParameterException;

/*Defining common methods for checking parameters in the resource call */
public interface ILogbookResource {
	
	public default void checkExternalPlatformId(String externalPlatformId) throws WrongParameterException {
		try {
			if (externalPlatformId==null) throw new WrongParameterException("Parameter external_platform_id missing");
			
			if (Integer.parseInt(externalPlatformId) < LogbookConstant.FACEBOOK_PLATFORM &&
					Integer.parseInt(externalPlatformId) > LogbookConstant.GOOGLE_PLATFORM) {
				throw new WrongParameterException("Parameter external_platform_id wrong value");
			}
		 } catch (NumberFormatException e) {
			  throw new WrongParameterException("Parameter external_platform_id wrong "+ e.getMessage());
		 }
	}
	
	
	public default void checkExternalToken(String externalToken) throws WrongParameterException {
		if (externalToken==null) throw new WrongParameterException("Parameter external_token missing");
	}
	
	public default void checkFreediverId(String freediverId) throws WrongParameterException {
		if (freediverId==null) throw new WrongParameterException("Parameter freediver_id missing");
	}
	
	public default void checkDivesessionId(String divesessionId) throws WrongParameterException {
		if (divesessionId==null) throw new WrongParameterException("Parameter divesession_id missing");
	}
	
	
	public default void checkDiveId(String diveId) throws WrongParameterException {
		if (diveId==null) throw new WrongParameterException("Parameter dive_id missing");
	}
	
	public default void checkDouble(String doubleValue, String name) throws WrongParameterException {
		try {
			if (doubleValue!=null) new Double(doubleValue);
		
		 }catch (NumberFormatException e) {
			  throw new WrongParameterException("Parameter "+ name +" "+ e.getMessage());
		 }
	}
	
	
	public default void checkDate(String aDate, String name) throws WrongParameterException {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(LogbookConstant.DATE_FORMAT);
		    formatter.parse(aDate);
			
		}catch(ParseException e) {
			throw new WrongParameterException("Parameter "+ name + " "+e.getMessage());
			
		}
		
	}
	
	public default void checkInt(String intValue, String name) throws WrongParameterException {
		try {
			if (intValue!=null) new Integer(intValue);
		
		 }catch (NumberFormatException e) {
			  throw new WrongParameterException("Parameter "+ name +" "+ e.getMessage());
		 }
	}
	
	
	public default void checkTime(int time, String name) throws WrongParameterException{
		if (time<0 || time >1440) throw new WrongParameterException("Parameter "+ name+"  "+"must be a value between 0 and 1440");
	}
	
	public default void checkDuration(int duration, String name) throws WrongParameterException{
		if (duration<0) throw new WrongParameterException("Parameter "+ name+"  "+"must be a value >=0 ");
		
	}	
}
