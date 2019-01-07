#!/usr/bin/env bash

set -e

echo Creating diff database
${DB}-create.sh

sleep 1

echo Populating diff database with existing changelogs
/opt/liquibase/liquibase \
--driver=$DRIVER \
--classpath=/opt/liquibase/lib/mariadb-java-client.jar:/opt/liquibase/lib/mysql-connector-java.jar \
--url=jdbc:$DB://$DB_HOST:$DB_PORT/qlack_liquibase \
--username=$DB_USER \
--password=$DB_PASS \
--changeLogFile=$CHANGELOG \
update

set +e
echo Finding differences
/opt/liquibase/liquibase \
--driver=$DRIVER \
--classpath=/opt/liquibase/lib/mariadb-java-client.jar:/opt/liquibase/lib/mysql-connector-java.jar \
--url=jdbc:$DB://$DB_HOST:$DB_PORT/qlack_liquibase \
--username=$DB_USER \
--password=$DB_PASS \
--changeLogFile=$DIFFLOG \
diffChangeLog \
--referenceUrl=jdbc:$DB://$DB_HOST:$DB_PORT/$DB_SCHEMA \
--referenceUsername=$DB_USER \
--referencePassword=$DB_PASS

# Check if there are no changes and exit in that case.
CHANGESETS=$(grep -E '<changeSet' -c $DIFFLOG)
if [ $CHANGESETS -eq 0 ]; then
  echo -e "\n"
  echo "----------------------------------"
  echo "NO CHANGES FOUND !!!"
  echo "----------------------------------"
  rm $DIFFLOG
  exit 1
else
  echo "Found $CHANGESETS changes."
fi

set -e
# --changeSetAuthor seems to be ignored, so manually changing this.
echo Modifying author to: $AUTHOR
sed -i "s/author=\".*\" id/author=\"${AUTHOR}\" id/g" $DIFFLOG

# Add logicalFilePath.
FILE=$(echo $DIFFLOG | sed 's:.*/::')
echo Adding logicalFilePath: $FILE
sed -i "s|<databaseChangeLog\(.*\)>|<databaseChangeLog\1 logicalFilePath=\"${FILE}\">|" $DIFFLOG

echo Dropping diff database
${DB}-drop.sh