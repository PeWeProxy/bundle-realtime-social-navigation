package sk.fiit.rabbit.adaptiveproxy.plugins.services.peoplemeter;

import sk.fiit.peweproxy.messages.HttpMessageFactory;
import sk.fiit.peweproxy.messages.HttpResponse;
import sk.fiit.peweproxy.messages.ModifiableHttpRequest;
import sk.fiit.peweproxy.messages.ModifiableHttpResponse;
import sk.fiit.peweproxy.services.content.ModifiableStringService;
import sk.fiit.rabbit.adaptiveproxy.plugins.services.injector.ClientBubbleMenuProcessingPlugin;

public class RealtimeSocialNavigationProcessingPlugin extends ClientBubbleMenuProcessingPlugin {

	@Override
	public HttpResponse getResponse(ModifiableHttpRequest request, HttpMessageFactory messageFactory) {
		
		String content = "";
		
	
		if (request.getRequestHeader().getRequestURI().contains("action=getPeoplemeterActivity")) {
			content = "TRUE";
			
		}
		if (request.getRequestHeader().getRequestURI().contains("action=setPeoplemeterActivity")) {
			content = "OK";
		}
		if (request.getRequestHeader().getRequestURI().contains("action=updateCounts")) {
			content = "{ \"peopleCount\" : [ { \"id\": 0, \"count\": 2 }, { \"id\": 1, \"count\": 12 }, { \"id\": 2, \"count\": 9 }, { \"id\": 3, \"count\": 0 }, { \"id\": 4, \"count\": 10 }," +
					" { \"id\": 5, \"count\": 3 }," +
					" { \"id\": 6, \"count\": 3 }," +
					" { \"id\": 7, \"count\": 3 }," +
					" { \"id\": 8, \"count\": 3 }," +
					" { \"id\": 9, \"count\": 3 }," +
					" { \"id\": 10, \"count\": 3 }," +
					" { \"id\": 11, \"count\": 3 }," +
					" { \"id\": 12, \"count\": 3 }," +
					" { \"id\": 13, \"count\": 3 }," +
					" { \"id\": 14, \"count\": 3 }," +
					" { \"id\": 15, \"count\": 3 }," +
					" { \"id\": 16, \"count\": 3 }," +
					" { \"id\": 17, \"count\": 3 }," +
					" { \"id\": 18, \"count\": 3 }," +
					" { \"id\": 19, \"count\": 3 }," +
					" { \"id\": 20, \"count\": 3 }," +
					" { \"id\": 21, \"count\": 3 }," +
					" { \"id\": 22, \"count\": 3 }," +
					" { \"id\": 23, \"count\": 3 }," +
					" { \"id\": 24, \"count\": 3 }," +
					" { \"id\": 25, \"count\": 3 }," +
					" { \"id\": 26, \"count\": 3 }," +
					" { \"id\": 27, \"count\": 3 }," +
					" { \"id\": 28, \"count\": 3 }," +
					" { \"id\": 29, \"count\": 3 }," +
					" { \"id\": 30, \"count\": 3 }," +
					" { \"id\": 31, \"count\": 3 }," +
					" { \"id\": 32, \"count\": 3 }," +
					" { \"id\": 33, \"count\": 3 }," +
					" { \"id\": 34, \"count\": 3 }," +
					" { \"id\": 35, \"count\": 3 }," +
					" { \"id\": 36, \"count\": 3 }," +
					"{ \"id\": 37, \"count\": 101 } ]}";
		}
		
		ModifiableHttpResponse httpResponse = messageFactory.constructHttpResponse(null, "text/html");
		ModifiableStringService stringService = httpResponse.getServicesHandle().getService(ModifiableStringService.class);
		stringService.setContent(content);
		
		return httpResponse;
	}
}
