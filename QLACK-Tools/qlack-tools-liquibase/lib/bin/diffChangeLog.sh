#!/usr/bin/env bash

echo ------------------------------------------------------
echo Creating diff database
echo ------------------------------------------------------
${DB}-create.sh

echo ------------------------------------------------------
echo Populating diff database with existing changelogs
echo ------------------------------------------------------
/opt/liquibase/liquibase \
--driver=$DRIVER \
--classpath=/opt/liquibase/lib/mariadb-java-client.jar \
--url=jdbc:$DB://$DB_HOST:$DB_PORT/qlack_liquibase \
--username=$DB_USER \
--password=$DB_PASS \
--changeLogFile=$CHANGELOG \
update

echo ------------------------------------------------------
echo Finding differences
echo ------------------------------------------------------
/opt/liquibase/liquibase \
--driver=$DRIVER \
--classpath=/opt/liquibase/lib/mariadb-java-client.jar \
--url=jdbc:$DB://$DB_HOST:$DB_PORT/qlack_liquibase \
--username=$DB_USER \
--password=$DB_PASS \
--changeLogFile=$DIFFLOG \
diffChangeLog \
--referenceUrl=jdbc:$DB://$DB_HOST:$DB_PORT/$DB_SCHEMA \
--referenceUsername=$DB_USER \
--referencePassword=$DB_PASS

echo ------------------------------------------------------
echo Dropping diff database
echo ------------------------------------------------------
${DB}-drop.sh