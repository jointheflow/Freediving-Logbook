package org.gianluca.logbook.dao.googledatastore.entity;

import java.util.List;

/*Represents a list of freediver's dive session.
 * This class i necessary because it contains also a last position of the cursor
 * of the entity set returned by the query in case of pagination */
public class DiveSessionsOfFreeediver {
	private List<DiveSession> diveSessions = null;
	private String cursor = null;
	public List<DiveSession> getDiveSessions() {
		return diveSessions;
	}
	public void setDiveSessions(List<DiveSession> diveSessions) {
		this.diveSessions = diveSessions;
	}
	public String getCursor() {
		return cursor;
	}
	public void setCursor(String cursor) {
		this.cursor = cursor;
	}
	
	
	
}
