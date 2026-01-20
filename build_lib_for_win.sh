#!/bin/bash
die() {
  echo "$@" >&2
  exit 1
}

echo "Build libcocoainput for Windows"
if [ -z "$VERSION" ]; then
  die "Requires version directory"
fi

# shellcheck disable=SC2034
# shellcheck disable=SC2155
export GIT_ROOT="$(git rev-parse --show-toplevel)"

mkdir -p "$VERSION/src/main/resources/win"
cd "$(dirname "$0")/libcocoainput/win" || die "cd failed"
make && make install
