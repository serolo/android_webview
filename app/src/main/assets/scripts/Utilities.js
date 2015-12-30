var TimeIsInPast = function( time ) {
	var currentTime = Math.floor((new Date()).getTime()/1000);
    if (time < currentTime ) {
    	return true;
    }
    return false;
}

var TimeIsInFuture = function( time ) {
	var currentTime = Math.floor((new Date()).getTime()/1000);
    if (time > currentTime ) {
    	return true;
    }
    return false;
}