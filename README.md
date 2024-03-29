sd-util
=======
[![CI](https://github.com/ohze/sd-util/actions/workflows/sbt-devops.yml/badge.svg)](https://github.com/ohze/sd-util/actions/workflows/sbt-devops.yml)

## Changelog
we use [Semantic Versioning](http://semver.org/)

### 1.2.1 (tobe release)
+ Update sbt and use [sbt-devops](/ohze/sbt-devops) for CI
+ Update latest scala patch version (such as `2.12.10 -> 2.12.15`)
+ Add scala3 support
+ Update `scala-collection-compat:2.5.0`
+ Change `com.typesafe:config` & `commons-codec` version:
  - `config:1.3.4 & commons-codec:1.10` for scala 2.11 & 2.12 => compatible with v1.0.x
  - `config:1.4.1 & commons-codec:1.15` for scala 2.13 & 3 => compatible with v1.1.x
+ Add a new implementation of `sd.util.HmacSHA1`
+ Cherry-pick an improvement for rnd.HashSet from scala's repo
+ Backward compatible with all versions from `1.0.0` to `1.2.0` except the things mentioned above.
  - Ensure by [mima](https://github.com/lightbend/mima)

### 1.2.0
+ support scala 2.13
+ update libs:
    - com.typesafe:config 1.3.2 -> 1.4.0 - same as in akka 2.6.1 & play 2.8.0
    - common-codec 1.11 -> 1.13
    - specs2 % Test 3.9.5 -> 4.8.1
+ update sbt & sbt plugins
+ make `sd.util.RandomIterHashSet` an alias to `scala.collection.mutable.rnd.HashSet`
+ mv `rnd.HashSet.doSome` to a deprecated extension method for Iterable.
    Note:
    - break compatibility! now you need `import sd.util.DoSomeOps` before use `aHashSet.doSome[U](count: Int, f: A => U)`
    - note `doSome` is now deprecated. Pls use `.iterator.take(count).foreach(f)`
+ add test specs for `rnd.HashSet`
+ a benchmark for `.doSome(count, f)` vs `.take(count).foreach(f)` vs `.view.take(count).foreach(f)`

### 1.1.0
+ revert changes in v1.0.3: Remove sd.util.HmacSHA1
+ Exactly === v1.0.2

### 1.0.3
+ Add sd.util.HmacSHA1

### 1.0.2
+ update scala 2.12.4, 2.11.12
+ update com.typesafe:config 1.3.2, commons-codec 1.11
+ update sbt 1.0.1, sbt-coursier 1.0.0-RC13

### 1.0.1
+ update scala 2.12.3, 2.11.11
+ update sbt 1.0.1, sbt-sonatype 2.0, sbt-pgp 1.1.0, scalastyle-sbt-plugin 1.0.0
+ use sbt-coursier 1.0.0-RC11
+ move source code to github.com/ohze/sd-util
+ update typesafe config 1.3.1

# 1.0.0
+ Initial release

## Licence
This software is licensed under the Apache 2 license:
http://www.apache.org/licenses/LICENSE-2.0

Copyright 2014-2017 Sân Đình (http://sandinh.com)
