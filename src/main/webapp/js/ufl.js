var map;

var markers = [];

$(function() {
        $("#autocomplete").autocomplete({
                delay: 200,
                minLength: 0,
                source: function(request, response) {
                        $.getJSON("http://ec2-52-27-43-77.us-west-2.compute.amazonaws.com:8080/UberFilmLocations/ufl/search/filmName/autocomplete", {
                                filmName: $("#autocomplete").val()
                        }, function(result) {
                                if (result.success) {
                                	response(result.data.data);
                                }                                
                        });
                },
                focus: function(event, ui) {
                        // prevent autocomplete from updating the textbox
                        event.preventDefault();
                },
                select: function(event, ui) {
                        // prevent autocomplete from updating the textbox
                        event.preventDefault();
                        $("#autocomplete").val(ui.item.label);
                        refreshMap();
                }
        });
        
        $("#autocomplete").change(function() {
        	clearMap();
        });
});

function clearMap() {
	if (markers.length > 0) {
		for (var i = 0; i < markers.length; i++) {
			markers[i].setMap(null);
		}
	}
	
	markers = [];
}

function refreshMap() {
	$.ajax({
		url : 'http://ec2-52-27-43-77.us-west-2.compute.amazonaws.com:8080/UberFilmLocations/ufl/search/filmName?filmName=' + $("#autocomplete").val(),
	}).done(function(result) {
		
		alert(result);
		if (result.success) {
			var locations = result.data.data;
			
			clearMap();
			
			for (var i = 0; i < locations.length; i++) {
				var entity = locations[i];
				var myLatlng = new google.maps.LatLng(entity.location.lat, entity.location.lon);
				var marker = new google.maps.Marker({
				    position: myLatlng,
				    title: location.locations,
				    label: location.locations
				});

				// To add the marker to the map, call setMap();
				marker.setMap(map);
				
				markers.push(marker);
			}
		}
		
	}).fail(function() {
		
		alert("Unable to fetch data. Please try again after some time.");
		
	})
}