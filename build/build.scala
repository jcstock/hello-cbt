package hello_cbt_build

import cbt._

class Build(val context: Context) extends SbtLayoutMain with GenerateBuildInfo with Scalastyle {

  // scalatest dependency needs to be added first
  // run tests with cbt test.run
  outer =>
    /* FIXME: calling `cbt rt` for `examples/scalatest-example` leads to
       java.lang.Exception: This should never happend. Could not find (org.scala-lang,scala-reflect) in
       (org.scala-lang,scala-library) -> BoundMavenDependency(1488121318000,cbt/cache/maven,MavenDependency(org.scala-lang,scala-library,2.11.8,Classifier(None)),Vector(https://repo1.maven.org/maven2))
       at cbt.Stage1Lib$$anonfun$actual$1.apply(Stage1Lib.scala:425)
    */
    override def test: Dependency = {
      new BasicBuild(context) with ScalaTest with SbtLayoutTest{
        override def dependencies = outer +: super.dependencies
      }
    }

  def artifactId = "org.johnstocker.cbt-fun"
  override def name = "cbt-fun"
  def version = "0.1"
  def groupId = "org.johnstocker"

  override def defaultScalaVersion = "2.12.3"

  override def buildInfo = super.buildInfo.copy(
    s"""
    def artifactId   = "$artifactId"
    def groupId      = "$groupId"
    def version      = "$version"
    def scalaVersion = "$scalaVersion"
    """
  )

  override def dependencies = (
    super.dependencies ++ // don't forget super.dependencies here for scala-library, etc.
    Seq(
      // source dependency
      // DirectoryDependency( projectDirectory ++ "/subProject" )
    ) ++
    // pick resolvers explicitly for individual dependencies (and their transitive dependencies)
    Resolver( mavenCentral, sonatypeReleases ).bind(
      MavenDependency("org.scala-lang", "scala-compiler", scalaVersion),
      ScalaDependency("com.github.scopt", "scopt", "3.7.0")
    )
  )
}
