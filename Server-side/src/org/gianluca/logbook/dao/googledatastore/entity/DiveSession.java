package org.gianluca.logbook.dao.googledatastore.entity;

import java.util.Date;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Text;

/*Represents a freediving session*/
public class DiveSession {
	//datastore id
	private String id ;
	private Date diveDate;
	private String locationDesc;
	private GeoPt locationGeoPt;
	private String meteoDesc;
	private double waterTempAsCelsius;
	private double waterTempAsFahrehneit;
	private double deepAsMeter;
	private double deepAsFeet;
	private String equipment;
	private double weightAsKilogram;
	private double weightAsPound;
	private Text note;
	private DivesOfDiveSession dives;
	//set if dive session has been published to facebook and is visible to everyone!!
	//TODO: change default to false
	private boolean published = true;
	
	public DivesOfDiveSession getDives() {
		return dives;
	}
	public void setDives(DivesOfDiveSession dives) {
		this.dives = dives;
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
	public void setDiveDate(Date date) {
		this.diveDate = date;
	}
	public String getLocationDesc() {
		return locationDesc;
	}
	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}
	public GeoPt getLocationGeoPt() {
		return locationGeoPt;
	}
	public void setLocationGeoPt(GeoPt locationGeoPt) {
		this.locationGeoPt = locationGeoPt;
	}
	public String getMeteoDesc() {
		return meteoDesc;
	}
	public void setMeteoDesc(String meteoDesc) {
		this.meteoDesc = meteoDesc;
	}
	public double getWaterTempAsCelsius() {
		return waterTempAsCelsius;
	}
	public void setWaterTempAsCelsius(double waterTempAsCelsius) {
		this.waterTempAsCelsius = waterTempAsCelsius;
	}
	public double getWaterTempAsFahrehneit() {
		return waterTempAsFahrehneit;
	}
	public void setWaterTempAsFahrehneit(double waterTempAsFahrehneit) {
		this.waterTempAsFahrehneit = waterTempAsFahrehneit;
	}
	public double getDeepAsMeter() {
		return deepAsMeter;
	}
	public void setDeepAsMeter(double deepAsMeter) {
		this.deepAsMeter = deepAsMeter;
	}
	public double getDeepAsFeet() {
		return deepAsFeet;
	}
	public void setDeepAsFeet(double deepAsFeet) {
		this.deepAsFeet = deepAsFeet;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public double getWeightAsKilogram() {
		return weightAsKilogram;
	}
	public void setWeightAsKilogram(double weightAsKilogram) {
		this.weightAsKilogram = weightAsKilogram;
	}
	public double getWeightAsPound() {
		return weightAsPound;
	}
	public void setWeightAsPound(double weightAsPound) {
		this.weightAsPound = weightAsPound;
	}
	public Text getNote() {
		return note;
	}
	public void setNote(Text note) {
		this.note = note;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	
	
	
	
	
}
