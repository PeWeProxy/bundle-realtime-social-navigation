package sk.fiit.rabbit.adaptiveproxy.plugins.services.peoplemeter;

import java.util.Map;

import org.json.simple.parser.ParseException;

import sk.fiit.peweproxy.messages.HttpMessageFactory;
import sk.fiit.peweproxy.messages.HttpResponse;
import sk.fiit.peweproxy.messages.ModifiableHttpRequest;
import sk.fiit.peweproxy.messages.ModifiableHttpResponse;
import sk.fiit.peweproxy.services.content.ModifiableStringService;
import sk.fiit.peweproxy.services.content.StringContentService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.DatabaseConnectionProviderService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.PostDataParserService;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.bubble.BubbleMenuProcessingPlugin;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.socialNavigation.UserOnlineAccess;

public class RealtimeSocialNavigationProcessingPlugin extends BubbleMenuProcessingPlugin {

	@Override
	public HttpResponse getResponse(ModifiableHttpRequest request, HttpMessageFactory messageFactory) {
		
		String content = "";
		String USER_SETTINGS = "USER_SETTINGS";
	
		if (!request.getServicesHandle().isServiceAvailable(DatabaseConnectionProviderService.class))
		    return messageFactory.constructHttpResponse(null, "text/html");
		
		String url = request.getOriginalRequest().getRequestHeader().getRequestURI();
		
		if (request.getRequestHeader().getRequestURI().contains("action=getPeoplemeterActivity")) {
		    String uid = url.split("&")[1].split("=")[1];
		    content = "true";
		}
		if (request.getRequestHeader().getRequestURI().contains("action=setPeoplemeterActivity")) {
		    String activity = url.split("&")[1].split("=")[1];
		    String uid = url.split("&")[2].split("=")[1];
		    content = "OK";
		}
		if (request.getRequestHeader().getRequestURI().contains("action=updateCounts")) {
		    if (!request.getServicesHandle().isServiceAvailable(PostDataParserService.class))
		    	return messageFactory.constructHttpResponse(null, "text/html");
		    
		    Map<String, String> postData = request.getServicesHandle().getService(PostDataParserService.class).getPostData();
		    StringContentService stringContentService = request.getServicesHandle().getService(StringContentService.class);
		    System.out.println(stringContentService.getContent());
		    String reqJson = postData.get("pageUrlList");
		    try {
			if (reqJson != null)
			    content = UserOnlineAccess.getResponse(reqJson).toJSONString();
		    } catch (ParseException e) {
			e.printStackTrace();
		    }
		}
		
		ModifiableHttpResponse httpResponse = messageFactory.constructHttpResponse(null, "text/html");
		ModifiableStringService stringService = httpResponse.getServicesHandle().getService(ModifiableStringService.class);
		stringService.setContent(content);
		
		return httpResponse;
	}
}
