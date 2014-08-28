requirejs.config({
    paths: {
        jquery: '/assets/javascripts/libs/jquery-1.11.1.min',
        backbone: 'https://cdnjs.cloudflare.com/ajax/libs/backbone.js/1.1.2/backbone-min',
        underscore: 'https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.3.3/underscore-min',
        holder: 'https://cdnjs.cloudflare.com/ajax/libs/holder/2.3.1/holder.min',
        jqueryui: 'https://code.jquery.com/ui/1.10.4/jquery-ui',
        jcrop: 'http://jcrop-cdn.tapmodo.com/v0.9.12/js/jquery.Jcrop.min',
        bootstrap: 'https://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min',
        collapse: 'https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.1.1/js/collapse.min',
        dropdown: 'https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.1.1/js/dropdown.min',
        modal: 'https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.1.1/js/modal.min',
        transition: 'https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.1.1/js/transition.min',
        fancybox: 'https://cdnjs.cloudflare.com/ajax/libs/fancybox/2.1.5/jquery.fancybox.pack',
        chart: '/assets/javascripts/libs/chart',
        metadata: '/assets/javascripts/libs/jquery.metadata',
        tablesorter: '/assets/javascripts/libs/jquery.tablesorter'
    },
    shim: {
    	'bootstrap': {
            deps: ['jquery']
        },
	    'collapse': {
	        deps: ['jquery']
	    },
	    'dropdown': {
            deps: ['jquery']
        },
        'transition': {
            deps: ['jquery']
        },
        'modal': {
            deps: ['jquery']
        },
        'chart': {
            deps: ['jquery']
        },
        'fancybox': {
            deps: ['jquery']
        },
        'jcrop': {
            deps: ['jquery']
        }
    }
});