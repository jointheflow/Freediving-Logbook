var freedivingLogbookConstant = freedivingLogbookConstant || {
	applicationName: 'App-nea',
	applicationClientVersion: '0.0.0 (grouperino)',
	applicationServerVersion: '0.0.0 (grouperino)',

	apiHostName: 'https://app-nea-it.appspot.com/app',
	apiFreediverLoginPath:'/freediver/login',
	apiFreediverRemovePath:'/freediver/remove',
	apiDiveSessionGet:'/freediver/divesession/get',
	apiDiveSessionUpdate:'/freediver/divesession/update',
	apiDiveSessionRemove:'/freediver/divesession/remove',
	apiDiveSessionAdd:'/freediver/divesession/add',
	apiDiveAdd: '/freediver/divesession/dive/add',
	apiDiveUpdate: '/freediver/divesession/dive/update',
	apiDiveRemove: '/freediver/divesession/dive/remove',
	
	pNameExternalPlatform : 'external_platform_id',
	pNamePageSize :'dive_page_size',
	pNameExternalToken : 'external_token',
    
    FB_STATUS_LOGGED : 0,
    FB_STATUS_NOT_AUTH : -1,
    FB_STATUS_UNKNOWN : -2,
    APPNEA_STATUS_LOGGED :0,
    APPNEA_STATUS_UNKNOWN : -2
};