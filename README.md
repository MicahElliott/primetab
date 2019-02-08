# primetab

[![CircleCI](https://circleci.com/gh/MicahElliott/primetab/tree/master.svg?style=svg)](https://circleci.com/gh/MicahElliott/primetab/tree/master)
[![Maintainability](https://api.codeclimate.com/v1/badges/cc8be0237070cc2d38d9/maintainability)](https://codeclimate.com/github/MicahElliott/primetab/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/cc8be0237070cc2d38d9/test_coverage)](https://codeclimate.com/github/MicahElliott/primetab/test_coverage)

Print a multiplication table of prime numbers using the [Sieve of
Eratosthenes](https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes)
(see a nice visual demo there).

## Usage

Git this repo:

    % git clone git@github.com:MicahElliott/primetab
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

Generate HTML docs:

    % lein codox

See the generated codox HTML in _target/default/doc/index.html_

    % $BROWSER target/default/doc/index.html


## Development

There is no CI or real quality control yet. :) This code relies on
kibit, joker, etc to keep code clean.

### Testing

You can run the suite of unit tests with:

    % lein test

To also run integration tests, run:

    % lein test :all

In order to test "large" primes, you may need to set JVM options
(i.e., initial memory allocation pool), like:

    % JVM_OPTS='-Xss1g' lein test :all

I see ~500 MB being allocated to run the integration tests.

During development, it's handy to run unit tests constantly upon
changes:

    % lein auto test

### Code quality

There are configured setups for ensuring code quality:

- [CircleCI](https://circleci.com/gh/MicahElliott/primetab/)
- [CodeClimate](https://codeclimate.com/github/MicahElliott/primetab)

## TODO

- Expand on tests for boundaries, long-running, overflows, etc
- Maybe add some specs
- Make use of `prime-matrix` instead of ugly `tabulate`
- Rearrange and clarify prime funcs that are not in use/experimental
- Add reloaded repl workflow via Component


## License

Copyright © 2019 Micah Elliott / FC
