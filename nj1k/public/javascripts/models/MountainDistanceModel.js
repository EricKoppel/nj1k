var MountainDistanceModel = Backbone.Model.extend({
	toString: function() {
		return "Distance between " + this.get("m1").get("name") + " and " + this.get("m2").get("name") + " is " + this.get("distanceAsMiles"); 
	},
	parse: function(response) {
		var attrs = {};
		attrs.distanceAsMiles = response.distanceAsMiles;
		attrs.m1 = new Mountain({'id': response.m1.id, 'name': response.m1.name});
		attrs.m2 = new Mountain({'id': response.m1.id, 'name': response.m2.name, 'latitude': response.m2.latitude, 'longitude': response.m2.longitude});
		return attrs;
	}
});