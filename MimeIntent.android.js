'use strict';

var MimeIntentManager = require('react-native').NativeModules.MimeIntentManager;

var _canOpenURLWithMime = MimeIntentManager.canOpenURLWithMime;
var _openURLWithMime = MimeIntentManager.openURLWithMime;

var convertError = (err) => {
  if (err.isOperational && err.cause) {
    err = err.cause;
  }

  var error = new Error(err.description || err.message);
  error.code = err.code;
  throw error;
};

module.exports = {
    canOpenURLWithMime: function(url, mime) {
        return _canOpenURLWithMime(url, mime)
            .catch(convertError);
    },
    openURLWithMime: function(url, mime) {
        return _openURLWithMime(url, mime)
            .catch(convertError);
    }
};
