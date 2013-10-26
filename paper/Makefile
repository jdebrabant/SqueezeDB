LATEX = pdflatex
MAINFILE = approx
UPLOADURI = ssh.cs.brown.edu:public_html/

.PHONY: all archive check clean osx pdf upload view

all : archive

$(MAINFILE).aux: *.tex $(wildcard *.sty) $(wildcard *.eps) Makefile
	$(LATEX) $(MAINFILE).tex

$(MAINFILE).blg: *.tex  *.bib
	-bibtex --min-crossrefs=20 $(MAINFILE) 

$(MAINFILE).pdf: $(MAINFILE).aux $(MAINFILE).blg
	$(LATEX) $(MAINFILE).tex
	$(LATEX) $(MAINFILE).tex

$(MAINFILE).tar.bz2: $(MAINFILE).pdf
	env COPYFILE_DISABLE=1 tar cjvfh $(MAINFILE).tar.bz2 *.tex *.bib *.pdf $(wildcard *.bst) $(wildcard *.cls) $(wildcard *.clo) $(wildcard *.eps) $(wildcard *.svg) Makefile

archive: $(MAINFILE).tar.bz2

check: *.tex
	$(LATEX) $(MAINFILE).tex | grep -s -e "multiply" -e "undefined"

clean:
	-/bin/rm -f $(MAINFILE).pdf $(MAINFILE).tar.bz2 *.dvi *.aux *.ps *~
	-/bin/rm -f *.log *.lot *.lof *.toc *.blg *.bbl *.out 

pdf: $(MAINFILE).pdf

osx: pdf
	open $(MAINFILE).pdf

upload: pdf
	scp $(MAINFILE).pdf $(UPLOADURI)

view: pdf
	acroread  -geometry 1000x1000 $(MAINFILE).pdf

