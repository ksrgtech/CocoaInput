#!/bin/bash
# TODO(kisaragi): replace this with Some make-like task runner; it should be run in parallel
./build_lib_for_win.sh
./build_lib_for_x11.sh
remote_build.sh
