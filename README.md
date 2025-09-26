![GitHub Workflow Status (with branch)](https://img.shields.io/github/actions/workflow/status/claudioaltamura/mockserver-test/maven-build.yml?branch=main)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# mockserver-test
Example project for integration tests with MockServer

## Restricting MockServer logging

```
    mvn test -Dmockserver.logLevel="OFF"
```