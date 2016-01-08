var EventTypeEnum = {
	none : -1,
    leaderBoard : 0,
    mission : 1,
    quest : 2
};

var EventStateEnum = {
    inactive : "inactive",
    active : "active",
    closed : "closed",
    processing : "processing",
    finished: "finished"
};

var LeaderRuleEnum = {
	none: -1,
    incremental : 0,
    spot : 1
};

//Event Structure
var Event = function() {
    this.id = -1;
    this.startTime = -8.64e15;
    this.endTime = 8.64e15;
    this.authorized = false;
    this.eventId = -1;
    this.state = EventStateEnum.inactive;
    this.score = 0;
    this.type = EventTypeEnum.none;
    this.metadata = new EventMetadata(); 
    this.visualData = new EventVisualData();
};

Event.prototype.buildFromJSON = function(json) {
    if (json.id) {
    	this.id = json.id;
    }
    if (json.startTime) {
        this.startTime = +json.startTime;
    }
    if (json.endTime) {
        this.endTime = +json.endTime;
    }
    if (json.authorized) {
        this.authorized = json.authorized;
    }
    if (json.eventId) {
        this.eventId = json.eventId;
    }
    if (json.eventId) {
        this.eventId = json.eventId;
    }
    if (json.state) {
        this.state = json.state;
    }
   	if (json.score) {
        this.score = json.score;
    }
    if (json.type) {
        this.type = json.type;
    }
   	if (json.metadata) {
        this.metadata.buildFromJSON(json.metadata);
    }
};

Event.prototype.Active = function() {
    if( this.state != EventStateEnum.active ) {
    	return false;
    }
    if( TimeIsInPast(this.startTime) && TimeIsInFuture(this.endTime) ) {
    	return true;
    }
    return false;   	
};

Event.prototype.CommingSoon = function() {
	if( TimeIsInFuture(this.startTime) ) {
		return true;
	}
	return false;
}

Event.prototype.Ended = function() {
	if( this.type == EventTypeEnum.mission && TimeIsInPast(this.endTime) && this.score < 1 ) {
		return true;
	}
	if( this.type == EventTypeEnum.leaderBoard && TimeIsInPast(this.endTime) ) {
		return true;
	}
	return false;
}

Event.prototype.Completed = function() {
	if( TimeIsInPast(this.endTime) ) {
		return true;
	}
	if( this.type == EventTypeEnum.mission && this.score >= 1 ) {
		return true;
	}
	return false;
}

//Event Metadata Structure
var EventMetadata = function() {
    this.name = null;
    this.imageUrl = null;
    this.gameData = null;
};

EventMetadata.prototype.buildFromJSON = function(json) {
    if (json.name) {
    	this.name = json.name;
    }
    if (json.imageUrl) {
        this.imageUrl = json.imageUrl;
    }
    if (json.gameData) {
        this.gameData = +json.gameData;
    }
};

//Event Visual Data Structure
var EventVisualData = function() {
    this.lastScore = -1;
};

//LeaderBoard Structure
var LeaderBoard = function() {
    this.id = -1;
    this.progress = -1;
    this.currentUserId = null;
    this.leaderList = null;
	this.rule = new LeaderBoardRule();
	this.metadata = new LeaderBoardMetadata();
    this.currentUserRank = null;
    this.visualData = new LeaderBoardVisualData();
};

LeaderBoard.prototype.buildFromJSON = function(json) {
    if (json.id) {
    	this.id = json.id;
    }
    if (json.progress) {
    	this.progress = json.progress;
    }
    if (json.currentUserId) {
    	this.currentUserId = json.currentUserId;
    }
    if (json.rule) {	
    	this.rule.buildFromJSON( json.rule );
    }
    if (json.metadata) {
    	this.metadata.buildFromJSON( json.metadata );
    }
};

//LeaderBoardMetadata Structure
var LeaderBoardMetadata = function() {
    this.name = null;
    this.gameData = null;
    this.virtualGoodList = null;
    this.participationVirtualGood = null;
};

LeaderBoardMetadata.prototype.buildFromJSON = function(json) {
    if (json.name) {
    	this.name = json.name;
    }
    if (json.gameData) {
    	this.gameData = json.gameData;
    }
    if (json.virtualGoodList) {
    	this.virtualGoodList = json.virtualGoodList;
    }
    if (json.participationVirtualGood) {
    	this.participationVirtualGood = json.participationVirtualGood;
    }
};

//LeaderBoardRule Structure
var LeaderBoardRule = function() {
    this.id = -1;
    this.variable = null;
    this.kind = LeaderRuleEnum.none;
    this.score = null;
};

LeaderBoardRule.prototype.buildFromJSON = function(json) {
    if (json.id) {
    	this.id = json.id;
    }
    if (json.variable) {
    	this.variable = json.variable;
    }
    if (json.kind) {
    	this.kind = json.kind;
    }
    if (json.score) {
    	this.score = json.score;
    }
};

//LeaderBoardVisualData Structure
var LeaderBoardVisualData = function() {
    this.lastRank = -1;
    this.lastScore = -1;
    this.showCompleted = false;
};

//Mission Structure
var Mission = function() {
    this.id = -1;
    this.progress = -1;
    this.rules = null;
   	this.metadata = new MissionMetadata();
   	this.visualData = new MissionVisualData();
};

Mission.prototype.buildFromJSON = function(json) {
	if (json.id) {
    	this.id = json.id;
    }
    if (json.metadata) {
    	this.metadata.buildFromJSON( json.metadata );
    }
    if (json.rules && json.rules.length > 0) {
		this.rules = [];
	    for (var i=0; i<json.rules.length; i++) {
	        var missionRule = new MissionRule();
	        missionRule.buildFromJSON( json.rules[i] );
	        this.rules.push( missionRule );
	    }
	}
};

Mission.prototype.AllRulesCompleted = function() {
	if( this.rules == null ) {
		return true;
	}
	return this.RulesCompleted() == this.rules.length;
}

Mission.prototype.RulesCompleted = function() {
	if( this.rules == null ) {
		return 0;
	}
	var rulesCompleted = 0;
	for (var i=0; i<this.rules.length; i++) {
		if( this.rules[i].progress() == 1 ) {
			rulesCompleted++;
		}
	}
	return rulesCompleted;
}

Mission.prototype.Completed = function() {
	return this.progress >= 1;
}

//Mission Metadata Structure
var MissionMetadata = function() {
    this.name = null;
    this.gameData = null;
    this.virtualGood = null;
};

MissionMetadata.prototype.buildFromJSON = function(json) {
	if (json.name) {
    	this.name = json.name;
    }
    if (json.gameData) {
    	this.gameData = json.gameData;
    }
    if (json.virtualGood) {
    	this.virtualGood = json.virtualGood;
    }
};

//Mission VisualData Structure
var MissionVisualData = function() {
    this.progressValue = -1;
    this.lastShowProgress = -1;
    this.missionCompleteCount = 0;
    this.showCompleted = false;
};

MissionMetadata.prototype.buildFromJSON = function(json) {
	if (json.name) {
    	this.name = json.name;
    }
    if (json.gameData) {
    	this.gameData = json.gameData;
    }
    if (json.virtualGood) {
    	this.virtualGood = json.virtualGood;
    }
};

//Mission Rule Structure
var MissionRule = function() {
    this.id = -1;
    this.score = -1;
    this.target = null;
    this.achieved = null;
   	this.variable = null;
   	this.kind = LeaderRuleEnum.none;
   	this.metadata = new MissionRuleMetadata();
   	this.visualData = new MissionRuleVisualData();
};

MissionRule.prototype.buildFromJSON = function(json) {
	if (json.id) {
    	this.id = json.id;
    }
    if (json.score) {
    	this.score = json.score;
    }
    if (json.target) {
    	this.target = json.target;
    }
    if (json.achieved) {
    	this.achieved = json.achieved;
    }
    if (json.variable) {
    	this.variable = json.variable;
    }
    if (json.kind) {
    	this.kind = json.kind;
    }
    if (json.metadata) {
    	this.metadata.buildFromJSON( json.metadata );
    }
};

MissionRule.prototype.Progress = function() {
	var progressValue = (this.target>0)?Math.Round(this.score/this.target):0;
	if( progressValue > 1 ) {
		return 1;
	}
	return progressValue;
}

//Mission Rule Metadata Structure
var MissionRuleMetadata = function() {
    this.name = null;
    this.gameData = null;
};

MissionRuleMetadata.prototype.buildFromJSON = function(json) {
	if (json.name) {
    	this.name = json.name;
    }
    if (json.gameData) {
    	this.gameData = json.gameData;
    }
};

//Mission Rule VisualData Structure
var MissionRuleVisualData = function() {
    this.progressValue = -1;
    this.lastShowProgress = -1;
};