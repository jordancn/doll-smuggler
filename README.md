# doll-smuggler

Maximize the total street value of drugs delivered.

In response to [Clojure Programming Challenge](http://spin.atomicobject.com/2011/05/31/use-clojure-to-move-drugs-a-programming-challenge/).

## Dependencies

  Install [Leinengen](http://leiningen.org)

## Installation

Download from (https://github.com/jordancn/doll-smuggler).

## Usage
  
    doll-smuggler$ lein run [options]

## Options

    -h, --help
    -d, --dolls INPUT        Dolls input file
    -w, --max-weight WEIGHT  Maximum weight
    
## Examples

    doll-smuggler$ lein run --dolls resources/available-dolls.txt --max-weight 400


### Tests

    doll-smuggler$ lein test

## License

Distributed under the [Eclipse Public License](LICENSE.EPL) either version 1.0 or (at
your option) any later version, with portions under [GNU Free Documentation
License](LICENSE.FDL) version 1.3 or (at your option) any later version.
