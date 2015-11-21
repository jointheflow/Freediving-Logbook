//Freediver model
function FreediverMdl() {
    this.id = null;
    this.externalId = null;
    this.externalPlatformId = null;
    this.externalToken = null;
    this.username = 'fake usename';
    this.wheightUnit = null;
    this.tempUnit = null;
    this.depthUnit = null;
    //array of DiveSessionMdl
    this.diveSessions = [];
}

//DiveSession model
function DiveSessionMdl() {
    this.id = null;
    this.diveDate = null;
    this.location = null;
    this.depth = null;
    this.meteo = null;
    this.equipment = null;
    this.weight = null;
    this.note = null;
    //array of DiveMdl
    this.dives = [];
}


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