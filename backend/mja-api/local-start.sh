#!/bin/sh
# ------------------------------------------------------------------------------------------------------
# Startet die Anwendung so, wie sie auch auf dem Server läuft: Angular-App wird durch Backend geserved
# ------------------------------------------------------------------------------------------------------
mvn  quarkus:dev -Dadmin-redirect-url.login=http://localhost:9210/mja-admin/
