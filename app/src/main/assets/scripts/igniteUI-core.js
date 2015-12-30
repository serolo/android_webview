var eventsHashTable = new HashTable();
var leaderBoardsHashTable = new HashTable();
var missionsHashTable = new HashTable();

var leaderBoardsUIHashTable = new HashTable();
var missionsUIHashTable = new HashTable();

var InitEvents  = function( eventsJson ) {
	for( var i=0 ; eventsJson.length ; i++ ) {
		var eventData = new Event();
		eventData.buildFromJSON( eventsJson[i] );
		eventsHashTable.insert( eventData.id , eventData);
	}
}

var InitLeaderBoard  = function( leaderBoardJson ) {
	var leaderBoardData = new LeaderBoard();
	leaderBoardData.buildFromJSON( leaderBoardJson );
	leaderBoardsHashTable.insert( leaderBoardData.id , leaderBoardData );
	var leaderBoardUI = leaderBoardsUIHashTable.retrieve();
	if( leaderBoardUI == null ) {

	}
	else {

	}
}

var InitMissions  = function( missionsJson ) {
	var missionData = new Mission();
	missionData.buildFromJSON( missionsJson );
	missionsHashTable.insert( missionData.id , missionData );
}



var MissionUIController = function() {
	//Containers
	this.container = null;
	this.titleObject = null;
	this.rulesObject = null;
	this.prizeObject = null;
	this.endNoticeObject = null;
	//Text
	this.eventNameText = null;
	this.eventDescText = null;
	this.progressCountText = null;
	this.prizeNameText = null;
	this.prizeCountText = null;
	this.endNoticeText = null;
	//Images
	this.endNoticeImage = null;
	this.prizeIconImage = null;

	//Rules
	this.rulesRows = null;

	//Variable
	this.localEventData = null;
	this.localMissionData = null;
	this.progressValue = -1;
	this.localVisualData = null;
	this.init = false;
}

MissionUIController.prototype.Update = function() {
	//if( this.init && this.)
}

MissionUIController.prototype.Init = function( eventData , missionData ) {
	this.localEventData = eventData;
	this.localMissionData = missionData;
}

MissionUIController.prototype.CreateUI = function() {
	this.container = new PIXI.Graphics();
	this.container.anchor = new PIXI.Point(0.5,0.5);
	this.container.beginFill(0x9C551F, 1);
	this.container.drawRoundedRect(0, 0, 450, 595, 20);
	this.container.endFill();
	this.container.position = new PIXI.Point(-455,-300);
	
	this.titleObject = new PIXI.Graphics();
	this.titleObject.anchor = new PIXI.Point(0.5,0.5);
	this.titleObject.beginFill(0xD1B986, 1);
	this.titleObject.drawRoundedRect(0, 0, 450, 96, 20);
	this.titleObject.endFill();
	this.titleObject.position = new PIXI.Point(-15,10);
	this.container.addChild( this.titleObject );

	this.eventNameText = new PIXI.Text('Peg Leg Monday Event!',{font : 'bold 20px Arial', fill : 0x321B0C, align : 'center', wordWrap : true,wordWrapWidth : 240});
	this.eventNameText.pivot = new PIXI.Point(0.5,0.5);
	this.eventNameText.position = new PIXI.Point(150,10);
	this.titleObject.addChild( this.eventNameText );

	this.eventDescText = new PIXI.Text('Complete all three missions to win the grand prize!',{font : '16px Arial', fill : 0x321B0C, align : 'center', wordWrap : true,wordWrapWidth : 220});
	this.eventDescText.pivot = new PIXI.Point(0.5,0.5);
	this.eventDescText.position = new PIXI.Point(150,40);
	this.titleObject.addChild( this.eventDescText );

	/*
	this.rulesObject = new PIXI.Container();
	this.rulesObject.position = new PIXI.Point(0,0);
	this.rulesObject.anchor = new PIXI.Point(0.5,0.5);
	this.container.addChild( this.rulesObject );
	*/

	this.prizeObject = new PIXI.Graphics();
	this.prizeObject.anchor = new PIXI.Point(0.5,0.5);
	this.prizeObject.beginFill(0xD1B986, 1);
	this.prizeObject.drawRoundedRect(0, 0, 450, 110, 20);
	this.prizeObject.endFill();
	this.prizeObject.position = new PIXI.Point(-15,400);
	this.container.addChild( this.prizeObject );

	this.prizeCountText = new PIXI.Text('Grand Prize:\nComplete all 3 sub-missions',{font : 'bold 17px Arial', fill : 0x523722, align : 'left'});
	this.prizeCountText.pivot = new PIXI.Point(0.5,0.5);
	this.prizeCountText.position = new PIXI.Point(80,40);
	this.prizeObject.addChild( this.prizeCountText );

	this.endNoticeObject = new PIXI.Graphics();
	this.endNoticeObject.anchor = new PIXI.Point(0.5,0.5);
	this.endNoticeObject.beginFill(0x00AF9C, 1);
	this.endNoticeObject.drawRoundedRect(0, 0, 450, 70, 20);
	this.endNoticeObject.endFill();
	this.endNoticeObject.position = new PIXI.Point(-15,520);
	this.container.addChild( this.endNoticeObject );

	this.endNoticeText = new PIXI.Text('Prizes are on their way!\nTournament results are being calculated.',{font : '16px Arial', fill : 0xFFFFFF, align : 'center'});
	this.endNoticeText.pivot = new PIXI.Point(0.5,0.5);
	this.endNoticeText.position = new PIXI.Point(100,20);
	this.endNoticeObject.addChild( this.endNoticeText );

	return this.container;
}

MissionUIController.prototype.UpdateUI = function() {
}



