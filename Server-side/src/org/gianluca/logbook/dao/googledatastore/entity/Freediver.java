package org.gianluca.logbook.dao.googledatastore.entity;

import org.gianluca.logbook.helper.LogbookConstant;



public class Freediver {
		
	//Datastore ID
	private String id;
	
	//facebook freediver's identifier returned back by external platform (e.g. Facebook)
	private String externalId;
	
	//freediver's name, returned back by extrenal platform (e.g. Facebook)
	private String externalName;
	
	//freediver's email, returned back by external platform (e.g. Facebook if exists and is public)
	private String externalEmail;
	
	//identifies the external platform that manage the authetication
	private int externalPlatformId=-1;
	
	private int temperatureUnit= LogbookConstant.TEMPERATURE_CELSIUS;
	
	private int deepUnit= LogbookConstant.DEEP_METER;
	
	private int weightUnit = LogbookConstant.WEIGHT_KILOGRAM;
	
	
	public String getId() {
	
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getExternalName() {
		return externalName;
	}

	public void setExternalName(String externalName) {
		this.externalName = externalName;
	}

	public String getExternalEmail() {
		return externalEmail;
	}

	public void setExternalEmail(String externalEmail) {
		this.externalEmail = externalEmail;
	}

	public int getExternalPlatformId() {
		return externalPlatformId;
	}

	public void setExternalPlatformId(int externalPlatformId) {
		this.externalPlatformId =externalPlatformId ;
	}

	public int getTemperatureUnit() {
		return temperatureUnit;
	}

	public void setTemperatureUnit(int temperatureUnit) {
		this.temperatureUnit = temperatureUnit;
	}

	public int getDeepUnit() {
		return deepUnit;
	}

	public void setDeepUnit(int deepUnit) {
		this.deepUnit = deepUnit;
	}

	public int getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(int weightUnit) {
		this.weightUnit = weightUnit;
	}

	

	
	
}
