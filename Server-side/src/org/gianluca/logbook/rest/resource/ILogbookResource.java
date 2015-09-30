package org.gianluca.logbook.rest.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.gianluca.logbook.helper.LogbookConstant;
import org.gianluca.logbook.rest.exception.WrongParameterException;

/*Defining common methods for checking parameters in the resource call */
public interface ILogbookResource {
	
	public default void checkMandatory(String value, String name) throws WrongParameterException {
		if (value==null) throw new WrongParameterException("Parameter"+name+" missing");
	}
	
	public default void checkExternalPlatformId(String externalPlatformId) throws WrongParameterException {
		try {
				
			if (Integer.parseInt(externalPlatformId) < LogbookConstant.FACEBOOK_PLATFORM &&
					Integer.parseInt(externalPlatformId) > LogbookConstant.GOOGLE_PLATFORM) {
				throw new WrongParameterException("Parameter external_platform_id wrong value");
			}
		 } catch (NumberFormatException e) {
			  throw new WrongParameterException("Parameter external_platform_id wrong "+ e.getMessage());
		 }
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
	
	
	public default void checkTime(String time, String name) throws WrongParameterException{
		checkInt(time, name);
		if (Integer.parseInt(time)<0 || Integer.parseInt(time) >1440) throw new WrongParameterException("Parameter "+ name+"  "+"must be a value between 0 and 1440");
		
	}
	
	public default void checkDuration(String duration, String name) throws WrongParameterException{
		if (duration != null)
			if (Integer.parseInt(duration)<0) throw new WrongParameterException("Parameter "+ name+"  "+"must be a value >=0 ");
		
	}
	
	public default void checkDeepUnit(String deepUnit, String name) throws WrongParameterException {
		checkInt(deepUnit,name);
		if ((new Integer(deepUnit) < LogbookConstant.DEEP_METER) || (new Integer (deepUnit) > LogbookConstant.DEEP_FEET)) throw new WrongParameterException("Parameter  "+ name+"  wrong value");
	}
	
	public default void checkWeightUnit(String weightUnit, String name) throws WrongParameterException {
		checkInt(weightUnit, name);
		if ((new Integer(weightUnit) < LogbookConstant.WEIGHT_KILOGRAM) || (new Integer (weightUnit) > LogbookConstant.WEIGHT_POUND)) throw new WrongParameterException("Parameter "+ name+" wrong value");
		
	}
	
	public default void checkTempUnit(String tempUnit, String name) throws WrongParameterException {
		checkInt(tempUnit, name);
		if ((new Integer(tempUnit) < LogbookConstant.TEMPERATURE_CELSIUS) || (new Integer (tempUnit) > LogbookConstant.TEMPERATURE_FAHRHENEIT)) throw new WrongParameterException("Parameter "+name+" wrong value");
		
	}
	
}
