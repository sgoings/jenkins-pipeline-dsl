package com.readytalk.jenkins.jobs.components

import groovy.transform.AutoClone

import javaposse.jobdsl.dsl.Job

@AutoClone
class GradleArtifactory {

  String url
  String repo

  void apply(Job job) {
    assert url, repo

    job.configure { project ->
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
          artifactoryUrl(url)
          artifactoryName(url)
          repositoryKey(repo)
          snapshotsRepositoryKey(repo)
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
}
