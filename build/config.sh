#!/bin/bash
## The docker account to push the image to
REGISTRY=brunoe

# The multistage target images to build and push
IMAGES="Server JavaClient WebClient"