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
} 

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
}


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
}

/*Getter method*/
//get depth basing depth unit defined
DiveSessionMdl.prototype.getDepth = function (unit) {
    if (unit == freedivingLogbookConstant.DEEP_METER) return this.depth + ' meters';
    if (unit == freedivingLogbookConstant.DEEP_FEET) return this.depth + ' feet';
    return 'error value';
    
}

//get depth basing weight unit defined
DiveSessionMdl.prototype.getWeight= function (unit) {
    if (unit == freedivingLogbookConstant.WEIGHT_KILOGRAM) return this.weight + ' kg';
    if (unit == freedivingLogbookConstant.WEIGHT_POUND) return this.weight + ' pounds';
    return 'error value';
    
}

//get depth basing temp unit defined
DiveSessionMdl.prototype.getTemp= function (unit) {
    if (unit == freedivingLogbookConstant.TEMPERATURE_CELSIUS) return this.temp + ' C';
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
}


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
}

/*Getter method*/
//get depth basing depth unit defined
DiveMdl.prototype.getMaxDepth = function (unit) {
    if (unit == freedivingLogbookConstant.DEEP_METER) return this.maxDepth + ' meters';
    if (unit == freedivingLogbookConstant.DEEP_FEET) return this.maxDepth + ' feet';
    return 'error value';
    
}

//get neutral buoynace depth basing depth unit defined
DiveMdl.prototype.getNeutralBuoyance = function (unit) {
    if (unit == freedivingLogbookConstant.DEEP_METER) return this.getNeutralBuoyance + ' meters';
    if (unit == freedivingLogbookConstant.DEEP_FEET) return this.getNeutralBuoyance + ' feet';
    return 'error value';
    
}

//get depth basing weight unit defined
DiveMdl.prototype.getWeight= function (unit) {
    if (unit == freedivingLogbookConstant.WEIGHT_KILOGRAM) return this.weight + ' kg';
    if (unit == freedivingLogbookConstant.WEIGHT_POUND) return this.weight + ' pounds';
    return 'error value';
}
//get the time in the format HH:mm
DiveMdl.prototype.getTimeHHMM = function () {
    if (this.diveTime != null) {
        return (Math.floor(this.diveTime / 60)) + ':' + (this.diveTime % 60);
        
    }else return '00:00';
        
}   
    
