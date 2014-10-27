

// A ProjectPipeline is intended to serve as an abstraction of what a project needs to do to get into a releaseable state.

// At ReadyTalk, we consider a pipeline as a collection of quality gates or "stages":

// - BuildStage (ensures the artifacts can be created and are of decent quality)
// -- builds and runs unit tests all binary artifacts (jars, wars, exes, docker images)
// -- runs integ tests (spin up this service with mocked out dependent services, and exercise)
// -- publishes all binary artifacts to an internally public place for consumption (Artifactory)

// - DeployStage (ensures the artifacts can be deployed and run in a real environment)
// -- takes the published binary artifacts from the previous stage and deploys them to an environment

// - TestStage (only lets through high quality artifacts)
// -- executes system/acceptance/ui tests on the project while it's up and running in the environment deployed to previously

// - PromoteStage (elevates the project's artifacts to a releaseable status)
// -- operations involved in releasing the binary artifacts, this group of binaries have been vetted for release to production


import com.readytalk.jenkins.dsl.*

use(PipelineDsl) {

  create(type: ProjectPipeline) {

    name = "example"
    repository = "http://github.com/example.git"

    stages = [ 'build', 'deploy', 'test', 'promote' ]

    add(stage: 'build', type: JobGroup) {
      name = "example-build"
      // define <version>
      // ./gradlew CI
    }

    // add(stage: 'deploy', type: DeployJob) {
    //   // deploy <version>
    // }

    // add(stage: 'test', type: PythonTestJob) {
    //   // test on environment
    // }

    // add(stage: 'promote', type: ArtifactoryPromotion) {
    //   // promote <version> to staging repo
    // }

  }

}

// As a DevTools team at ReadyTalk, we like to make things really simple to use, so we've added more conventions to enable the following


// create(type: ReadyTalkProjectPipeline) {
//   name = "mediaservice"
//   team = Team.DATT
// }
