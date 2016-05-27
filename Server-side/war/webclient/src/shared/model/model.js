/** FreediverMdl, DiveSessionMdl, DiveMdl definition **/

//******Freediver model
function FreediverMdl() {
    this.id = null;
    this.externalId = null;
    this.externalPlatformId = null;
    this.externalToken = null;
    this.username = 'fake usename';
    this.weightUnit = null;
    this.tempUnit = null;
    this.depthUnit = null;
    //custom field name array defined by the user for a single dive
    this.customFieldListOfDive = [];
    //array of DiveSessionMdl
    this.diveSessions = [];    
    //the "current" dive session, null if is new - a dive session of divesessions[] ortherwise
    this.currentDiveSession = null;   
    
    //the active tab on dive session view. Used to manage back from dive detail to dive session
    this.diveSessionActiveTabIndex = 0;
    
    //this is the status of a view and could be UPADTE or NEW. NEW means you are inserting a new data, UPDATE yuou are reading or updating existing
    //data
    this.viewstatus = freedivingLogbookConstant.VIEW_UPDATE;
    
    
}

//finds the first occurrence of divesessionid in divesessions array.
//returns the array index if username exists, null otherwise
FreediverMdl.prototype.indexOfDiveSession = function (divesessionid) {
    for (var i = 0; i < this.diveSessions.length; i++){
        if (this.diveSessions[i].id == divesessionid){
            return i;
        }
     }
    return null;
}; 

//adds a new dive session or updates if exists
FreediverMdl.prototype.addOrUpdateDiveSession = function(aDiveSession) {
    var i;
    i = this.indexOfDiveSession (aDiveSession.id);
    if (i == null) {
        //add
        this.diveSessions.push(aDiveSession);
    } else {
    //divesession already exists, updates
       this.diveSessions[i] = aDiveSession;
        
    }
};

//remove dive session if exists
FreediverMdl.prototype.removeDiveSession = function (aDivesessionId) {
    var i;
    i = this.indexOfDiveSession (aDivesessionId);
    if (i != null) {
        //add
        this.diveSessions.splice(i,1);
    } 
};


//*******DiveSession model
function DiveSessionMdl () {
    this.id = null;
    this.diveDate = null;
    this.location = null;
    this.depth = null;
    this.meteo = null;
    this.equipment = null;
    this.weight=null;
    this.temp = null;
    this.note = null;
    //array of DiveMdl
    this.dives = [];   
    //the "current" dive, null if new, a dive of dives[] otherwise
    this.currentDive=null;
};

/*Getter method*/
//get depth basing depth unit defined
DiveSessionMdl.prototype.getDepth = function (unit) {
    if (unit == freedivingLogbookConstant.DEEP_METER) return this.depth + ' meters';
    if (unit == freedivingLogbookConstant.DEEP_FEET) return this.depth + ' feet';
    return 'error value';
    
};

//get depth basing weight unit defined
DiveSessionMdl.prototype.getWeight= function (unit) {
    if (unit == freedivingLogbookConstant.WEIGHT_KILOGRAM) return this.weight + ' kg';
    if (unit == freedivingLogbookConstant.WEIGHT_POUND) return this.weight + ' pounds';
    return 'error value';
    
};

//get depth basing temp unit defined
DiveSessionMdl.prototype.getTemp= function (unit) {
    if (unit == freedivingLogbookConstant.TEMPERATURE_CELSIUS) return this.temp + ' \u00B0 C';
    if (unit == freedivingLogbookConstant.TEMPERATURE_FAHRHENEIT) return this.temp + ' F';
    return 'error value';
    
}

//finds the first occurrence of dive in dives array. returns the array index if diveid exists, null otherwise
DiveSessionMdl.prototype.indexOfDive = function (diveid) {
    for (var i = 0; i < this.dives.length; i++){
        if (this.dives[i].id == diveid){
            return i;
        }
    }
    return null;
};

//adds a dive or updates if exists
DiveSessionMdl.prototype.addOrUpdateDive = function (aDive) {
    var i;
    i = this.indexOfDive (aDive.id);
    if (i == null) {
        //add
        this.dives.push(aDive);
    } else {
    //dive already exists, update
       this.dives[i] = aDive;
        
    }
};



//remove dive  if exists
DiveSessionMdl.prototype.removeDive = function (aDiveId) {
    var i;
    i = this.indexOfDive (aDiveId);
    if (i != null) {
        //remove
        this.dives.splice(i,1);
    } 
};

/*dive session statistics method*/
//get dive with max depth
DiveSessionMdl.prototype.getMaxDiveDepth = function(unit){
    //create array of dive depth
    var diveDepth = [];
    for (var i = 0; i < this.dives.length; i++){
        diveDepth.push(this.dives[i].maxDepth);
    }
    
    var maxDepth = diveDepth.max();
    if (maxDepth != null) {
        if (unit == freedivingLogbookConstant.DEEP_METER) return maxDepth;// + ' meters';
        if (unit == freedivingLogbookConstant.DEEP_FEET) return maxDepth;// + ' feet';
        return -1;
    
    }else
        return 0;
};

//get dive with max duration
DiveSessionMdl.prototype.getMaxDiveDuration = function () {
    //create array of dive duration
    var diveDuration = [];
    for (var i = 0; i < this.dives.length; i++){
        diveDuration.push(this.dives[i].duration);
    }
    
    var maxDuration = diveDuration.max();
    if (maxDuration != null) {
       //return maxDuration;
        return (Math.floor(maxDuration / 60)) + '\'' + (maxDuration % 60)+'\'\'';
        
    
    }else
        return '-1';

};




//get dive with min depth
DiveSessionMdl.prototype.getMinDiveDepth = function(unit){
    //create array of dive depth
    var diveDepth = [];
    for (var i = 0; i < this.dives.length; i++){
        diveDepth.push(this.dives[i].maxDepth);
    }
    
    var minDepth = diveDepth.min();
    if (minDepth != null) {
        if (unit == freedivingLogbookConstant.DEEP_METER) return minDepth + ' meters';
        if (unit == freedivingLogbookConstant.DEEP_FEET) return minDepth + ' feet';
        return 'error value';
    
    }else
        return '-';
};

//get dive with min duration
DiveSessionMdl.prototype.getMinDiveDuration = function () {
    //create array of dive duration
    var diveDuration = [];
    for (var i = 0; i < this.dives.length; i++){
        diveDuration.push(this.dives[i].duration);
    }
    
    var minDuration = diveDuration.min();
    if (minDuration != null) {
       return (Math.floor(minDuration / 60)) + '\'' + (minDuration % 60)+'\'\'';
        
    
    }else
        return '-';

};

//get average depth of all dives
DiveSessionMdl.prototype.getAvgDiveDepth = function(unit){
    //sum all dive depth
    var diveDepthSum = 0;
    for (var i = 0; i < this.dives.length; i++){
        diveDepthSum = diveDepthSum + this.dives[i].maxDepth;
    }
    
    var avgDepth = (diveDepthSum / this.dives.length);
    if (avgDepth != null) {
        if (unit == freedivingLogbookConstant.DEEP_METER) return avgDepth + ' meters';
        if (unit == freedivingLogbookConstant.DEEP_FEET) return avgDepth + ' feet';
        return 'error value';
    
    }else
        return '-';
};

//get average duration of all dives
DiveSessionMdl.prototype.getAvgDiveDuration = function () {
    //sum all dive duration
    var diveDurationSum = 0;
    for (var i = 0; i < this.dives.length; i++){
        diveDurationSum = diveDurationSum + this.dives[i].duration;
    }
    
    var avgDuration = (diveDurationSum / this.dives.length);
    if (avgDuration != null) {
       return (Math.floor(avgDuration / 60)) + '\'' + (Math.floor(avgDuration % 60))+'\'\'';
        
    
    }else
        return '-';

};


/*end statistics method*/

//******Dive model
function DiveMdl() {
    this.id = null;
    this.diveTime = null;
    this.diveType = null;
    this.equipment = null;
    this.maxDepth = null;
    this.duration = null;
    this.neutralBuoyance = null;
    this.depthTemp = null;
    this.weight = null;
    this.note = null;
};

/*Getter method*/
//get depth basing depth unit defined
DiveMdl.prototype.getMaxDepth = function (unit) {
    if (unit == freedivingLogbookConstant.DEEP_METER) return this.maxDepth + ' meters';
    if (unit == freedivingLogbookConstant.DEEP_FEET) return this.maxDepth + ' feet';
    return 'error value';
    
};

//get neutral buoynace depth basing depth unit defined
DiveMdl.prototype.getNeutralBuoyance = function (unit) {
    if (unit == freedivingLogbookConstant.DEEP_METER) return this.getNeutralBuoyance + ' meters';
    if (unit == freedivingLogbookConstant.DEEP_FEET) return this.getNeutralBuoyance + ' feet';
    return 'error value';
    
};

//get depth basing weight unit defined
DiveMdl.prototype.getWeight= function (unit) {
    if (unit == freedivingLogbookConstant.WEIGHT_KILOGRAM) return this.weight + ' kg';
    if (unit == freedivingLogbookConstant.WEIGHT_POUND) return this.weight + ' pounds';
    return 'error value';
};
//get the time in the format HH:mm
DiveMdl.prototype.getTimeHHMM = function () {
    if (this.diveTime != null) {
        var HH = String(Math.floor(this.diveTime / 60));
        var MM = String(this.diveTime % 60);
        if (HH.length<2) HH = '0'+HH;
        if (MM.length<2) MM = '0'+MM;
        return HH + ':' + MM;
        
    }else return '00:00';
        
};   

//get the duration in the format mm:ss
DiveMdl.prototype.getDuration = function () {
    if (this.duration != null) {
        return (Math.floor(this.duration / 60)) + '\'' + (this.duration % 60)+'\'\'';
        
    }else return '00\'00\'\'';
        
};   

