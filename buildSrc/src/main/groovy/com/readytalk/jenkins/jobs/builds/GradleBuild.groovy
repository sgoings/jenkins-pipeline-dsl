package com.readytalk.jenkins.jobs.builds

import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.JobManagement

import com.readytalk.jenkins.jobs.JobGroup

class GradleBuild {
  @Delegate JobGroup jobGroup

  String artifactoryUrl
  String artifactoryRepoKey
  boolean metrics = true

  GradleBuild(JobManagement jm) {
    jobGroup = new JobGroup(jm)
  }

  void artifactory() {
    assert artifactoryUrl, artifactoryRepoKey

    configure { project ->
      project / buildWrappers / 'org.jfrog.hudson.gradle.ArtifactoryGradleConfigurator'(plugin: 'artifactory@2.2.1') {
        deployBuildInfo(true)
        deployArtifacts(true)
        evenIfUnstable(true)
        deployMaven(false)
        deployIvy(true)
        includeEnvVars(false)
        allowPromotionOfNonStagedBuilds(true)
        ivyPattern("[organisation]/[module]/[revision]/ivy-[revision].xml")
        artifactPattern("[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]")

        'details' {
          artifactoryUrl(artifactoryUrl)
          artifactoryName(artifactoryUrl)
          repositoryKey(artifactoryRepoKey)
          snapshotsRepositoryKey(artifactoryRepoKey)
          downloadRepositoryKey('repo')
          stagingPlugin {
            pluginName("None")
          }
        }

        'releaseWrapper' {
          tagPrefix("")
          releaseBranchPrefix("release_")
          releasePropsKeys("version")
          nextIntegPropsKeys("version")
        }
      }
    }
  }

  void gradle() {
    steps {
      gradle("ci", "--no-daemon")
    }
  }

  void metrics() {
    if(metrics) {
      publishers {
        checkstyle('**/reports/checkstyle/main.xml')
        findbugs('**/reports/findbugs/main.xml', true)
        warnings(['Java Compiler (javac)'])
        archiveJunit('**/test-results/**/*.xml')
        jacocoCodeCoverage {
          execPattern "**/**.exec"
          classPattern "**/classes/main"
        }
      }
    }
  }

  Set<Job> generate() {
    gradle()
    metrics()
    artifactory()
    return jobGroup.generate()
  }
}