package org.gianluca.logbook.external.integration;

/*Represents a fb user*/
public class FacebookUser implements ExternalUser {
	
	private String id=null;
	private String name=null;
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void checkExternalToken(String externalToken) {
		// TODO Auto-generated method stub
		
	}

	
}
