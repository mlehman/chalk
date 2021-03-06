# Chalk

Author: **Jason Baldridge** (jasonbaldridge@gmail.com)

## Introduction

Chalk is a library for natural language processing (NLP).

The name Chalk comes from my son's stuffed elephant. "Chalk" obviously has a great connection to writing (and thus text processing). Of course, it also follows the what-your-kid-called-his-elephant naming convention precedent established with Hadoop. :)

In addition to OpenNLP, there are two other related projects. One is Scalabha (https://github.com/utcompling/Scalabha), which I started as a package for teaching Scala in computational linguistics projects, and which I may pilfer as Chalk develops. The other is Breeze, which is a Scala-based machine learning and NLP project that I've been peripherally involved with. It's a great and very general library; ultimately, I expect to bring Breeze in as a dependency and then Chalk will in some respects be an example of building an NLP library on top of the core capabilities provided by Breeze.

## Using Chalk

The latest stable release is 1.0, which corresponds quite directly to Apache OpenNLP v1.5.3. 

In SBT:

    libraryDependencies += "com.jasonbaldridge" % "chalk" % "1.0"

In Maven:

    <dependency>
       <groupId>com.jasonbaldridge</groupId>
       <artifactId>chalk</artifactId>
       <version>1.0</version>
    </dependency>

## Requirements

* Version 1.6 of the Java 2 SDK (http://java.sun.com)

## Configuring your environment variables

The easiest thing to do is to set the environment variables `JAVA_HOME`
and `CHALK_DIR` to the relevant locations on your system. Set `JAVA_HOME`
to match the top level directory containing the Java installation you
want to use.

Next, add the directory `CHALK_DIR/bin` to your path. For example, you
can set the path in your `.bashrc` file as follows:

	export PATH=$PATH:$CHALK_DIR/bin

Once you have taken care of these three things, you should be able to
build and use Chalk.


## Building the system from source

Chalk uses SBT (Simple Build Tool) with a standard directory
structure.  To build Chalk, type (in the `$CHALK_DIR` directory):

	$ ./build update compile

This will compile the source files and put them in
`./target/classes`. If this is your first time running it, you will see
messages about Scala being downloaded -- this is fine and
expected. Once that is over, the Chalk code will be compiled.

To try out other build targets, do:

	$ ./build

This will drop you into the SBT interface. To see the actions that are
possible, hit the TAB key. (In general, you can do auto-completion on
any command prefix in SBT, hurrah!)

To make sure all the tests pass, do:

	$ ./build test

Documentation for SBT is at <http://www.scala-sbt.org/>

Note: if you have SBT already installed on your system, you can
also just call it directly with "sbt" in `CHALK_DIR`.


## Trying it out

Assuming you have completed all of the above steps, including running the "compile" action in SBT, you should now be able to try out some examples. There is no documentation specific to Chalk at this time, but you should be able to follow the OpenNLP documentation:

<http://opennlp.apache.org/documentation/1.5.2-incubating/manual/opennlp.html>

However, you'll need to substitute 'chalk cli' for 'opennlp' in that manual. Here's an example to do sentence detection.

        $ echo "Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29. Mr. Vinken is chairman of Elsevier N.V., the Dutch publishing group. Rudolph Agnew, 55 years old and former chairman of Consolidated Gold Fields PLC, was named a director of this British industrial conglomerate." > input.txt
 
        $ wget http://opennlp.sourceforge.net/models-1.5/en-sent.bin

        $ chalk cli SentenceDetector en-sent.bin < input.txt 
        Loading Sentence Detector model ... done (0.099s)
        Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29.
        Mr. Vinken is chairman of Elsevier N.V., the Dutch publishing group.
        Rudolph Agnew, 55 years old and former chairman of Consolidated Gold Fields PLC, was named a director of this British industrial conglomerate.



        Average: 1500.0 sent/s 
        Total: 3 sent
        Runtime: 0.0020s

Here's an example of doing sentence detection via the API by using the Scala console in SBT.

```
$ cd /tmp
$ wget http://opennlp.sourceforge.net/models-1.5/en-sent.bin
$ cd $CHALK_DIR
$ ./build
> console
scala> import java.io.FileInputStream
import java.io.FileInputStream

scala> import chalk.tools.sentdetect._
import chalk.tools.sentdetect._

scala> val sdetector = new SentenceDetectorME(new SentenceModel(new FileInputStream("/tmp/en-sent.bin")))
sdetector: chalk.tools.sentdetect.SentenceDetectorME = chalk.tools.sentdetect.SentenceDetectorME@74dd590f

scala> val sentences = sdetector.sentDetect("Here is a sentence. Here is another with Mr. Brown in it. Hurrah.")
sentences: Array[java.lang.String] = Array(Here is a sentence., Here is another with Mr. Brown in it., Hurrah.)

scala> sentences.foreach(println)
Here is a sentence.
Here is another with Mr. Brown in it.
Hurrah.
```

# Questions or suggestions?

Email Jason Baldridge: <jasonbaldridge@gmail.com>

Or, create an issue: <https://github.com/jasonbaldridge/chalk/issues>


