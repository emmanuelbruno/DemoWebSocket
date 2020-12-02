#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
. ${DIR}/env.sh

for image in $IMAGES; do
imageLower=`echo ${image}|tr '[:upper:]' '[:lower:]'`
DOCKER_BUILDKIT=1 docker build \
  --target $image \
	-t ${BASE}.${imageLower}:$SHA \
	-t ${BASE}.${imageLower}:$BRANCH \
	`[[ "$BRANCH" == "master" ]] && -t ${BASE}:latest` \
	. 2>&1
done
exit 0


