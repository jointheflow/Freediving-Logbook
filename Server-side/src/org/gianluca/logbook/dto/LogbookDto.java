package org.gianluca.logbook.dto;

public class LogbookDto {
	public final static String RESULT_OK="OK";
	
	public String externalToken;
	public int externalPlatformId;
	public String result;
	public String message;
	public int tempUnit;
	public int deepUnit;
	public int weightUnit;
	
	
	
	public int getExternalPlatformId() {
		return externalPlatformId;
	}

	public void setExternalPlatformId(int externalPlatformId) {
		this.externalPlatformId = externalPlatformId;
	}

	public int getTempUnit() {
		return tempUnit;
	}

	public void setTempUnit(int tempUnit) {
		this.tempUnit = tempUnit;
	}

	public int getDeepUnit() {
		return deepUnit;
	}

	public void setDeepUnit(int deepUnit) {
		this.deepUnit = deepUnit;
	}

	public int getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(int weightUnit) {
		this.weightUnit = weightUnit;
	}

	public String getExternalToken() {
		return externalToken;
	}

	public void setExternalToken(String externalToken) {
		this.externalToken = externalToken;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
