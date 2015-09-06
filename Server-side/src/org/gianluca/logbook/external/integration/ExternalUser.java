package org.gianluca.logbook.external.integration;

/*Identifies a user from external platform (e.g. Facebook, Linkedin, etc)
 * Each user must return an id and a name*/
public interface ExternalUser {
	public String getId();
	public String getName();
	public abstract void checkExternalToken(String externalToken);
	
}
