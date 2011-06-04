package sk.fiit.rabbit.adaptiveproxy.plugins.services.peoplemeter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import sk.fiit.peweproxy.messages.HttpMessageFactory;
import sk.fiit.peweproxy.messages.HttpResponse;
import sk.fiit.peweproxy.messages.ModifiableHttpRequest;
import sk.fiit.peweproxy.messages.ModifiableHttpResponse;
import sk.fiit.peweproxy.services.content.ModifiableStringService;
import sk.fiit.peweproxy.services.content.StringContentService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.DatabaseConnectionProviderService;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.bubble.BubbleMenuProcessingPlugin;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.socialNavigation.UserOnlineAccess;

public class RealtimeSocialNavigationProcessingPlugin extends BubbleMenuProcessingPlugin {

	@Override
	public HttpResponse getResponse(ModifiableHttpRequest request, HttpMessageFactory messageFactory) {
		
		String content = "";
	
		if (!request.getServicesHandle().isServiceAvailable(DatabaseConnectionProviderService.class))
		    return messageFactory.constructHttpResponse(null, "text/html");
		
		//String url = request.getOriginalRequest().getRequestHeader().getRequestURI();
		
		if (request.getRequestHeader().getRequestURI().contains("action=getPeoplemeterActivity")) {
		    //String uid = url.split("&")[1].split("=")[1];
		    content = "TRUE";
		}
		if (request.getRequestHeader().getRequestURI().contains("action=setPeoplemeterActivity")) {
		    //String activity = url.split("&")[1].split("=")[1];
		    //String uid = url.split("&")[2].split("=")[1];
		    content = "OK";
		}
		if (request.getRequestHeader().getRequestURI().contains("action=updateCounts")) {
		    StringContentService stringContentService = request.getServicesHandle().getService(StringContentService.class);
		    try {
		    	String parameters = URLDecoder.decode(stringContentService.getContent(), "utf-8");
		    	String pageUrlListJson = parameters.split("pageUrlList=")[1];
		    	System.err.println(pageUrlListJson);
		    	if (pageUrlListJson != null) {
		    		content = UserOnlineAccess.getUrlCounts(pageUrlListJson);
		    		System.err.println(content);
		    	}
		    } catch (UnsupportedEncodingException e1) {
		    	// TODO Auto-generated catch block
		    }
		}
		
		ModifiableHttpResponse httpResponse = messageFactory.constructHttpResponse(null, "text/html");
		ModifiableStringService stringService = httpResponse.getServicesHandle().getService(ModifiableStringService.class);
		stringService.setContent(content);
		
		return httpResponse;
	}
}
