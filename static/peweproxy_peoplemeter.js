peweproxy.register_module('rsn', function($) {

	var peoplemeterButonSelector = 'div#peweproxy_icon_banner a.__proxy_peoplemeter_button';
	
	var __proxy_followedPages = new Array;
	var __proxy_peoplemeter_html = '<div id="__proxy_peoplemeterBubble" style="display: none; position: absolute;"><div id="__proxy_peoplemeterTop" class="__proxy_top_TR"></div><div id="__proxy_peoplemeterMiddle" class="__proxy_middle"><span id="__proxy_counter">0</span> ľudí práve prehliada túto stránku</div><div id="__proxy_peoplemeterBottom" class="__proxy_bottom"></div></div>';
	var __proxy_peoplemeter_active = false;
	var __proxy_peoplemeter_active_after_onload = true;
	var __proxy_normalizedUrl = "";
	
	$(document).ready(function() {
		 peweproxy.on_uid_ready(function(){
			$(peoplemeterButonSelector).click(function(){
				$('#__proxy_peoplemeterMenu').hide().removeClass('hidden').fadeIn('fast');
				return false;
			});
			$('div#__proxy_peoplemeterMenu a.__proxy_peoplemeter_closebutton').click(function(){
				$(this).blur();
				$('#__proxy_peoplemeterMenu').fadeOut('fast');
				return false;
			});
	          
			$("body").append(__proxy_peoplemeter_html);
			__proxy_initializeTracking();
	  	
	  		$(document).mousemove(function(event){
	  			$('#__proxy_peoplemeterBubble').css('left',event.pageX-16);
	  			$('#__proxy_peoplemeterBubble').css('top',event.pageY+20);
	  		});
	  
			$.post('adaptive-proxy/peoplemeter_call.html?action=getPeoplemeterActivity&userId=' + peweproxy.uid, {
				action: "getPeoplemeterActivity"
			}, function(data) {
				if (data === "TRUE") {
					__proxy_peoplemeter_active = true
					__proxy_updatePeoplemeter();
					setInterval("peweproxy.modules.rsn.__proxy_updatePeoplemeter()",4000);
				}
				else {
					__proxy_peoplemeter_active = false;
				}
			});
	  
			$("#__proxy_peoplemeter_deactivateButton").click(function () {
				$.post('adaptive-proxy/peoplemeter_call.html?action=setPeoplemeterActivity&activity=false&userId=' + peweproxy.uid, {
					action: "setPeoplemeterActivity"
				}, function(data) {
					$("#__proxy_peoplemeter").fadeOut(80);
					if (data == "OK") __proxy_peoplemeter_active = false;
				});
				return false;
			});
	  
			$("#__proxy_peoplemeter_activateButton").click(function () {
				$.post('adaptive-proxy/peoplemeter_call.html?action=setPeoplemeterActivity&activity=true&userId=' + peweproxy.uid, {
					action: "setPeoplemeterActivity"
				},function(data) {
					if (data == "OK") {
						__proxy_peoplemeter_active = true;
						__proxy_updatePeoplemeter();
						if (__proxy_peoplemeter_active_after_onload) {
							setInterval("peweproxy.modules.rsn.__proxy_updatePeoplemeter()",4000);
						} else {
							__proxy_peoplemeter_active_after_onload = false;
						}
					}
				});
				return false;
			});
		});
	});
	
	var __proxy_initializeTracking = function() {
		var __proxy_forbidenUrls = new Array;
		var __proxy_flag = true;
		var pageIndex = 0;
		$.each($("#peweproxy_addons_container a"), function(index, value) {
			__proxy_forbidenUrls[index] = new Array(value);
		});
	
		$.each($("a"), function(index, value) {
			__proxy_flag = true;
			$.each(__proxy_forbidenUrls, function(forbidenIndex, forbidenValue) {
				if ($(value).attr("href") == $(forbidenValue).attr("href")) __proxy_flag = false;
                                if ($(value).attr("href") == undefined || $(value).attr("href") == "#") __proxy_flag = false;
			});
			if (__proxy_flag == true) {
				__proxy_appendHover(value, pageIndex);
				__proxy_normalizeUrl($(value).attr("href"));
				__proxy_followedPages[pageIndex] = new Array(value, __proxy_normalizedUrl, "-1");
				pageIndex++;
			}
		});
	}
	
	var __proxy_normalizeUrl = function(url) {
		if ((url.substr(0, 7) != "http://") && (url.substr(0, 8) != "https://")) {
			if (url.indexOf(".") == 0) {
				normalizedUrlPart = document.domain.substring(0, document.domain.lastIndexOf("/"));
				if ((url[0] == "/") || (normalizedUrlPart[normalizedUrlPart.length - 1] == "/")) {
					if ((url[0] == "/") && (normalizedUrlPart[normalizedUrlPart.length - 1] == "/")) {
						normalizedUrl = normalizedUrlPart + url.substring(1);
					} else {
						normalizedUrl = normalizedUrlPart + url;
					}
				} else {
					normalizedUrl = normalizedUrlPart + "/" + url;
				}
			} else {
				if ((url[0] == "/") || (document.domain[document.domain.length - 1] == "/")) {
					if ((url[0] == "/") && (document.domain[document.domain.length - 1] == "/")) {
						normalizedUrl = document.domain + url.substring(1);
					} else {
						normalizedUrl = document.domain + url;
					}
				} else {
					normalizedUrl = document.domain + "/" + url;
				}
			}
			__proxy_normalizedUrl = "http://" + normalizedUrl;
			return __proxy_normalizedUrl;
		} else {
			__proxy_normalizedUrl = url;
			return url;
		}
	}
	
	var __proxy_appendHover = function(element, index) {
		var offset = $(element).offset();
		$(element).mouseenter(function () {
			if (__proxy_followedPages[index][2] != "-1") {
				$("#__proxy_peoplemeterTop").addClass("__proxy_top_BR");
				$("#__proxy_peoplemeterBottom").addClass("__proxy_bottom_B");
				if (__proxy_peoplemeter_active) {
					$("#__proxy_peoplemeterBubble").fadeIn(0);
					$("#__proxy_counter").text(__proxy_followedPages[index][2]);
				}
			}
		})
		$(element).mouseleave(function () {
			$("#__proxy_peoplemeterBubble").fadeOut(0);
		}
		)
	}
	
	var __proxy_updatePeoplemeter = function() {
		var pageUrlList = '{ "pageUrlList" : [';
		$.each(__proxy_followedPages, function(index, value) {
			pageUrlList += '{ "id":"' + index + '", "url":"' + $(value)[1] + '" }';
			if (__proxy_followedPages.length != (index + 1)) pageUrlList += ",";
		});
		pageUrlList += ']}';
	
		$.post('adaptive-proxy/peoplemeter_call.html?action=updateCounts', {
			pageUrlList: pageUrlList
		},
		function(data) {
			response = $.parseJSON(data);
			$.each(response.peopleCount, function(index, value) {
				});
			$.each(response.peopleCount, function(index, value) {
				__proxy_followedPages[value.id][2] = value.count;
			});
                        __proxy_fillInUrlList();
		});
	}

        var __proxy_fillInUrlList = function() {
            $("#__proxy_peoplemeter_url_list_table").text("");
            var __proxy_followedPagesSorted = __proxy_followedPages.concat();
            __proxy_followedPagesSorted.sort(compareFollowedPagesFunction);
            var previousItem = "";
            var displayCount = 0;
            var max_displayCount = $.trim($("#rsn_link_count .__peweproxy_preference_table_value .__peweproxy_preference_row_display").text());
            max_displayCount = max_displayCount == "" ? 5 : max_displayCount;
            $.each(__proxy_followedPagesSorted, function(index, value){
                if (previousItem == $(value)[1]) return;
                if (displayCount >= max_displayCount) return false;
                $("#__proxy_peoplemeter_url_list_table").append("<tr><td class=\"first\"><a title=\"" + $(value)[1] + "\" href=\"" + $(value)[1] + "\">" + $(value)[1] + "</a></td>\n\
                                                                     <td><b>" + $(value)[2] + "</b> používateľov</td></tr>");
                previousItem = $(value)[1];
                displayCount++;
            });
        }
        var compareFollowedPagesFunction = function (a, b){
            if(a[2] < b[2]) return true;
            return false;
        }

	this.__proxy_updatePeoplemeter = __proxy_updatePeoplemeter;

});