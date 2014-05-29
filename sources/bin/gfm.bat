@echo off

set PATH=C:/Windows/System32;%PATH%
set PATH=C:/dev/ruby-1.9.3/bin;%PATH%

chcp 65001

::cd /d %~dp0

type header.txt > %2
ruby.exe "%~dpn0.rb" %1 >> %2
type footer.txt >> %2


::chcp 936

::pause &
