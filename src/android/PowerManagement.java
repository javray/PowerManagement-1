/*
   Copyright 2011-2012 Wolfgang Koller - http://www.gofg.at/

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

/**
 * Cordova (Android) plugin for accessing the power-management functions of the device
 * @author Wolfgang Koller <viras@users.sourceforge.net>
 */
package org.apache.cordova.plugin;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;

/**
 * Plugin class which does the actual handling
 */
public class PowerManagement extends CordovaPlugin {
	// As we only allow one wake-lock, we keep a reference to it here
	private PowerManager.WakeLock wakeLock = null;
	private PowerManager powerManager = null;
	
	public HashMap<String, PowerManager.WakeLock> watches = new HashMap<String, CallbackContext>();
	
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {

		PluginResult result = null;
		Log.d("PowerManagementPlugin", "Plugin execute called - " + this.toString() );
		Log.d("PowerManagementPlugin", "Action is " + action );
		
		try {
			if( action.equals("acquire") ) {
				String type = args.optString(0);
				String watchId = args.optString(1);
				
				if(type.equals("dim") ) {
					Log.d("PowerManagementPlugin", "Only dim lock" );
					result = this.acquire( PowerManager.SCREEN_DIM_WAKE_LOCK, watchId);
				}
				else if(type.equals("partial") ) {
					Log.d("PowerManagementPlugin", "Only partial lock" );
					result = this.acquire( PowerManager.PARTIAL_WAKE_LOCK, watchId);
				}
				else if(type.equals("screen") ) {
					Log.d("PowerManagementPlugin", "Full wakelock with screen" );
					result = this.acquire( PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, watchId);
				}
				else {
					Log.d("PowerManagementPlugin", "Full wakelock" );
					result = this.acquire( PowerManager.FULL_WAKE_LOCK, watchId);
				}
				
			}
			else if( action.equals("release") ) {
				result = this.release(watchId);
			}
		}
		catch( Exception e ) {
			result = new PluginResult(Status.JSON_EXCEPTION, e.getMessage());
		}
		
		callbackContext.sendPluginResult(result);
		return true;
	}
	
	/**
	 * Acquire a wake-lock
	 * @param p_flags Type of wake-lock to acquire
	 * @return PluginResult containing the status of the acquire process
	 */
	private PluginResult acquire( int p_flags , String watchId) {
		PluginResult result = null;
		PowereManger.WakeLock wakeLock = null;
		PowerManager powerManager = (PowerManager) cordova.getActivity().getSystemService(Context.POWER_SERVICE);
		
		wakeLock = powerManager.newWakeLock(p_flags, "PowerManagementPlugin");
		
		try {
			wakeLock.acquire();
			this.watches.put(watchId, wakeLock);
			result = new PluginResult(PluginResult.Status.OK, watchId);
		}
		catch( Exception e ) {
			result = new PluginResult(PluginResult.Status.ERROR,"Can't acquire wake-lock - check your permissions!");
		}
		
		
		return result;
	}
	
	/**
	 * Release an active wake-lock
	 * @return PluginResult containing the status of the release process
	 */
	private PluginResult release(String watchId) {
		PluginResult result = null;
		
		if (this.watches.containsKey(watchId)) {
			PowerManaer.WakeLock = wakeLock = this.watches.get(watchId);
			wakeLock.release();
			this.watches.remove(watchId);
			
			result = new PluginResult(PluginResult.Status.OK, "OK");
		}
		else {
			result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION, "No WakeLock active - acquire first");
		}
		
		return result;
	}
}
