package com.grepgame.android.plugin.grplugin;

import org.json.JSONArray;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


public class Koi {
	
	//The tag for debugging in LogCat
	public static final String KOI_TAG = "koin_plugin";
	
	//The result returned from this plugin
	private byte[] pluginData = null;
	
	public static Koi instance = new Koi();
	
	public Context unityActivity;
	
	//The name of the GameObject in Unity's Scene which is used to communicate with this Plugin
	public static final String KOI_GAMEOBJECT = "KoiGameObject";
	
	/**
	* Return the result to Unity
	*/
	public byte[] getPluginData(){
		return pluginData;
	}
	
	/**
	* Set the data result to return later
	*/
	public void setPluginData(byte[] data){
		pluginData = data;
	}
	
	/**
	* Clear all data
	*/
	public void cleanUp(){
		pluginData = null;
		unityActivity = null;
	}
	
	/**
	* Start an Android Activity, this method is called in Unity	
	*/
	public void launchAndroidActivity(Context context, Class<?> activityClass){
		
		this.unityActivity = context;
		Intent myIntent = new Intent(context, activityClass);
		context.startActivity(myIntent);
	}
	
	/**
	* Open phone's gallery
	*/
	public void launchGallery(Context context){
		launchAndroidActivity(context,KoiGalleryActivity.class );
	}
	
	/**
	* Start phone's camera
	*/
	public void launchCamera(Context context){
		launchAndroidActivity(context,KoiCameraActivity.class );
	}
	
	/**
	* Go back to Unity (from Android Activity)
	*/
	public void backToUnity(Activity androiActivity){
		Intent myIntent = new Intent(androiActivity, unityActivity.getClass());
		unityActivity.startActivity(myIntent);
	}
	
	/**
	 * Send message to UNity's GameObject (named as Koi.KOI_GAMEOBJECT) 
	 * @param method name of the method in GameObject's script
	 * @param message the actual message
	 */
	public static void sendMessageToKoiObject(String method, String message){
		UnityPlayer.UnitySendMessage(Koi.KOI_GAMEOBJECT, method, message);
	}
	
	public static void sendMessageToKoiObject(String method, JSONArray parameters){
		
		sendMessageToKoiObject(method, parameters.toString());
		
	}

}
