package org.gianluca.logbook.dao.googledatastore.entity;

import org.gianluca.logbook.helper.LogbookConstant;

import com.google.appengine.api.datastore.Key;

public class Freediver {
		
	//Datastore ID
	private Key id;
	
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
	
	public Key getId() {
	
		return id;
	}

	public void setId(Key id) {
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

	

	
	
}
