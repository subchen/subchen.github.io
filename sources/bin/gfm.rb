#!C:/ruby-1.9.3/bin/ruby.exe

require 'redcarpet'

require 'rouge'
require 'rouge/plugins/redcarpet'

class HTMLwithHighlight < Redcarpet::Render::HTML
    # add highlight using Rouge
    include Rouge::Plugins::Redcarpet

    # add anthor for each header,
    # see https://gist.github.com/courtenay/5454972
    def header(title, level)
        @headers ||= []
        permalink = title.downcase.gsub(/\W+/, '-')

        if @headers.include? permalink
            permalink += '_1'
            permalink = permalink.succ while @headers.include? permalink
        end
        @headers << permalink
        %(
            <h#{level}><a name="#{permalink}" class="anchor" href="##{permalink}"><span class="anchor-icon"></span></a>#{title}</h#{level}>
        )
    end
end

render_extensions = {
    :hard_wrap            => true,
}
parse_extensions = {
    :no_intra_emphasis    => true,
    :fenced_code_blocks   => true,
    :tables               => true,
    :autolink             => true,
    :underline            => true,
}

fileContents = ARGF.read
render = HTMLwithHighlight.new(render_extensions)
html = Redcarpet::Markdown.new(render, parse_extensions).render(fileContents)
STDOUT.write(html)
