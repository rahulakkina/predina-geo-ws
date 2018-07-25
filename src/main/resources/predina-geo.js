'use strict';
var mapDiv = document.getElementById('predina_map');
var om = null;
var map = null;
var infowindow = null;
var markers;

function Setup() {
    try {
        acme.maps3.Initialize();
        acme.maps3.maptypes.Initialize();
        om = new acme.overlaymessage.OverlayMessage(mapDiv);
        map = new google.maps.Map(mapDiv, acme.maps3.mapOptions);
        acme.maps3.zoom5.Add(map, acme.maps3.mapOptions);
        acme.maps3.FixStreetView(map);
        acme.maps3.maptypes.AddMapTypes(map, acme.maps3.mapOptions);
        acme.maps3.typecontrol.Add(map, acme.maps3.mapOptions);
        acme.maps3.DoubleClickZoom(map);
        acme.maps3.ScrollWheelZoom(map);
        map.setCenter(new google.maps.LatLng(37.7, -122.4));
        map.setZoom(9);
        acme.maps3.GetPositionZoomTypeCookie(map);
        acme.maps3.SavePositionZoomTypeCookieOnChanges(map);
        om.Set('Loading...');
        acme.utils.HttpGet('http://localhost:8080/find/60.9/-9.0/49.9/10.0', RequestChecker);
    } catch (e) {
        acme.utils.Log('Setup: ' + e.name + ' - ' + e.message + ', ' + acme.utils.Props(e));
    }
}

/*
 * marker creater function (acts as a closure for html parameter)
 */
function createMarker(options, html) {
    var marker = new google.maps.Marker(options);
    /*bounds.extend(options.position);
    if (html) {
        google.maps.event.addListener(marker, "click", function() {
            infoWindow.setContent(html);
            infoWindow.open(options.map, this);
            map.setZoom(map.getZoom() + 1)
            map.setCenter(marker.getPosition());
        });
    }*/
    return marker;
}

/*
 * getColor
 */
function getColor(c) {
    if (c == 'G')
        return "green";
    else if (c == 'Y')
        return "yellow";
    else if (c == 'O')
        return "orange";
    else if (c == 'R')
        return "red";
    else
        return "#8b0000";
}

function RequestChecker(request) {
    try {
		
		var obj = JSON.parse(xhr.responseText);
        var len = obj.s;
		var places = obj.d;
		markers = [];
		
		for (var i = 0; i < len; i++) {
			var point = places[i];
			var mark = new Object();
			mark.lat = point[0];
            mark.lng = point[1];
            mark.location = new google.maps.LatLng(point[0], point[1]);
			mark.col = getColor(point[2]);
			mark.name = point[0]+", "+point[1];
			markers.push(mark);
		}
		
		for (var i = 0; i < len; i++) {
			     var mark = markers[i];
				 var cMarker = createMarker({
				       position: new google.maps.LatLng(point[0], point[1]),
					   map: map,
					   icon: {
                          path: google.maps.SymbolPath.CIRCLE,
                          fillColor: mark.col,
                          fillOpacity: .4,
                          scale: 4,
                          strokeColor: mark.col,
                          strokeWeight: 2
                       }},'');
			google.maps.event.addListener(cMarker, 'click', acme.utils.Partial(PopUp, i));		   
		    markers.push(cMarker);
        }  
        om.Clear();
    } catch (e) {
        acme.utils.Log('RequestChecker: ' + e.name + ' - ' + e.message + ', ' + acme.utils.Props(e));
    }
}

function PopUp(s) {
    try {
        if (infowindow)
            infowindow.close();
        var mark = markers[s];
        var marker = mark.marker;
        var mhtml = '<table><tbody><tr><td><b>' + mark.name + '</b></td></tr></tbody></table>';
        infowindow = new google.maps.InfoWindow({
            content: mhtml,
            maxWidth: 400
        });
        infowindow.open(map, marker);
        acme.maps3.InfoWindowStayVisibleOnZoom(map, marker, infowindow);
    } catch (e) {
        acme.utils.Log('PopUp: ' + e.name + ' - ' + e.message + ', ' + acme.utils.Props(e));
    }
}