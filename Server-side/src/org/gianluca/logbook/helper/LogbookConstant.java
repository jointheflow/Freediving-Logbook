package org.gianluca.logbook.helper;

public class LogbookConstant {
	/*start values for test*/
	//public final static String HOST_NAME ="http://freediving-logbook.appspot.com";
	public final static String HOST_NAME ="http://localhost:8888";
	//get new toke from FB https://developers.facebook.com/tools/explorer
	public final static String FACEBOOK_ACCESSS_TOKEN="CAAB4GhgAGN0BAJQIgwyTYDKP73L4j6VfAcjIEjGCmPFcu22nyPY77YuceILKst1tuf2MlS8xymBQmg4nNO2p6L3hz1x55rS4wfC6F1uC3zmAWTZA2z5ZCj5vZCJVZCNExM4UFy0LsPlAcGEP4YuqSSFbIqa7uKaQrENEfVqpG8LIWbzYZCgJUrOprZAAkAZBjL0o3C5vOrAgAZDZD";
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
