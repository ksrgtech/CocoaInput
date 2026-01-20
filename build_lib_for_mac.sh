#!/bin/bash
set -x
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
export GIT_ROOT="$(git rev-parse --show-toplevel)"

mkdir -p "$VERSION/src/main/resources/darwin"
cd "$(dirname "$0")/libcocoainput/darwin/libcocoainput" || die "cd failed"
make && make install
