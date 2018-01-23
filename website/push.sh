#!/bin/bash


mkdocs build --clean
rsync --delete -vr site/ grebing@i57adm.ira.uka.de:.public_html/psdbg/
