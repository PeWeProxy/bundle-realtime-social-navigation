package sk.fiit.rabbit.adaptiveproxy.plugins.services.socialNavigation;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import sk.fiit.peweproxy.headers.RequestHeader;
import sk.fiit.peweproxy.messages.HttpMessageFactory;
import sk.fiit.peweproxy.messages.HttpRequest;
import sk.fiit.peweproxy.messages.HttpResponse;
import sk.fiit.peweproxy.messages.ModifiableHttpRequest;
import sk.fiit.peweproxy.plugins.PluginProperties;
import sk.fiit.peweproxy.plugins.processing.RequestProcessingPlugin;
import sk.fiit.peweproxy.services.ProxyService;
import sk.fiit.peweproxy.services.content.ModifiableStringService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.PostDataParserService;

public class OnlineUserActivityProcessingPlugin implements
	RequestProcessingPlugin {
    
    protected Logger logger = Logger.getLogger(OnlineUserActivityProcessingPlugin.class);
    
    private String pattern = null;
    
    @Override
    public HttpRequest getNewRequest(ModifiableHttpRequest request,
	    HttpMessageFactory messageFactory) {
	return null;
    }

    @Override
    public HttpResponse getResponse(ModifiableHttpRequest request,
	    HttpMessageFactory messageFactory) {
	return null;
    }

    @Override
    public RequestProcessingActions processRequest(ModifiableHttpRequest request) {	
	return RequestProcessingActions.PROCEED;
    }

    @Override
    public void processTransferedRequest(HttpRequest request) {
	
	if(request.getOriginalRequest().getRequestHeader().getRequestURI().contains(pattern)
		&& request.getServicesHandle().isServiceAvailable(PostDataParserService.class)) {
	
	    Map<String, String> postData = request.getServicesHandle().getService(PostDataParserService.class).getPostData();
        	
	    if (postData.containsKey("__peweproxy_uid")) {
        	String user = postData.get("__peweproxy_uid");
        	String url = request.getOriginalRequest().getRequestHeader().getRequestURI();
        	UserOnlineAccess.push(user, url);
	    }
	}
    }

    @Override
    public void desiredRequestServices(
	    Set<Class<? extends ProxyService>> desiredServices,
	    RequestHeader clientRQHeader) {
	desiredServices.add(PostDataParserService.class);
	desiredServices.add(ModifiableStringService.class);
    }

    @Override
    public boolean start(PluginProperties props) {
	pattern = props.getProperty("pattern");
	
	return true;
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean supportsReconfigure(PluginProperties newProps) {
	return false;
    }

}
