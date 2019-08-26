#!/bin/bash

function toLowerCase {
    if [[ ((${BASH_VERSION%%.*} -ge 4)) ]]; then
        echo "${@,,}"   # Bash version >= 4.0
    else
        echo "print '$@'.lower()" | python
    fi
}
