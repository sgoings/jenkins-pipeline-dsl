package com.readytalk.jenkins.jobs

import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.JobManagement

class JobGroup {
  @Delegate Job job

  JobGroup(JobManagement jm) {
    job = new Job(jm, [:])
  }

  Set<Job> generate() {
    return [job]
  }

  void addDownstream(Job job) {
    addDownstream(job.name)
  }

  void addDownstream(String jobName) {
    blockOnUpstreamProjects()
    blockOnDownstreamProjects()
    publishers {
      downstreamParameterized {
        trigger(jobName, 'SUCCESS', true) {
          currentBuild()
        }
      }
    }
  }

}
