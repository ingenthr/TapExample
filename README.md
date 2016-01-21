# Couchbase TAP Example

This example shows how TAP (the protocol for replication from Membase 1.6 to Couchbase 2.5) can be used to fetch all
items from a given cluster.  Note that the Java 1.4 client can't distinguish out of the box between master and replica,
so this client does it in a heavy handed sort of way by checking the key against the vbucket.

As a result, using this probably isn't recommended.  This code is quite old and a better replacement is the DCP client
in the [Couchbase JVM core](http://search.maven.org/#artifactdetails%7Ccom.couchbase.client%7Ccore-io%7C1.2.3%7Cjar).
Other alternatives are `cbtransfer` or other integration tools such as Apache Spark.

Sometimes, this is the only way to get the data.  Use this only in the case that you haven't another option.