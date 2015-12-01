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

//******Dive model
function DiveMdl() {
    this.id = null;
    this.diveTime = null;
    this.equipment = null;
    this.maxDepth = null;
    this.duration = null;
    this.neutralBuoyance = null;
    this.depthTemp = null;
    this.weight = null;
}