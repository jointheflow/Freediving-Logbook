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
function DiveSessionMdl() {
    this.id = null;
    this.diveDate = null;
    this.location = null;
    this.depthAsMeter = null;
    this.depthAsFeet = null;
    this.meteo = null;
    this.equipment = null;
    this.weightAsKilogram = null;
    this.weightAsPound = null;
    this.tempAsCelsius = null;
    this.tempAsFarheneit =null;
    this.note = null;
    //array of DiveMdl
    this.dives = [];
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