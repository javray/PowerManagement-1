	var exec = require("cordova/exec"),
	    utils = require('cordova/utils');

	function PowerManagement() {}

	/**
	 * Acquire a new wake-lock (keep device awake)
	 * 
	 * @param successCallback function to be called when the wake-lock was acquired successfully
	 * @param errorCallback function to be called when there was a problem with acquiring the wake-lock
	 */
	PowerManagement.prototype.acquire = function(successCallback,failureCallback) {
		cordova.exec(successCallback, failureCallback, 'PowerManagement', 'acquire', ['', utils.createUUID()]);
	};

	/**
	 * Release the wake-lock
	 * 
	 * @param successCallback function to be called when the wake-lock was released successfully
	 * @param errorCallback function to be called when there was a problem while releasing the wake-lock
	 */
	PowerManagement.prototype.release = function(watchId, successCallback,failureCallback) {
		cordova.exec(successCallback, failureCallback, 'PowerManagement', 'release', [watchId]);
	};

	/**
	 * Acquire a partial wake-lock, allowing the device to dim the screen
	 *
	 * @param successCallback function to be called when the wake-lock was acquired successfully
	 * @param errorCallback function to be called when there was a problem with acquiring the wake-lock
	 */
	PowerManagement.prototype.dim = function(successCallback,failureCallback) {
		
		cordova.exec(successCallback, failureCallback, 'PowerManagement', 'acquire', ["dim", utils.createUUID()]);
	};
	
	 /**
     * Acquire a partial wake-lock, allowing the device to turn off the screen but keep the CPU active
     *
     * @param successCallback function to be called when the wake-lock was acquired successfully
     * @param errorCallback function to be called when there was a problem with acquiring the wake-lock
     */
    PowerManagement.prototype.partial = function(successCallback,failureCallback) {
        cordova.exec(successCallback, failureCallback, 'PowerManagement', 'acquire', ["partial", utils.createUUID()]);
    }
    
    PowerManagement.prototype.screen = function(successCallback,failureCallback) {
        cordova.exec(successCallback, failureCallback, 'PowerManagement', 'acquire', ["screen", utils.createUUID()]);
    }


	var powerManagement = new PowerManagement();
	module.exports = powerManagement;
