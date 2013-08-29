var MountainDistanceModelCollection = Backbone.Collection.extend({
	model: MountainDistanceModel,
	url: function() {
		return '/radius/' + this.collectionId;
	},
	initialize: function() {
		console.debug("Initializing MountainDistanceModelCollection");
	}
});