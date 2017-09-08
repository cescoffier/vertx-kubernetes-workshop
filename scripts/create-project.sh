#!/usr/bin/env bash

RED='\033[0;31m'
NC='\033[0m' # No Color
YELLOW='\033[0;33m'
BLUE='\033[0;34m'

PROJECT=vertx-kubernetes-workshop

echo -e "${BLUE}Creating project ${PROJECT} ${NC}"
oc new-project ${PROJECT}
echo -e "${BLUE}Adding permissions to ${PROJECT} ${NC}"
oc policy add-role-to-user view admin -n ${PROJECT}
oc policy add-role-to-user view -n ${PROJECT} -z default
oc policy add-role-to-group view system:serviceaccounts -n ${PROJECT}

oc project
echo -e "${BLUE}Done ! ${NC}"