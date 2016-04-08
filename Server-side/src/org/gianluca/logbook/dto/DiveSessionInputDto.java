package org.gianluca.logbook.dto;

import java.util.Date;

public class DiveSessionInputDto extends LogbookDto {
	public String id ;
	public String freediverId;
	public Date diveDate;
	public String locationDesc;
	public Float locationLatitude;
	public Float locationLongitude;
	public String meteoDesc;
	public Double waterTemp;
	public Double deep;
	public String equipment;
	public Double weight;
	public String note;
	public String userName;
	public Double maxDiveDepth;
	public String maxDiveDuration;
	
	
	public String getFreediverId() {
		return freediverId;
	}
	public void setFreediverId(String freediverId) {
		this.freediverId = freediverId;
	}
	public Double getWaterTemp() {
		return waterTemp;
	}
	public void setWaterTemp(Double waterTemp) {
		this.waterTemp = waterTemp;
	}
	public Double getDeep() {
		return deep;
	}
	public void setDeep(Double deep) {
		this.deep = deep;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDiveDate() {
		return diveDate;
	}
	public void setDiveDate(Date diveDate) {
		this.diveDate = diveDate;
	}
	public String getLocationDesc() {
		return locationDesc;
	}
	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}
	public Float getLocationLatitude() {
		return locationLatitude;
	}
	public void setLocationLatitude(Float locationLatitude) {
		this.locationLatitude = locationLatitude;
	}
	public Float getLocationLongitude() {
		return locationLongitude;
	}
	public void setLocationLongitude(Float locationLongitude) {
		this.locationLongitude = locationLongitude;
	}
	public String getMeteoDesc() {
		return meteoDesc;
	}
	public void setMeteoDesc(String meteoDesc) {
		this.meteoDesc = meteoDesc;
	}

	
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Double getMaxDiveDepth() {
		return maxDiveDepth;
	}
	public void setMaxDiveDepth(Double maxDiveDepth) {
		this.maxDiveDepth = maxDiveDepth;
	}
	public String getMaxDiveDuration() {
		return maxDiveDuration;
	}
	public void setMaxDiveDuration(String maxDiveDuration) {
		this.maxDiveDuration = maxDiveDuration;
	}
	
	
	
	
}
