var exec = require('cordova/exec');

exports.startTrackLocation = function (arg0, success, error) {
    exec(success, error, 'LocationCriteriaDB', 'startTrackLocation', [arg0]);
};

exports.stopTrackLocation = function (success, error) {
    exec(success, error, 'LocationCriteriaDB', 'stopTrackLocation');
};

exports.deleteAllDB = function (success, error) {
    exec(success, error, 'LocationCriteriaDB', 'deleteAllDB');
};

exports.getDB = function (success, error) {
    exec(success, error, 'LocationCriteriaDB', 'getDB');
};