#!/usr/bin/env bash
PROJECT=$(oc project -q)
echo "Cleaning up current project: ${PROJECT}"
# https://docs.openshift.org/latest/cli_reference/basic_cli_operations.html#object-types
oc delete build --all
oc delete bc --all

oc delete dc --all
oc delete deploy --all

oc delete is --all
oc delete istag --all
oc delete isimage --all

oc delete job --all

oc delete po --all
oc delete rc --all
oc delete rs --all

oc delete secrets --all
oc delete configmap --all

oc delete services --all

oc delete routes --all

oc delete template --all

