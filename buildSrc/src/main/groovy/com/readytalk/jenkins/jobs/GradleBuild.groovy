package com.readytalk.jenkins.jobs

import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.dsl.Job

import com.google.inject.Inject

class GradleBuild extends BasicJob {

  String tasks = "ci"
  String args

  @Inject
  GradleBuild(JobManagement jm) {
    super(jm)
  }

  Set<Job> generate() {
    super.generate()

    steps {
      gradle("${tasks}", "${args} --no-daemon")
    }

    return [job]
  }


}
