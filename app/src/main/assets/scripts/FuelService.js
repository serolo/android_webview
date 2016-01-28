var FuelService = {
    messageHost : function (message) {
        if (window['FuelHostInterface'] != null && window['FuelHostInterface']['message'] != null) {
            window['FuelHostInterface']['message'](message);
        } else if (window['webkit'] != null && window['webkit']['messageHandlers'] != null && window['webkit']['messageHandlers']['message'] != null) {
            window['webkit']['messageHandlers']['message'].postMessage(message);
        } else if (window['ext'] && window['ext']['IDTK_APP'] && window['ext']['IDTK_APP']['makeCall']) {
            var hostFunction = window['ext']['IDTK_APP']['makeCall'];
            var call = 'FuelHost("' + message + '");'
            hostFunction("forward", call);
        } else {
            var consoleFrame = document.createElement("iframe");
            consoleFrame.src = 'fuelhost://' + message;
            consoleFrame.setAttribute("style", "position:absolute; top: 0px; left: 0px; z-index: -100; visibility: hidden; margin: 0; padding: 0; width: 0px;height: 0px;");
            document.documentElement.appendChild(consoleFrame);
            consoleFrame.parentNode.removeChild(consoleFrame);
            consoleFrame = null;
        }
    },

    log: function ( messageLog ) {
      var message = "sdkAction=Log";
      message += "&message="+ messageLog;
      this.messageHost( message );
    }
};


