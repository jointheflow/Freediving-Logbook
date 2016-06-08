package org.gianluca.logbook.dto;

import java.util.HashMap;
import java.util.Map;


public class DiveInputDto extends LogbookDto {
	public String id ;
	public String diveSessionId;
	public int diveTime;
	public int duration;
	public Double depthWaterTemp;
	public Double maxDeep;
	public Double neutralBuoyance;
	public String equipment;
	public Double weight;
	public String note;
	public String diveType;
	public double waterTemp;
	public Map<String, String> customFieldList = new HashMap<String, String>();;
	
	public double getWaterTemp() {
		return waterTemp;
	}
	public void setWaterTemp(double waterTemp) {
		this.waterTemp = waterTemp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDiveSessionId() {
		return diveSessionId;
	}
	public void setDiveSessionId(String diveSessionId) {
		this.diveSessionId = diveSessionId;
	}
	
	
	public int getDiveTime() {
		return diveTime;
	}
	public void setDiveTime(int diveTime) {
		this.diveTime = diveTime;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public Double getDepthWaterTemp() {
		return depthWaterTemp;
	}
	public void setDepthWaterTemp(Double depthWaterTemp) {
		this.depthWaterTemp = depthWaterTemp;
	}
	public Double getMaxDeep() {
		return maxDeep;
	}
	public void setMaxDeep(Double maxDeep) {
		this.maxDeep = maxDeep;
	}
	public Double getNeutralBuoyance() {
		return neutralBuoyance;
	}
	public void setNeutralBuoyance(Double neutralBuoyance) {
		this.neutralBuoyance = neutralBuoyance;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDiveType() {
		return diveType;
	}
	public void setDiveType(String diveType) {
		this.diveType = diveType;
	}
	
	
	
	
}
