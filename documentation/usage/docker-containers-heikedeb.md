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

Startscript: ~/bin/start-docker-latex.sh

```
#!/bin/bash
cd ~/git/mathe-jung-alt/backend/docker/docker-latex-client
docker-compose up -d
cd ~
```

Stoppscript: ~/bin/stop-docker-latex.sh

# Docker container für die Tests

Es gibt ein eigenes Test-DB-Image  __heik2718/mja-test-db__. Sourcen liegen hier:

```
/media/veracrypt1/ansible/docker/mja-test-db
```

Dort liegt das aktuelle dump für die Tests, das vor den globalen Tests eingespielt werden muss: mittels root-connect....sh connecten und dann den dump einspielen:

```
source dumps/quarkus-test__mathe_jung_alt_complete_dump.sql
```

Startscript: ~/bin/start-mja-test-db.sh
Stopscript: ~/bin/start-mja-test-db.sh


