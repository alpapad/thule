#!/bin/bash

function toLowerCase {
    if [[ ((${BASH_VERSION%%.*} -ge 4)) ]]; then
        echo "${1,,}"   # Bash version >= 4.0
    else
        echo "print '$1'.lower()" | python
    fi
}
