package org.gianluca.logbook.dto;


public class DiveDto extends LogbookDto {
	public String id ;
	public int diveTime;
	public int duration;
	public Double depthWaterTempAsCelsius;
	public Double depthWaterTempAsFahrehneit;
	public Double maxDeepAsMeter;
	public Double maxDeepAsFeet;
	public Double neutralBuoyanceAsMeter;
	public Double neutralBuoyanceAsFeet;
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
	public Double getDepthWaterTempAsCelsius() {
		return depthWaterTempAsCelsius;
	}
	public void setDepthWaterTempAsCelsius(Double depthWaterTempAsCelsius) {
		this.depthWaterTempAsCelsius = depthWaterTempAsCelsius;
	}
	public Double getDepthWaterTempAsFahrehneit() {
		return depthWaterTempAsFahrehneit;
	}
	public void setDepthWaterTempAsFahrehneit(Double depthWaterTempAsFahrehneit) {
		this.depthWaterTempAsFahrehneit = depthWaterTempAsFahrehneit;
	}
	public Double getMaxDeepAsMeter() {
		return maxDeepAsMeter;
	}
	public void setMaxDeepAsMeter(Double maxDeepAsMeter) {
		this.maxDeepAsMeter = maxDeepAsMeter;
	}
	public Double getMaxDeepAsFeet() {
		return maxDeepAsFeet;
	}
	public void setMaxDeepAsFeet(Double maxDeepAsFeet) {
		this.maxDeepAsFeet = maxDeepAsFeet;
	}
	public Double getNeutralBuoyanceAsMeter() {
		return neutralBuoyanceAsMeter;
	}
	public void setNeutralBuoyanceAsMeter(Double neutralBuoyanceAsMeter) {
		this.neutralBuoyanceAsMeter = neutralBuoyanceAsMeter;
	}
	public Double getNeutralBuoyanceAsFeet() {
		return neutralBuoyanceAsFeet;
	}
	public void setNeutralBuoyanceAsFeet(Double neutralBuoyanceAsFeet) {
		this.neutralBuoyanceAsFeet = neutralBuoyanceAsFeet;
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
