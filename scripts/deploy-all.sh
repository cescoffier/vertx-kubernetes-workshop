#!/usr/bin/env bash
echo "Deploying 3rd party curency service"
cd currency-3rdparty-service
mvn clean fabric8:deploy

cd ../micro-trader-dashboard
echo "Deploying the dashboard"
mvn clean fabric8:deploy

cd ../quote-generator
echo "Deploying the quote generator"
oc create configmap app-config --from-file=src/kubernetes/config.json
mvn clean fabric8:deploy -Psolution

cd ../portfolio-service
echo "Deploying the portfolio service"
mvn clean fabric8:deploy -Psolution

cd ../compulsive-traders
mvn clean fabric8:deploy -Psolution
oc scale dc compulsive-traders --replicas=2

cd ../audit-service
echo "Deploying the audit service"
oc create -f src/kubernetes/database-secret.yaml
oc new-app -e POSTGRESQL_USER=admin -ePOSTGRESQL_PASSWORD=secret -ePOSTGRESQL_DATABASE=audit registry.access.redhat.com/rhscl/postgresql-94-rhel7 --name=audit-database
mvn clean fabric8:deploy -Psolution


cd ../currency-service
echo "Deploying the currency service proxy"
mvn clean fabric8:deploy -Psolution