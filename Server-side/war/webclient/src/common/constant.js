var freedivingLogbookConstant = freedivingLogbookConstant || {
	applicationName: 'App-nea',
	applicationClientVersion: '0.0.0 (grouperino)',
	applicationServerVersion: '0.0.0 (grouperino)',

	apiHostName: 'https://app-nea-it.appspot.com',
	apiFreediverLoginPath:'/app/freediver/login',
	apiFreediverRemovePath:'/app/freediver/remove',
	apiDiveSessionGet:'/app/freediver/divesession/get',
	apiDiveSessionUpdate:'/app/freediver/divesession/update',
	apiDiveSessionRemove:'/app/freediver/divesession/remove',
	apiDiveSessionAdd:'/app/freediver/divesession/add',
	apiDiveAdd: '/app/freediver/divesession/dive/add',
	apiDiveUpdate: '/app/freediver/divesession/dive/update',
	apiDiveRemove: '/app/freediver/divesession/dive/remove',
	
	pNameExternalPlatform : 'external_platform_id',
	pNamePageSize :'dive_page_size',
	pNameExternalToken : 'external_token',
    
    FB_STATUS_LOGGED : 0,
    FB_STATUS_NOT_AUTH : -1,
    FB_STATUS_UNKNOWN : -2,
    APPNEA_STATUS_LOGGED :0,
    APPNEA_STATUS_UNKNOWN : -2,
    
    facebook_app_id: '132053467142365'
};