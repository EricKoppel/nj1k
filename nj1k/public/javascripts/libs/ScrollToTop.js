(function($) {
	$.fn.scrollify = function() {
		if ($(window).width() < 768) {
			return;
		}
		
		this.css('padding', '12px');
		this.css('background-color', 'rgb(66, 139, 202)');
		this.find('span').css('color', "#fff");
		
		var that = this;
		
		$(window).scroll(function() {
			if ($(this).scrollTop() > 200) {
				$(that).fadeIn();
			} else {
				$(that).fadeOut();
			}
		});

		$(that).click(function() {
			$('html, body').animate({
				scrollTop : 0
			}, 800);
			return false;
		});
	};
}(jQuery));