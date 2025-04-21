#!/bin/bash
cmd=$1
command=upgrade
if [ -n "$cmd" ] ; then
  command=$cmd
fi
helm $command httpbin ./k8s
