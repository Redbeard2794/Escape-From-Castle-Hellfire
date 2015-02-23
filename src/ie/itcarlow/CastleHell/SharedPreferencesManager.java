package ie.itcarlow.CastleHell;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
	 private static SharedPreferencesManager mInstance = null;
	 
	 private SharedPreferences mSharedPref;
	 private Activity mActivity;
	  
	 private SharedPreferencesManager(Activity activity){
		 mActivity = activity;
		 mSharedPref = activity.getPreferences(Context.MODE_PRIVATE);
	 }
	  
	 public static SharedPreferencesManager getInstance(Activity activity){
		 if(mInstance == null) {
			 mInstance = new SharedPreferencesManager(activity);
		 }
	 
		 return mInstance;
	 }
	 
	 public void saveDebugDraw(boolean debugDrawState)
	 {
		 mSharedPref = mActivity.getPreferences(Context.MODE_PRIVATE);
		 SharedPreferences.Editor editor = mSharedPref.edit();
		 editor.putBoolean("state", debugDrawState);
		 editor.commit();
	 }
	 
	 public boolean getDebugDraw()
	 {
		 boolean defaultValue = false;
		 boolean debugstate = mSharedPref.getBoolean("state", defaultValue);
		 return debugstate;
	 }
}
