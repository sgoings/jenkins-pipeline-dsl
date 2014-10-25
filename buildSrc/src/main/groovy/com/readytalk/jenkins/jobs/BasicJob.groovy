package com.readytalk.jenkins.jobs

import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.JobManagement
import com.readytalk.jenkins.dsl.*

class BasicJob extends Job {

  String environmentName

  BasicJob(JobManagement jm, Map<String, String> arguments = [:]) {
    super(jm, arguments)

    description("""
This job is managed by the PipelineDsl project.
Any changes to the job config (including enabling/disabling the job) will be overwritten.
""")

    wrappers {
      colorizeOutput('xterm')
    }
  }

  /*void apply(ExtensionHandler extensions) {

  }*/

  void setEnvironmentName(String envName) {
    environmentName = envName
  }

  Set<Job> generate() {
    dslConfigs.apply()
    return [this]
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
