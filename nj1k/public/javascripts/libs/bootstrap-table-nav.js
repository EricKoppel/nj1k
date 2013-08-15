// Generated by CoffeeScript 1.6.2
(function() {
  (function($) {
    var current, endIndexForPage, getRows, numPages, onPageNav, options, paint, paintPagination, paintTableRows, startIndexForPage, tableNav,
      _this = this;

    options = {};
    current = {};
    tableNav = function(opts) {
      var _ref, _ref1, _ref2, _ref3, _ref4;

      options.tableSelector = this.selector;
      options.childSelector = (_ref = opts.childSelector) != null ? _ref : 'tr';
      options.paginationSelector = (_ref1 = opts.pagination) != null ? _ref1 : $(options.tableSelector).siblings('.pagination');
      options.itemsPerPage = (_ref2 = opts.itemsPerPage) != null ? _ref2 : 10;
      options.hideWhenOnePage = (_ref3 = opts.hideWhenOnePage) != null ? _ref3 : true;
      current.page = (_ref4 = opts.initialPage) != null ? _ref4 : 0;
      $(options.paginationSelector).on('click', 'li', onPageNav);
      $(options.paginationSelector).on('click', function(e) {
    	  e.preventDefault();
      });
      return paint();
    };
    startIndexForPage = function(page) {
      return options.itemsPerPage * page;
    };
    endIndexForPage = function(page) {
      return startIndexForPage(page + 1) - 1;
    };
    getRows = function() {
      return $(options.tableSelector).find(options.childSelector);
    };
    numPages = function() {
      return Math.ceil(getRows().length / options.itemsPerPage);
    };
    paint = function() {
      paintTableRows();
      return paintPagination();
    };
    onPageNav = function(ev) {
      current.page = parseInt($(ev.target).attr('data-page-num'));
      return paint();
    };
    paintTableRows = function() {
      var endRow, startRow, table;

      startRow = startIndexForPage(current.page);
      endRow = endIndexForPage(current.page);
      table = $(options.tableSelector);
      table.find("" + options.childSelector + ":hidden").show();
      table.find("" + options.childSelector + ":lt(" + startRow + ")").hide();
      return table.find("" + options.childSelector + ":gt(" + endRow + ")").hide();
    };
    paintPagination = function() {
      var cssClass, i, markup, num, pagination, _i;

      num = numPages();
      pagination = $(options.paginationSelector);
      if (num === 1 && options.hideWhenOnePage) {
        return pagination.hide();
      }
      markup = [];
      for (i = _i = 0; 0 <= num ? _i < num : _i > num; i = 0 <= num ? ++_i : --_i) {
        cssClass = i === current.page ? 'active' : '';
        markup.push("<li class='" + cssClass + "'><a href='#' data-page-num='" + i + "'>" + (i + 1) + "</a></li>");
      }
      var ret = pagination.show().empty().append("<ul>" + (markup.join('')) + "</ul>");
      
      return ret;
    };
    return $.extend(jQuery.fn, {
      tableNav: tableNav
    });
  })(window.jQuery);

}).call(this);

/*
//@ sourceMappingURL=bootstrap-table-nav.map
*/
