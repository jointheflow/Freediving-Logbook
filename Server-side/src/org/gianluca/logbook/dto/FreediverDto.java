package org.gianluca.logbook.dto;

import java.util.List;

public class FreediverDto extends LogbookDto {
	
	
	public String id;
	public String externalId;
	public String externalUsername;
	//public String externalToken;
	public String status;
	
	
	
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
	public int getDeepUnit() {
		return deepUnit;
	}
	public void setDeepUnit(int deepUnit) {
		this.deepUnit = deepUnit;
	}
	
	
	

}
