package org.gianluca.logbook.dto;

public class FreediverInputDto extends LogbookDto {
	
	
	public String id;
	public String externalId;
	public String externalUsername;
	public int pageSize;
	public String[] customFieldListOfDive;
	
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
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String[] getCustomFieldListOfDive() {
		return customFieldListOfDive;
	}
	public void setCustomFieldListOfDive(String[] customFieldListOfDive) {
		this.customFieldListOfDive = customFieldListOfDive;
	}
	
	
	
	
	
	
	

}
