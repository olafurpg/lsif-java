#!/usr/bin/env sh
set -eux
COURSIER="$(dirname "$0")/coursier"
SOURCES=$($COURSIER fetch --classifier sources junit:junit:4.13.2)
mkdir sources
for source in $SOURCES; do
    unzip -d "sources/$(basename "$source")" "$source"
done
