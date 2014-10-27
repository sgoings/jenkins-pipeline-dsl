import com.readytalk.jenkins.dsl.*
import com.readytalk.jenkins.pipelines.*
import com.readytalk.jenkins.jobs.*
import com.readytalk.jenkins.jobs.builds.*

use(PipelineDsl) {

  template(type: GradleBuild) {
    artifactoryUrl = "http://artifactory.example.com/artifactory"
    artifactoryRepoKey = "libs-snapshots-local"
  }

  create(type: ProjectPipeline) {

    name = "example"
    repository = "http://github.com/example.git"

    stages = [ 'build', 'deploy', 'test', 'promote' ]

    add(stage: 'build', type: GradleBuild) {
      name = "${owner.name}-build"
      artifactoryUrl = "http://artifactory.example.com/artifactory"
      artifactoryRepoKey = "libs-snapshots-local"
    }

    add(stage: 'deploy', type: JobGroup) {
      name = "${owner.name}-deploy"
    }

    add(stage: 'test', type: JobGroup) {
      name = "${owner.name}-test"
    }

    add(stage: 'promote', type: JobGroup) {
      name = "${owner.name}-promote"
    }
  }

  create(type: JobGroup) {
    name = "test"
  }

  create(type: GradleBuild) {
    name = "test2"
  }
}