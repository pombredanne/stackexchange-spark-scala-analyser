name := "stackexchange-spark-scala-analyser"

version := "0.1"

scalaVersion := "2.11.12"

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.3"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.3"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.3"

libraryDependencies += "org.apache.spark" %% "spark-hive" % "2.4.3"

libraryDependencies += "org.apache.spark" %% "spark-graphx" % "2.4.3"

libraryDependencies += "com.databricks" %% "spark-xml" % "0.4.1"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "com.github.scopt" %% "scopt" % "3.7.1"


// needed to make examples run at least on Linux
javaOptions in run += "-XX:MaxPermSize=256M"

scalacOptions += "-target:jvm-1.8"
parallelExecution in Test := false//just for local, not for cluster

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}