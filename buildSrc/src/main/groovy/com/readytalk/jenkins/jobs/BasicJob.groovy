package com.readytalk.jenkins.jobs

import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.JobManagement

import com.readytalk.jenkins.jobs.components.DownstreamHelpers

class BasicJob implements JobGroup, DownstreamHelpers {

  @Delegate Job job

  BasicJob(JobManagement jm) {
    job = new Job(jm)
  }

  Set<Job> generate() {
    return [job]
  }

}
