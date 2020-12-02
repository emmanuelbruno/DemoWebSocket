#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
. ${DIR}/config.sh

## The image name by default the project directory in lowercase
IMAGE_NAME=`echo ${PWD##*/}| tr '[:upper:]' '[:lower:]'`

CURRENT=`pwd`
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd $DIR
BASE=${REGISTRY}/${IMAGE_NAME}
BRANCH=`git rev-parse --abbrev-ref HEAD|tr '/' '_' `
SHA=`git log -1 --pretty=%h`
cd $CURRENT


echo "### BUILDING with REGISTRY=$REGISTRY IMAGE_NAME=$IMAGE_NAME BASE=$BASE BRANCH=$BRANCH"