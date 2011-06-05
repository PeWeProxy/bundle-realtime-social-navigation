package sk.fiit.rabbit.adaptiveproxy.plugins.services.socialNavigation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.svenson.JSON;
import org.svenson.JSONParser;

public class UserOnlineAccess {
    
	private static Map<String, String> mapUserUrl;
    private static Map<String, Integer> mapUrlCount;
    
	public static void push(String user, String url) {
		Map<String, String> userUrlMap = getInstanceUserUrl();
		Map<String, Integer> urlCountMap = getInstanceUrlCount();
		
		String prevUrl = userUrlMap.put(user, url);
		
		if (!urlCountMap.containsKey(url)) {
			urlCountMap.put(url, 1);
			if ((!url.equals(prevUrl)) && prevUrl != null) {
				Integer count = urlCountMap.get(prevUrl);
				if (--count == 0)
					urlCountMap.remove(prevUrl);
				else
					urlCountMap.put(prevUrl, count);
			}
		} else {
			if (!url.equals(prevUrl)) {
				Integer count = urlCountMap.get(url);
				urlCountMap.put(url, ++count);
				if (prevUrl != null) {
					count = urlCountMap.get(prevUrl);
					if (--count == 0)
						urlCountMap.remove(prevUrl);
					else
						urlCountMap.put(prevUrl, count);
				}
			}
		}
	}
    
    public static String getUrlCounts(String pageUrlListJson) {
		HashMap<String, ArrayList<HashMap<String, String>>> pageUrlLisJsonMap = JSONParser.defaultJSONParser().parse(HashMap.class, pageUrlListJson);
		
		ArrayList<HashMap<String, String>> pageUrlListList =  pageUrlLisJsonMap.get("pageUrlList");
		ArrayList<HashMap<String, String>> pageUrlCountList = new ArrayList<HashMap<String,String>>(pageUrlListList.size()); 
		
		for(HashMap<String, String> urlEntry : pageUrlListList){
			String id = (String) urlEntry.get("id");
		    String url = (String) urlEntry.get("url");
		    
		    int count = getByUrl(url);
		    
		    HashMap<String, String> countEntry = new HashMap<String, String>(2);
		    countEntry.put("id", id);
		    countEntry.put("count", Integer.toString(count));
		    
		    pageUrlCountList.add(countEntry);
		}
		
		HashMap<String, ArrayList<HashMap<String, String>>> linkCountMap = new HashMap<String, ArrayList<HashMap<String,String>>>();
		linkCountMap.put("peopleCount", pageUrlCountList);
		return JSON.defaultJSON().forValue(linkCountMap);
    }
    
    private static int getByUrl(String url){
	Map<String, Integer> muc = getInstanceUrlCount();
	if (!muc.isEmpty() && muc.containsKey(url))
	{
	    return muc.get(url);
	}
	
	//UGLY CRUTCH: replacing double slashes
	url = "http://" + (url.split("http://")[1]).replace("//", "/");
	if (!muc.isEmpty() && muc.containsKey(url))
	{
	    return muc.get(url);
	}
	
	return 0;
    }
    
	private static Map<String, String> getInstanceUserUrl() {
		return UserUrlHolder.getInstance();
	}

	private static Map<String, Integer> getInstanceUrlCount() {
		return UrlCountHolder.getInstance();
	}
	
	static class UserUrlHolder {
		static Map<String, String> instance;
		
		static {
			instance = new HashMap<String, String>();
		}

		public static Map<String, String> getInstance() {
			return instance;
		}
	}
	
	static class UrlCountHolder {
		static Map<String, Integer> instance;
		
		static {
			instance = new HashMap<String, Integer>();
		}

		public static Map<String, Integer> getInstance() {
			return instance;
		}
	}
}
