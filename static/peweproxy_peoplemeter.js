
var peoplemeterButonSelector = 'div#peweproxy_icon_banner a.__proxy_peoplemeter_button';

var __proxy_followedPages = new Array;
var __proxy_peoplemeter_html = '<div id="__proxy_peoplemeterBubble" style="display: none; position: absolute;"><div id="__proxy_peoplemeterTop" class="__proxy_top_TR"></div><div id="__proxy_peoplemeterMiddle" class="__proxy_middle"><span id="__proxy_counter">0</span> ľudí práve prehliada túto stránku</div><div id="__proxy_peoplemeterBottom" class="__proxy_bottom"></div></div>';
var __proxy_peoplemeter_active = false;
var __proxy_peoplemeter_active_after_onload = true;

var temp = function($) {
    $(document).ready(function() {
      __ap_register_callback(function(){
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
  
  
         $.post('adaptive-proxy/peoplemeter_call.html?action=getPeoplemeterActivity&userId=' + __peweproxy_uid, {
              action: "getPeoplemeterActivity"
          }, function(data) {
              if (data === "TRUE") {
                  __proxy_peoplemeter_active = true
                  $("#__proxy_peoplemeter_serviceActivated").css("display", "block");
                  $("#__proxy_peoplemeter_serviceDeactivated").css("display", "none");
                  __proxy_updatePeoplemeter();
                  setInterval("__proxy_updatePeoplemeter()",8000);
              }
              else {
                  __proxy_peoplemeter_active = false;
                  $("#__proxy_peoplemeter_serviceActivated").css("display", "none");
                  $("#__proxy_peoplemeter_serviceDeactivated").css("display", "block");
              }
          });
  
          $("#__proxy_peoplemeter_deactivateButton").click(function () {
              $("#__proxy_peoplemeter_serviceActivated").css("display", "none");
              $("#__proxy_peoplemeter_serviceDeactivated").css("display", "block");
              $.post('adaptive-proxy/peoplemeter_call.html?action=setPeoplemeterActivity&activity=false&userId=' + __peweproxy_uid, {
                  action: "setPeoplemeterActivity"
              }, function(data) {
                  $("#__proxy_peoplemeter").fadeOut(80);
                  if (data == "OK") __proxy_peoplemeter_active = false;
              });
          });
  
          $("#__proxy_peoplemeter_activateButton").click(function () {
              $("#__proxy_peoplemeter_serviceActivated").css("display", "block");
              $("#__proxy_peoplemeter_serviceDeactivated").css("display", "none");
              $.post('adaptive-proxy/peoplemeter_call.html?action=setPeoplemeterActivity&activity=true&userId=' + __peweproxy_uid, {
                  action: "setPeoplemeterActivity"
              },function(data) {
                  if (data == "OK") {
                      __proxy_peoplemeter_active = true;
                      __proxy_updatePeoplemeter();
                      if (__proxy_peoplemeter_active_after_onload) {
                          setInterval("__proxy_updatePeoplemeter()",8000);
                      } else {
                          __proxy_peoplemeter_active_after_onload = false;
                      }
                  }
              });
          });
      });
    });
} (adaptiveProxyJQuery);

function __proxy_initializeTracking() {
    var temp = function($) {
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
            });

            if (__proxy_flag == true) {
                __proxy_appendHover(value, pageIndex);
                __proxy_followedPages[pageIndex] = new Array(value, __proxy_normalizeUrl($(value).attr("href")), "-1");
                pageIndex++;
            }
        });
    } (adaptiveProxyJQuery);
}

function __proxy_normalizeUrl(url) {
    var temp = function($) {
        if ((url.substr(0, 7) != "http://") && (url.substr(0, 7) != "https://")) {
            return ($(location).attr('href') + url);
        } else {
            return url;
        }
    } (adaptiveProxyJQuery);
}

function __proxy_appendHover(element, index) {
    var temp = function($) {
        var offset = $(element).offset();
        $(element).hover(function () {
                if (__proxy_followedPages[index][2] != "-1") {
                    if (offset.left > ($(document).width() - 100)) {
                        leftPosition = (offset.left - 130);
                    } else {
                        leftPosition = (offset.left + 15);
                    }
                    if (offset.top > 140) {
                        topPosition = (offset.top - 80);
                    } else {
                        topPosition = (offset.top + 35);
                    }
                    $("#__proxy_peoplemeterTop").attr("class", "");
                    $("#__proxy_peoplemeterBottom").attr("class", "");
                    if ((offset.top > 100) && (offset.left > ($(document).width() - 100))) {
                        $("#__proxy_peoplemeterTop").addClass("__proxy_top_T");
                        $("#__proxy_peoplemeterBottom").addClass("__proxy_bottom_TL");
                    }
                    if ((offset.top > 100) && (offset.left <= ($(document).width() - 100))) {
                        $("#__proxy_peoplemeterTop").addClass("__proxy_top_T");
                        $("#__proxy_peoplemeterBottom").addClass("__proxy_bottom_TR");
                    }
                    if ((offset.top < 100) && (offset.left > ($(document).width() - 100))) {
                        $("#__proxy_peoplemeterTop").addClass("__proxy_top_BL");
                        $("#__proxy_peoplemeterBottom").addClass("__proxy_bottom_B");
                    }
                    if ((offset.top < 100) && (offset.left <= ($(document).width() - 100))) {
                        $("#__proxy_peoplemeterTop").addClass("__proxy_top_BR");
                        $("#__proxy_peoplemeterBottom").addClass("__proxy_bottom_B");
                    }
                    if (__proxy_peoplemeter_active) {
                        $("#__proxy_peoplemeterBubble").css("left", leftPosition);
                        $("#__proxy_peoplemeterBubble").css("top", topPosition);
                        $("#__proxy_peoplemeterBubble").fadeIn(80);
                        $("#__proxy_counter").text(__proxy_followedPages[index][2]);
                    }
                }
            },
            function () {
                $("#__proxy_peoplemeterBubble").fadeOut(80);
            }
        )
    } (adaptiveProxyJQuery);
}

function __proxy_updatePeoplemeter() {
    var temp = function($) {
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
        });
    } (adaptiveProxyJQuery)
}
