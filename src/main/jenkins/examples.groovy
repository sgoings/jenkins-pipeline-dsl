import com.readytalk.jenkins.dsl.PipelineDsl
import com.readytalk.jenkins.jobs.*
import com.readytalk.jenkins.jobs.components.*

use(PipelineDsl) {

  defaults(GradleArtifactory) {
  	url = "http://artifactory.example.com/artifactory"
  	repo = "libs-integration-local"
  }

  create(GradleBuild) {
    name = "test"
    addDownstream("ci-1")

    artifactory.repo = "libs-snapshots-local"
  }

  create(GradleBuild) {
  	name = "test2"
  }

}
