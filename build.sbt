name := "PostGraffiti"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.webjars" % "bootstrap" % "3.0.0",
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.projectlombok" % "lombok" % "1.14.8"  
)     

play.Project.playJavaSettings

resolvers += "webjars" at "http://webjars.github.com/m2"