# primetab

Print a multiplication table of prime numbers using the sieve of Eratosthenes.

## Usage

Git this repo:

    % git clone git@github:MicahElliott/primetab
    % cd primetab

Build:

    % lein uberjar

There is a tiny wrapper script provided. Run with:

    % ./primetab [options]


## Options

Run `primetab --help` to see the full list of options.

The basic configurables are:
- the `n`umber of primes to print for the table
- color or `b`land output
- `raw` format to omit labels/headers for the table
- `c`sv or `t`sv delimiters


## Examples

Print a small `5x5` table, blandly without color, in CSV format.

    % ./primetab -n5 -bc


## More documentation

See the generated codox html in target/default/doc/index.html


## Contributing

There is no CI or real quality control yet. :) This code relies on
kibit, joker, etc to keep code clean.


## License

Copyright © 2019 Micah Elliott / FC
