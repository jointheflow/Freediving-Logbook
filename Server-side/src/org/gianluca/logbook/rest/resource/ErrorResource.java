package org.gianluca.logbook.rest.resource;

import org.gianluca.logbook.helper.LogbookConstant;

public class ErrorResource {
	public static final int TOKEN_ERROR = -4;
	public static final int NUMBER_FORMAT_ERROR=-1;
	public static final int SERIALIZING_JSON_ERROR=-2;
	public static final int PARAMETER_UNKNOWN=-3;
	public static final int IO_ERROR = -5;
	public static final int UNIQUE_CONSTRAINT_VIOLATION = -6;
	public static final int WRONG_OAUTH_TOKEN = -7;
	public static final int MANDATORY_FIELD_MISSING = -8;
	public static final int PLATFORM_NOT_MANAGED_ERROR = -9;
	public static final int INTERNAL_SERVER_ERROR = -10;
	public static final int WRONG_PARAMETER_ERROR =-11;
	public static final int FREEDIVER_ID_ERROR=-12;
	public static final int DIVESESSION_ID_ERROR=-13;
	
	
	private int errorCode;
	private String errorMessage;
	private String serverVersion = LogbookConstant.SW_VERSION;
	
	
	public String getServerVersion() {
		return serverVersion;
	}
	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
