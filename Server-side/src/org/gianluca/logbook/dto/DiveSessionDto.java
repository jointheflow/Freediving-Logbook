package org.gianluca.logbook.dto;

import java.util.Date;

public class DiveSessionDto {
	public String id ;
	public Date diveDate;
	public String locationDesc;
	public Float locationLatitude;
	public Float locationLongitude;
	public String meteoDesc;
	public Double waterTempAsCelsius;
	public Double waterTempAsFahrehneit;
	public Double deepAsMeter;
	public Double deepAsFeet;
	public String equipment;
	public Double weightAsKilogram;
	public Double weightAsPound;
	public String note;
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
	public Double getWaterTempAsCelsius() {
		return waterTempAsCelsius;
	}
	public void setWaterTempAsCelsius(Double waterTempAsCelsius) {
		this.waterTempAsCelsius = waterTempAsCelsius;
	}
	public Double getWaterTempAsFahrehneit() {
		return waterTempAsFahrehneit;
	}
	public void setWaterTempAsFahrehneit(Double waterTempAsFahrehneit) {
		this.waterTempAsFahrehneit = waterTempAsFahrehneit;
	}
	public Double getDeepAsMeter() {
		return deepAsMeter;
	}
	public void setDeepAsMeter(Double deepAsMeter) {
		this.deepAsMeter = deepAsMeter;
	}
	public Double getDeepAsFeet() {
		return deepAsFeet;
	}
	public void setDeepAsFeet(Double deepAsFeet) {
		this.deepAsFeet = deepAsFeet;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public Double getWeightAsKilogram() {
		return weightAsKilogram;
	}
	public void setWeightAsKilogram(Double weightAsKilogram) {
		this.weightAsKilogram = weightAsKilogram;
	}
	public Double getWeightAsPound() {
		return weightAsPound;
	}
	public void setWeightAsPound(Double weightAsPound) {
		this.weightAsPound = weightAsPound;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
	
	
}
