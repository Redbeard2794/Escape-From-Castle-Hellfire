package ie.itcarlow.CastleHell;

//import ie.itcarlow.MessageHandler;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.codebutler.android_websockets.WebSocketClient;

public class WebSocket {
	private final String TAG = "WebSocket"; 
	private WebSocketClient mClient;
	boolean joinSent = false;
	
	int numPlayers = 1;
	private MessageHandler mHandler;

	public WebSocket(MessageHandler handler) {
		//mHandler = handler;
		List<BasicNameValuePair> extraHeaders = Arrays.asList(
			    new BasicNameValuePair("Cookie", "session=abcd")
		);
		
		//"ws://10.0.2.2:8080/wstest"
		mClient = new WebSocketClient(URI.create("ws://10.0.3.2:8080/wstest"), new WebSocketClient.Listener() {
		    @Override
		    public void onConnect() {
		       Log.d(TAG, "Connected!");
		     
		    }

		    @Override
		    public void onMessage(String message) {
		        Log.d(TAG, String.format("Got string message! %s", message));
		        try {
		        		JSONObject json = new JSONObject(message);
		        		String type = json.getString("type");
		        		String pid = json.getString("data");
		        		Log.d(TAG, "Type is: "+type+"Data is:" +pid);
		        		//if(pid == "0")
		        		
		        			mHandler.handleMessage(message);//"waiting");  
		        		
		        		//if(json.getString("type") == "join")
		        		//{
		        			//mHandler.handleMessage(pid);   
		        		//}
		        	} 
		        catch(JSONException e) {
		        	}  

		       
		    }

		    @Override
		    public void onMessage(byte[] data) {
		        //Log.d(TAG, String.format("Got binary message! %s", toHexString(data));
		    }

		    @Override
		    public void onDisconnect(int code, String reason) {
		        Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
		    }

		    @Override
		    public void onError(Exception error) {
		        Log.e(TAG, "Error!", error);
		    }
		}, extraHeaders);

		mClient.connect();
		
		
	}
	
	void sendMessage() {
    	// Use the WebSocketClient (mClient) here to 
  	  	// send JSON messages to the server. 
		
		try { 
			JSONObject json  = new JSONObject();
			json.put("type", "join");
			json.put("pid", "Android Client");
			mClient.send(json.toString());
			//joinSent = true;
			//numPlayers+=1;
			//Log.d(TAG, "number of players: " + numPlayers);
			//mHandler.handleMessage(json.toString());
		}
		catch(JSONException e) {
			Log.e(TAG, "JSONException!" + e.toString());
		}
		
	}
	void sendMessage(int x, int y)
	{
		JSONObject json  = new JSONObject();
		try {
			Map<String, Integer> pos = new HashMap<String, Integer>();
			pos.put("X", x);
			pos.put("Y", y);
			json.put("type", "updateState");
			json.put("data", pos);
			mClient.send(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
