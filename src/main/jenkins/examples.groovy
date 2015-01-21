import com.readytalk.jenkins.dsl.PipelineDsl
import com.readytalk.jenkins.jobs.*
import com.readytalk.jenkins.jobs.components.*

use(PipelineDsl) {

  def myArtifactory = new GradleArtifactory(url: "http://my.artifactory.com/artifactory",
                                            repo: "my-snapshot-repo")

  // configuration("some file")

  // create(Pipeline) {
  //   build(GradleBuild)

  //   deploy(AnsibleDeploy)

  //   test(PythonAutomator)
  // }

  create(GradleBuild) {
    name = "test"
    addDownstream("ci-1")

    add(new GradleArtifactory(url: 'test', repo: 'test2'))
  }

  create(GradleBuild) {
    name = "test2"

    add(myArtifactory)
  }

}
