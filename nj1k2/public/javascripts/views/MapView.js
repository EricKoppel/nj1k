var MapView = Backbone.View.extend({
	el: "div#map_canvas",
	map: null,

	initialize: function() {
		if (GBrowserIsCompatible()) {
			this.constructMap();
			this.setCenterPoint();
		}
	},
	
	constructMap: function() {
		this.$el.css({'width': '800px', 'height': '600px'})
		this.map = new GMap2(this.$el[0]);
		var myTopoCopy = new GCopyright(1, new GLatLngBounds(new GLatLng(-90, -180), new GLatLng(90, 180)), 0, 'Topo maps (c) MyTopo.com');
		var copyrightCollection = new GCopyrightCollection('Mytopo.com');
		copyrightCollection.addCopyright(myTopoCopy);
		var tileLayer = new GTileLayer(copyrightCollection, 9, 15, {
			isPng: true,
			tileUrlTemplate: 'http://maps.mytopo.com/groundspeak/tilecache.py/1.0.0/topoG/{Z}/{X}/{Y}.png'
		});

		var tileLayers = [tileLayer];

		var topoMap = new GMapType(tileLayers, G_SATELLITE_MAP.getProjection(), 'MyTopo', {
			minResolution: 9,
			maxResolution: 15 });
		
		this.map.addMapType(topoMap);      
		this.map.addMapType(G_PHYSICAL_MAP);

		this.map.enableScrollWheelZoom();
		this.map.addControl(new GLargeMapControl());
		this.map.addControl(new GMapTypeControl());

		
	},
	
	setCenterPoint: function() {
		var blueIcon = new GIcon(G_DEFAULT_ICON);
		blueIcon.image = "http://www.google.com/intl/en_us/mapfiles/ms/micons/blue-dot.png";

		var centerPoint = new GLatLng(this.model.get("latitude"), this.model.get("longitude"));
		var marker = new GMarker(centerPoint, {icon: blueIcon, title: this.model.get("name")});
		this.map.setCenter(centerPoint, 11, G_PHYSICAL_MAP);
		this.map.addOverlay(marker);
	},
	
	addPoint: function(mountain) {
		var point = new GLatLng(mountain.get("latitude"), mountain.get("longitude"));
		var marker = new GMarker(point, {title: mountain.get("name")});

		GEvent.addListener(marker, 'click', function() {
			var link = document.createElement("a");
			$(link).attr("href", "../" + mountain.get("id"));
			$(link).text(mountain.get("name"));
			marker.openInfoWindow(link);
		});
		
		this.map.addOverlay(marker);
	}
});