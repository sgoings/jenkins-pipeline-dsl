package com.readytalk.jenkins.jobs

import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.dsl.Job

import com.readytalk.jenkins.jobs.components.JavaMetrics
import com.readytalk.jenkins.jobs.components.GradleArtifactory

class GradleBuild extends BasicJob implements JavaMetrics {

  String tasks = "ci"
  String args

  GradleArtifactory artifactory

  GradleBuild(JobManagement jm, GradleArtifactory artifactory) {
    super(jm)
    this.artifactory = artifactory.clone()
  }

  Set<Job> generate() {
    JavaMetrics.super.apply()
    artifactory.apply(job)

    steps {
      gradle("${tasks}", "${args} --no-daemon")
    }

    return [job]
  }


}
