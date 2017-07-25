Contributors Coding Exercise
============================

Solution for the New Relic coding exercise, implemented as a 
standalone JAX-RS API running on an embedded Grizzly Jersey container.

 * Validation of API parameters.
 * Cache GitHub search API results
 * Retry and rate limit GitHub calls
 
Configuration
-------------

The file [configuration.properties](service/src/main/resources/contributors.properties) 
contains configuration properties for the application:

 * `github.search.url`: The GitHub search endpoint.
 * `github.search.requestsPerMinute`: Requests per minute.
 * `github.retry.maxAttempts`: Max attempts on retry.
 * `github.retry.waitDuration.millis`: Duration between retries.
 * `github.cache.expiry.seconds`: GitHub results cache expiry duration.

Requirements
------------

 * Java 8
 * Maven 3

Building
--------

With Maven 3 in your PATH, run the command:

`$ mvn clean package`

Running
-------

With Maven 3 in your PATH, run the commands:

`$ cd api`

`$ mvn exec:java`
