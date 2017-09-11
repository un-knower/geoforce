#! /bin/bash

##########################################
# zip: 1.shutdown 2.backup&clean 3.unzip 4.chmod 5.startup
##########################################
echo "#####################################"
echo "dealing service..."
# shutdown
basedir=$PWD
for provider in $(ls ./service)
do
	cd $basedir/service/$provider
	./shutdown.sh
done
echo "shutdown OK."

# backup&clean
cd $basedir
backupDir=$(date '+%Y%m%d%H%M%S')
mkdir -p ./backup/$backupDir
mv ./service/* ./backup/$backupDir
echo "backup&clean OK."

# unzip
for zip in $(ls ./*.zip)
do
	unzip -q $zip -d ./service
done
echo "unzip OK."

# chmod
chmod 755 ./service/* -R
echo "chmod OK."

# startup
for provider in $(ls ./service)
do
	cd $basedir/service/$provider
	./startup.sh
done
echo "startup OK."

##########################################
# war: 1.shutdown 2.backup 3.clean 4.cp 5.startup
##########################################
echo "#####################################"
echo "dealing web..."
# shutdown
cd $basedir/web/apache-tomcat-7.0.70/bin
./shutdown.sh
echo "shutdown OK."

# backup
mv ../webapps/*.war $basedir/backup/$backupDir
echo "backup OK."

# clean
rm -rf ../webapps/*
rm -rf ../work/*
echo "clean OK."

# cp
cp $basedir/*.war ../webapps
echo "cp OK."

# startup
./startup.sh
echo "startup OK."

##########################################
# clean upload
##########################################
echo "#####################################"
echo "cleaning uploads..."
cd $basedir
rm -rf ./*.zip
rm -rf ./*.war
rm -rf ./deploy.sh
echo "clean upload OK."

##########################################
echo "#####################################"
echo "deploy OK."
echo "#####################################"