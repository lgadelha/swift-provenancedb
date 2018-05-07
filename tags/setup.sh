# ensure that this script is being sourced

if [ ${BASH_VERSINFO[0]} -gt 2 -a "${BASH_SOURCE[0]}" = "${0}" ] ; then
  echo ERROR: script ${BASH_SOURCE[0]} must be executed as: source ${BASH_SOURCE[0]}
  exit 1
fi

# Setting scripts folder to the PATH env var.

TAGDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ _$(which tag 2>/dev/null) != _$TAGDIR/tag ]; then
  echo Adding $TAGDIR: to front of PATH
  PATH=$TAGDIR:$PATH
else
  echo Assuming $TAGDIR: is already in PATH
fi

# Setting .pgpass

if [ -e $HOME/.pgpass ]; then
  savepgpass=$(mktemp $HOME/.pgpass.XXXX)
  echo Saving $HOME/.pgpass in $savepgpass
  mv $HOME/.pgpass $savepgpass
fi

cat >>$HOME/.pgpass <<END
swift.rcc.uchicago.edu:5432:provdb:provdb:sesame
END

#----------

return

PATH=$PWD:/home/wilde/pgsql/bin:$PATH

#-----------

PATH=/home/wilde/swift/provenancedb:/home/wilde/swift/provwork:/home/wilde/pgsql/bin:$PATH

alias sql="psql -U provdb -h swift.rcc.uchicago.edu provdb -c"

