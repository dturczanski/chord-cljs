# Chord-cljs

A web app implemented in Clojurescript that allows to get a chord name based on pushed piano keys.

Live demo : http://chord-cljs.herokuapp.com/

App uses [Overtone](https://github.com/overtone/overtone/) chord recognition algorithm. Clever div css copied from [javascript-piano](https://github.com/mrcoles/javascript-piano) and adjusted.

## Running

Run:
lein ring server

This should open a web browser with the application. You can also go to live demo listed above.


## Using

Click on the piano keys and the chord name shows up. Chord names might not be the simplest possible (chord recognition taken from the Overtone project).

## License

Copyright © 2013

Distributed under the Eclipse Public License, the same as Clojure.
