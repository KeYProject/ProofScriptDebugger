#!/bin/bash


mkdocs build --clean
rsync --delete -vr site/ i57adm.ira.uka.de:htdocs/grebing/psdbg/
