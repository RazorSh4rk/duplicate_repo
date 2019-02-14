let title = document.getElementById('issue_title'),
	body = document.getElementById('issue_title')

let ioService = Components.classes["@mozilla.org/network/io-service;1"].getService(Components.interfaces.nsIIOService)
let uri = ioService.newURI('http://localhost:8081/ping/', null, null)
let channel = ioService.newChannelFromURI(uri)

let listener = new StreamListener(function(data) { alert(data) })

channel.notificationCallbacks = listener
channel.asyncOpen(listener, null)

function StreamListener(aCallbackFunc) {
  this.mCallbackFunc = aCallbackFunc
}

StreamListener.prototype = {
  mData: "",

  // nsIStreamListener
  onStartRequest: function (aRequest, aContext) {
    this.mData = "";
  },

  onDataAvailable: function (aRequest, aContext, aStream, aSourceOffset, aLength) {
    var scriptableInputStream = 
      Components.classes["@mozilla.org/scriptableinputstream;1"]
        .createInstance(Components.interfaces.nsIScriptableInputStream);
    scriptableInputStream.init(aStream);

    this.mData += scriptableInputStream.read(aLength);
  },

  onStopRequest: function (aRequest, aContext, aStatus) {
    if (Components.isSuccessCode(aStatus)) {
      // request was successfull
      this.mCallbackFunc(this.mData);
    } else {
      // request failed
      this.mCallbackFunc(null);
    }

    gChannel = null;
  },

  // nsIChannelEventSink
  onChannelRedirect: function (aOldChannel, aNewChannel, aFlags) {
    // if redirecting, store the new channel
    gChannel = aNewChannel;
  },

  // nsIInterfaceRequestor
  getInterface: function (aIID) {
    try {
      return this.QueryInterface(aIID);
    } catch (e) {
      throw Components.results.NS_NOINTERFACE;
    }
  },

  // nsIProgressEventSink (not implementing will cause annoying exceptions)
  onProgress : function (aRequest, aContext, aProgress, aProgressMax) { },
  onStatus : function (aRequest, aContext, aStatus, aStatusArg) { },

  // nsIHttpEventSink (not implementing will cause annoying exceptions)
  onRedirect : function (aOldChannel, aNewChannel) { },

  // we are faking an XPCOM interface, so we need to implement QI
  QueryInterface : function(aIID) {
    if (aIID.equals(Components.interfaces.nsISupports) ||
        aIID.equals(Components.interfaces.nsIInterfaceRequestor) ||
        aIID.equals(Components.interfaces.nsIChannelEventSink) || 
        aIID.equals(Components.interfaces.nsIProgressEventSink) ||
        aIID.equals(Components.interfaces.nsIHttpEventSink) ||
        aIID.equals(Components.interfaces.nsIStreamListener))
      return this;

    throw Components.results.NS_NOINTERFACE;
  }
};