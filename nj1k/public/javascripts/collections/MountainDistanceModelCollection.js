var MountainDistanceModelCollection = Backbone.Collection.extend({
	model: MountainDistanceModel,
	url: function() {
		return 'http://localhost:9000/radius/' + this.collectionId;
	},
	initialize: function() {
		console.debug("Initializing MountainDistanceModelCollection");
	}
});