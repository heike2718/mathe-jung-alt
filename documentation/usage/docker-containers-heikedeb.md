# Docker container für die lokale Entwicklung

Folgende Docker container benötigt die lokale Entwicklung von mathe-jung-alt:

+ die Datenbank
+ clamav
+ authprovider
+ docker-latex-client

## Starten und stoppen der DB

Startscript: /home/heike/bin/start-mariadb-all.sh

```
#!/bin/bash
cd /home/heike/mariadb-all
docker-compose up -d
cd ~
```

Stoppscript /home/heike/bin/stop-mariadb-all.sh

## Starten und stoppen von authprovider und clammav

Startscript: /home/heike/bin/start-authprovider.sh

```
#!/bin/bash
cd /media/veracrypt1/ansible/docker/authprovider
docker-compose up -d
cd /media/veracrypt1/ansible/docker/docker-clamav
docker-compose up -d
cd ~
```

Stoppscript: /home/heike/bin/stop-authprovider.sh

```
#!/bin/bash
cd /home/heike/git/mathe-jung-alt/backend/docker/docker-latex-client
docker-compose up -d
```

## Starten und stoppen des docker-latex-clients

Startscript: /home/heike/bin/start-docker-latex.sh

```
#!/bin/bash
cd /home/heike/git/mathe-jung-alt/backend/docker/docker-latex-client
docker-compose up -d
cd ~
```

Stoppscript: /home/heike/bin/stop-docker-latex.sh