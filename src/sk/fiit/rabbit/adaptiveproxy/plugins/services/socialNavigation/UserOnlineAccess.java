package sk.fiit.rabbit.adaptiveproxy.plugins.services.socialNavigation;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserOnlineAccess {
    
    private static HashMap<String, String> mapUserUrl;
    private static HashMap<String, Integer> mapUrlCount;
    
    public static void push(String user, String url){
	Map<String, String> muu = getInstanceUserUrl();
	Map<String, Integer> muc = getInstanceUrlCount();
	String prevUrl = muu.put(user, url);
	if (!muc.containsKey(url)){
	    muc.put(url, 1);
	    if (prevUrl != url && prevUrl != null) {
		Integer count = muc.get(prevUrl);
		if (--count == 0) 
		    muc.remove(prevUrl);
		else
		    muc.put(prevUrl, count);
	    }
	} else{
	    if (prevUrl != url){
		Integer count = muc.get(url);
		muc.put(url, ++count);
		if (prevUrl != null){
		    count = muc.get(prevUrl);
		    if (--count == 0) 
			muc.remove(prevUrl);
		    else
			muc.put(prevUrl, count);
		}
	    }
	}	
    }
    
    public static JSONObject getResponse(String reqJson) throws ParseException{
	
	JSONObject json = (JSONObject) new JSONParser().parse(reqJson);
	JSONArray array = (JSONArray) json.get("pageUrlList");
	
	JSONObject retJson = new JSONObject();
	JSONArray retArray = new JSONArray();

	for (int i = 0; i < array.size(); i++) {
	    JSONObject j = (JSONObject) array.get(i);
	    String id = (String) j.get("id");
	    String url = (String) j.get("url");
	    int count = getByUrl(url);
		
	    j = new JSONObject();
	    j.put("id", id);
	    j.put("count", count);
	    
	    retArray.add(j);
	}
	
	retJson.put("peopleCount", retArray);
	
	return retJson;
    }
    
    private static int getByUrl(String url){
	Map<String, Integer> muc = getInstanceUrlCount();
	if (!muc.isEmpty() && muc.containsKey(url))
	{
	    return muc.get(url);
	}
	
	return 0;
    }
    
    private static Map<String, String> getInstanceUserUrl(){
	if (mapUserUrl == null)
	{
	    mapUserUrl = new HashMap<String, String>();
	}
	return mapUserUrl;
    }
    
    private static Map<String, Integer> getInstanceUrlCount(){
	if (mapUrlCount == null)
	{
	    mapUrlCount = new HashMap<String, Integer>();
	}
	return mapUrlCount;
    }
}
