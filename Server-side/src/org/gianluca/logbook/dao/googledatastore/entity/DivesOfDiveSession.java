package org.gianluca.logbook.dao.googledatastore.entity;

import java.util.List;

/*Represents a list of DiveSessions's dive.
 * This class i necessary because it contains also a last position of the cursor
 * of the entity set returned by the query in case of pagination */
public class DivesOfDiveSession {
	private List<Dive> dives = null;
	//private String cursor = null;
	public List<Dive> getDives() {
		return dives;
	}
	public void setDives(List<Dive> dives) {
		this.dives = dives;
	}
	/*public String getCursor() {
		return cursor;
	}
	public void setCursor(String cursor) {
		this.cursor = cursor;
	}*/
	
	
	
	
}
