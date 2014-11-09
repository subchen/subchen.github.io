/******************************************
 *  jetbrick.widget.scrolltop.js
 ******************************************/
(function (window, $) {

    /**
     * 单击此处，返回到顶部
     */
    var scrolltop = function() {
        var $dom = $('<img src="/assets/images/scrolltop.png" />');
        $dom.css({
            display: 'none',
            cursor: 'pointer',
            position: 'fixed',
            right: '60px',
            bottom: '80px',
            border: '0 none',
            margin: 0,
            padding: 0,
            width: 'auto',
            height: 'auto'
        });
        $('body').append($dom);

        $dom.click(function() {
            $('body,html').animate({scrollTop:0}, 400);
            return false;
        });

        var hidden = true;
        $(window).on('scroll resize', function() {
            if ($(window).scrollTop() > 40) {
                if (hidden) {
                    $dom.fadeIn();
                    hidden = false;
                }
            } else {
                if (!hidden) {
                    $dom.fadeOut();
                    hidden = true;
                }
            }
        });
    };

    $(function() {
        scrolltop();
    });

})(typeof window !== "undefined" ? window : this, jQuery);


$(function() {
    $("pre > code.highlight").each(function() {
        var $copy = '<div class="zero-clipboard"><span class="btn-clipboard">Copy</span></div>';
        $(this).parent().before($copy);
    });
    
    var clip = new ZeroClipboard($(".btn-clipboard"));
    var $bridge = $("#global-zeroclipboard-html-bridge");
 
    clip.on("ready", function(e) {
        $bridge.data("placement", "left").attr("title", "Copy to clipboard").tooltip();
    });
 
    clip.on("copy", function(e) {
        var c = $(e.target).parent().next();
        clip.setText(c.text());
    });
 
    clip.on("aftercopy", function(e) {
        $bridge.attr("title", "Copied!").tooltip("fixTitle").tooltip("show").attr("title", "Copy to clipboard").tooltip("fixTitle");
    });
 
    clip.on("error", function(e) {
        $bridge.attr("title", e.message).tooltip("fixTitle").tooltip("show");
    });
});

