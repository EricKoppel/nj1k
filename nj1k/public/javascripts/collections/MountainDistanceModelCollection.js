var MountainDistanceModelCollection = Backbone.Collection.extend({
	model: MountainDistanceModel,
	url: function() {
		return '/mountains/' + this.collectionId + '/neighbors';
	}
});