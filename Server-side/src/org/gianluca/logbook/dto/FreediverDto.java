package org.gianluca.logbook.dto;

import java.util.ArrayList;
import java.util.List;

public class FreediverDto /*extends LogbookDto*/ {
	
	
	public String id;
	public String externalId;
	public String externalUsername;
	//public String externalToken;
	public String status;
	
	//the cursor pointing to the first next dive session, if pagination is used
	public String diveSessionCursor;
	
	//contains the custom field defined for
	public ArrayList<String> customFieldListOfDive;
	
	public String getDiveSessionCursor() {
		return diveSessionCursor;
	}
	public void setDiveSessionCursor(String diveSessionCursor) {
		this.diveSessionCursor = diveSessionCursor;
	}
	public List<DiveSessionDto> diveSessions;
	
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
	
	
	public String getExternalUsername() {
		return externalUsername;
	}
	public void setExternalUsername(String externalUsername) {
		this.externalUsername = externalUsername;
	}
	
	public List<DiveSessionDto> getDiveSessions() {
		return diveSessions;
	}
	public void setDiveSessions(List<DiveSessionDto> diveSessions) {
		this.diveSessions = diveSessions;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<String> getCustomFieldListOfDive() {
		return customFieldListOfDive;
	}
	public void setCustomFieldListOfDive(ArrayList<String> customFieldListOfDive) {
		this.customFieldListOfDive = customFieldListOfDive;
	}
	
	
	
	

}
