#!/bin/bash
die() {
  echo "$@" >&2
  exit 1
}

echo "Build libcocoainput for macOS"
if [ -z "$VERSION" ]; then
  die "Requires version directory"
fi

# shellcheck disable=SC2034
# shellcheck disable=SC2155
export GIT_ROOT="$("$(dirname "$0")/gitw" rev-parse --show-toplevel)"
if [ -z "$GIT_ROOT" ]; then
  die "Requires git to be installed"
fi

echo "Minecraft version: $VERSION"
echo "Git repository root: $GIT_ROOT"

mkdir -p "$VERSION/src/main/resources/darwin"
cd "$(dirname "$0")/libcocoainput/darwin/libcocoainput" || die "cd failed"
make && make install
