#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
echo $DIR
. ${DIR}/env.sh

for image in $IMAGES; do
  imageLower=`echo ${image}|tr '[:upper:]' '[:lower:]'`
  docker push ${BASE}.${imageLower}:`git log -1 --pretty=%h`
  docker push ${BASE}.${imageLower}:`git rev-parse --abbrev-ref HEAD`
  [[ "$BRANCH" == "master" ]] && docker push "${BASE}.${imageLower}:latest"
done