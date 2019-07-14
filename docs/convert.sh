pandoc -o out.docx -f markdown -t docx doc.md
pandoc -s -o out.pdf doc.md -V geometry:margin=2.5cm