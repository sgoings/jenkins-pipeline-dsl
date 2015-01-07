package com.readytalk.jenkins.jobs.components

import javaposse.jobdsl.dsl.Job

trait DownstreamHelpers {
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
