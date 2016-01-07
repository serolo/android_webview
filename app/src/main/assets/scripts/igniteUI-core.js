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

var TabType = {
	none: 0,
	leaderBoard: 1,
	mission: 2
}

var TabState = {
	open : 0,
    close : 1
};

var TabPosition = {
	none: -1,
	one : 0,
	two : 1,
	three : 2,
	four : 3
};

//============================== TAB UI CONTROLLER ==============================

var TabUIController = function( index , tabPosition, type , openCloseCallback ) {
	this.index = index;
	this.type = type;
	this.openCloseCallback = openCloseCallback;
	this.tabPosition = tabPosition;
	this.container = null;
	this.currentState = TabState.close;
	this.enable = false;
	this.localEventData = null;
}

TabUIController.prototype.Update = function() {
	//if( this.init && this.)
}

TabUIController.prototype.Init = function( eventData ) {
	this.localEventData = eventData;
}

TabUIController.prototype.CreateUI = function() {
	this.container = new PIXI.Container();
	this.container.position = new PIXI.Point(0,0);
	this.container.anchor = new PIXI.Point(0.5,0.5);

	var background = new PIXI.Graphics();
	background.beginFill(0x462E27, 1);
	if( this.type == TabType.mission ) {
		background.drawRoundedRect(-468, -300, 468, 600, 20);
	}
	else {
		background.drawRoundedRect(0, -300, 468, 600, 20);	
	}
	//background.drawRoundedRect(0, 0, 468, 600, 20);	
	background.endFill();
	this.container.addChild( background );

	var TabContainer = new PIXI.Container();
	if( this.type == TabType.mission ) {
		TabContainer.anchor = new PIXI.Point(1,0.5);
	}
	else {
		TabContainer.anchor = new PIXI.Point(0,0.5);
	}
	var position = new PIXI.Point(0,0);
	switch( this.tabPosition ) {
		case TabPosition.one:
			position.y = -210;
			break;
		case TabPosition.two:
			position.y = -80;
			break;
		case TabPosition.three:
			position.y = 50;
			break;
		case TabPosition.four:
			position.y = 180;
			break;
	}
	if( this.type == TabType.mission ) {
		position.x = 40;
	}
	else {
		position.x = -40;
	}
	TabContainer.position = position;
	TabContainer.interactive = true;
	TabContainer.on('mousedown', this.OpenCloseContainer.bind(this) );
	TabContainer.on('touchstart', this.OpenCloseContainer.bind(this) );
	this.container.addChild(TabContainer);

	var TabImage = new PIXI.Graphics();
	TabImage.position = new PIXI.Point(-60,-40);
	TabImage.anchor = new PIXI.Point(0.5,0.5);
	TabImage.beginFill(0x9C551F, 1);
	TabImage.drawRoundedRect(0, 0, 120, 90, 20);
	TabImage.endFill();
	TabContainer.addChild(TabImage);

	var timer = new PIXI.Graphics();
	timer.anchor = new PIXI.Point(0.5,0.5);
	timer.position = new PIXI.Point(0,89);
	timer.beginFill(0x462E27, 1);
	if( this.type == TabType.mission ) {
		timer.drawRoundedRect(4, 4, 130, 30, 10);
	}
	else {
		timer.drawRoundedRect(-4, 4, 130, 30, 10);	
	}
	timer.endFill();
	timer.beginFill(0x9C551F, 1);
	timer.drawRoundedRect(0, 0, 130, 30, 10);
	timer.endFill();
	TabImage.addChild(timer);

	var backgroundTimer = new PIXI.Graphics();
	backgroundTimer.anchor = new PIXI.Point(0.5,0.5);
	if( this.type == TabType.mission ) {
		backgroundTimer.position = new PIXI.Point(7,3);
	}
	else {
		backgroundTimer.position = new PIXI.Point(4,3);	
	}
	backgroundTimer.beginFill(0xD1B986, 1);
	backgroundTimer.drawRoundedRect(0, 0, 120, 24, 10);
	backgroundTimer.endFill();
	timer.addChild(backgroundTimer);

	var text = new PIXI.Text('01m 02s',{font : 'bold 18px Arial', fill : 0x523722, align : 'center'});
	text.pivot = new PIXI.Point(0.5,0.5);
	text.position = new PIXI.Point(25,3);
	backgroundTimer.addChild( text );

	if( this.type == TabType.mission ) {
		var controller = new MissionUIController();
		this.container.addChild( controller.CreateUI() );
	}
	else {
		var controller = new LeaderBoardUIController();
		this.container.addChild( controller.CreateUI() );	
	}
	
	return this.container;
}

TabUIController.prototype.OpenCloseContainer = function (eventData) {
	if( this.currentState == TabState.open ) {
		this.container.position.set(0,0);
	}
	else {
		if( this.type == TabType.mission ) {
			this.container.position.set(400,0);
		}
		else {
			this.container.position.set(-400,0);	
		}
	}
	if( this.openCloseCallback != null ) {
		this.openCloseCallback(this.index,this.currentState);
	}
	this.currentState = (this.currentState == TabState.open)?TabState.close:TabState.open;
}

//============================== MISSION UI CONTROLLER ==============================

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

//============================== LEADERBOARD UI CONTROLLER ==============================

var LeaderBoardUIController = function() {
	//Containers
	this.container = null;
	this.titleObject = null;
	this.leadersObject = null;
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
	this.localLeaderBoardData = null;
	this.localVisualData = null;
	this.init = false;
}

LeaderBoardUIController.prototype.Update = function() {
	//if( this.init && this.)
}

LeaderBoardUIController.prototype.Init = function( eventData , leaderBoardData ) {
	this.localEventData = eventData;
	this.localLeaderBoardData = leaderBoardData;
	this.init = true;
}

LeaderBoardUIController.prototype.CreateUI = function() {
	this.container = new PIXI.Graphics();
	this.container.anchor = new PIXI.Point(0.5,0.5);
	this.container.beginFill(0x9C551F, 1);
	this.container.drawRoundedRect(0, 0, 450, 595, 20);
	this.container.endFill();
	this.container.position = new PIXI.Point(5,-300);
		
	this.titleObject = new PIXI.Graphics();
	this.titleObject.anchor = new PIXI.Point(0.5,0.5);
	this.titleObject.beginFill(0xD1B986, 1);
	this.titleObject.drawRoundedRect(0, 0, 450, 96, 20);
	this.titleObject.endFill();
	this.titleObject.position = new PIXI.Point(15,10);
	this.container.addChild( this.titleObject );

	this.eventNameText = new PIXI.Text('Test LeaderBoard',{font : 'bold 20px Arial', fill : 0x321B0C, align : 'center', wordWrap : true,wordWrapWidth : 300});
	this.eventNameText.pivot = new PIXI.Point(0.5,0.5);
	this.eventNameText.position = new PIXI.Point(100,10);
	this.titleObject.addChild( this.eventNameText );

	this.eventDescText = new PIXI.Text('Collect the most treasure to win!',{font : '16px Arial', fill : 0x321B0C, align : 'center', wordWrap : true,wordWrapWidth : 300});
	this.eventDescText.pivot = new PIXI.Point(0.5,0.5);
	this.eventDescText.position = new PIXI.Point(100,40);
	this.titleObject.addChild( this.eventDescText );

	/*
	this.rulesObject = new PIXI.Container();
	this.rulesObject.position = new PIXI.Point(0,0);
	this.rulesObject.anchor = new PIXI.Point(0.5,0.5);
	this.container.addChild( this.rulesObject );
	*/

	/*
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
	*/

	this.endNoticeObject = new PIXI.Graphics();
	this.endNoticeObject.anchor = new PIXI.Point(0.5,0.5);
	this.endNoticeObject.beginFill(0x00AF9C, 1);
	this.endNoticeObject.drawRoundedRect(0, 0, 450, 70, 20);
	this.endNoticeObject.endFill();
	this.endNoticeObject.position = new PIXI.Point(15,520);
	this.container.addChild( this.endNoticeObject );
	
	this.endNoticeText = new PIXI.Text('Event Completed!\nResults are being calculated.',{font : '16px Arial', fill : 0xFFFFFF, align : 'center'});
	this.endNoticeText.pivot = new PIXI.Point(0.5,0.5);
	this.endNoticeText.position = new PIXI.Point(100,20);
	this.endNoticeObject.addChild( this.endNoticeText );
	
	return this.container;
}

LeaderBoardUIController.prototype.UpdateUI = function() {
}



