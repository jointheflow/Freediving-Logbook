package org.gianluca.logbook.helper;

public class LogbookConstant {
	/*start values for test*/
	//public final static String HOST_NAME ="http://freediving-logbook.appspot.com";
	public final static String HOST_NAME ="http://localhost:8888";
	//get new toke from FB https://developers.facebook.com/tools/explorer
	public final static String FACEBOOK_ACCESSS_TOKEN="CAAB4GhgAGN0BANxT6LG1t7W1iT7C5p6dmmn9JKMUafJJq7nndfk3HMuYs2SzZBshzUmmBMisoG1i3qraaQWxfGBaZBBn9rdhDIa2xprFksWRkxYTzqmxmQ6LvJkSoFrpmlVhLFyPeZCcffw8ElZBQXFiY9PlODxGWBbkSQOEiiLXx9qjFU6SqwYYjbSKBoZCgrHkSq0ZAs3gZDZD";
	/*end values for test*/
	
	public static final String SW_VERSION="1.0.0 (grouperino)";
	
	public static final int FACEBOOK_PLATFORM=0;
	public static final int GOOGLE_PLATFORM=1;
		
	//freediver has been just created! 
	public static final String FREEDIVER_STATUS_NEW="NEW";
	//freediver already exists!
	public static final String FREEDIVER_STATUS_OLD="OLD";
	
	//Facebook secret app identifier
	public static final String FACEBOOK_SECRET_APP="9112e450484d1f40ab554bec6ff96c30";
	
	//Settings constants
	public static final int TEMPERATURE_CELSIUS=0;
	public static final int TEMPERATURE_FAHRHENEIT=1;
	
	public static final int DEEP_METER=0;
	public static final int DEEP_FEET=1;
	
	public static final int WEIGHT_KILOGRAM=0;
	public static final int WEIGHT_POUND=1;
	
	public static final double METER_AS_FEET= 3.280839895;
	public static final double CELSIUS_AS_FAREHN_TIME= 1.8;
	public static final double CELSIUS_AS_FAREHN_ADD= 32;
	public static final double KILOGRAM_AS_POUND = 2.2046;
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	
	
	
}
