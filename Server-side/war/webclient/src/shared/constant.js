var freedivingLogbookConstant = freedivingLogbookConstant || {
	applicationName: 'appnea',
	applicationClientVersion: '0.0.0 (grouperino)',
	applicationServerVersion: '0.0.0 (grouperino)',

	apiHostName: 'https://app-nea-it.appspot.com',
	apiFreediverLoginPath:'/app/freediver/login',
	apiFreediverRemovePath:'/app/freediver/remove',
	apiDiveSessionGet:'/app/freediver/divesession/get',
	apiDiveSessionUpdate:'/app/freediver/divesession/update',
	apiDiveSessionRemove:'/app/freediver/divesession/remove',
    apiDiveSessionPublish:'/app/freediver/divesession/publish',
	apiDiveSessionAdd:'/app/freediver/divesession/add',
	apiDiveAdd: '/app/freediver/divesession/dive/add',
	apiDiveUpdate: '/app/freediver/divesession/dive/update',
	apiDiveRemove: '/app/freediver/divesession/dive/remove',
	
	pNameExternalPlatform : 'external_platform_id',
	pNamePageSize :'dive_page_size',
	pNameExternalToken : 'external_token',
    pNameDiveSessionId : 'divesession_id',
    
    FB_STATUS_LOGGED : 0,
    FB_STATUS_NOT_AUTH : -1,
    FB_STATUS_UNKNOWN : -2,
    APPNEA_STATUS_LOGGED :0,
    APPNEA_STATUS_UNKNOWN : -2,
    
    facebook_app_id: '132053467142365',
    
    //plaform managed
    PLATFORM_FACEBOOK : 0,
    PLATFORM_GOOGLE : 1, 
    
    //constant for freediver unit
    TEMPERATURE_CELSIUS : 0,
	TEMPERATURE_FAHRHENEIT : 1,
	DEEP_METER : 0,
	DEEP_FEET : 1,
	WEIGHT_KILOGRAM : 0,
	WEIGHT_POUND : 1,
    
    //dive session TAB constants
    TAB_DETAIL : 0,
    TAB_DIVES : 1,
    
    //dive session icon names
    ICON_SAVE_DIVESESSION : 'done',
    ICOND_ADD_DIVE : 'add',
    
    //status of a view: NEW or UPDATE
    VIEW_NEW: 0,
    VIEW_UPDATE: 1
    
    	
};