package sk.fiit.rabbit.adaptiveproxy.plugins.services.socialNavigation;

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
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.LoggingBackendService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.RequestDataParserService;
import sk.fiit.rabbit.adaptiveproxy.plugins.servicedefinitions.UserIdentificationService;

public class OnlineUserActivityProcessingPlugin implements
		RequestProcessingPlugin {

	protected Logger logger = Logger
			.getLogger(OnlineUserActivityProcessingPlugin.class);

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
		String requestURI = request.getRequestHeader().getRequestURI();
		if (requestURI.contains(pattern)) {
			String user = request.getServicesHandle()
					.getService(UserIdentificationService.class)
					.getClientIdentification();
			String url = request.getOriginalRequest().getRequestHeader()
					.getField("referer");
			System.out.println("\n\n" + user + ", " + url + "\n\n");
			UserOnlineAccess.push(user, url);
		}

		return RequestProcessingActions.PROCEED;
	}

	@Override
	public void processTransferedRequest(HttpRequest request) {
	}

	@Override
	public void desiredRequestServices(
			Set<Class<? extends ProxyService>> desiredServices,
			RequestHeader clientRQHeader) {
		desiredServices.add(ModifiableStringService.class);
		desiredServices.add(RequestDataParserService.class);
		desiredServices.add(LoggingBackendService.class);
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
