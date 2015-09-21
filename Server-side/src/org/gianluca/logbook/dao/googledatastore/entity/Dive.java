package org.gianluca.logbook.dao.googledatastore.entity;

import java.util.Date;

import com.google.appengine.api.datastore.Text;

public class Dive {
	//dive id
	private String id;
	
	//time of dive
	private Date diveTime;
	
	//dive type ex. free immersion, constant,...
	private String diveType;
	
	//equimpent (ex. lanyard, mask, fin, monofin)
	private String equipment;
	
	//feeling during the dive
	private Text note;
	
	//dive duration
	private int duration; //int seconds
	
	//neutral
	private double neutralBuoyancyAsMeter; //
	private double neutralBuoyancyAsFeet;
	
	//maximum depth
	private double maxDeepAsMeter;
	private double maxDeepAsFeet;
	
	//weight used
	private double weightAsKilogram;
	private double weightAsPound;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDiveTime() {
		return diveTime;
	}
	public void setDiveTime(Date diveTime) {
		this.diveTime = diveTime;
	}
	public String getDiveType() {
		return diveType;
	}
	public void setDiveType(String diveType) {
		this.diveType = diveType;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public Text getNote() {
		return note;
	}
	public void setNote(Text note) {
		this.note = note;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public double getNeutralBuoyancyAsMeter() {
		return neutralBuoyancyAsMeter;
	}
	public void setNeutralBuoyancyAsMeter(double neutralBuoyancyAsMeter) {
		this.neutralBuoyancyAsMeter = neutralBuoyancyAsMeter;
	}
	public double getNeutralBuoyancyAsFeet() {
		return neutralBuoyancyAsFeet;
	}
	public void setNeutralBuoyancyAsFeet(double neutralBuoyancyAsFeet) {
		this.neutralBuoyancyAsFeet = neutralBuoyancyAsFeet;
	}
	public double getMaxDeepAsMeter() {
		return maxDeepAsMeter;
	}
	public void setMaxDeepAsMeter(double maxDeepAsMeter) {
		this.maxDeepAsMeter = maxDeepAsMeter;
	}
	public double getMaxDeepAsFeet() {
		return maxDeepAsFeet;
	}
	public void setMaxDeepAsFeet(double maxDeepAsFeet) {
		this.maxDeepAsFeet = maxDeepAsFeet;
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
	
}
