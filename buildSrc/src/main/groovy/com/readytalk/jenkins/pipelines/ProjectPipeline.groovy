package com.readytalk.jenkins.pipelines

import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.JobManagement

import com.readytalk.jenkins.jobs.JobGroup

class ProjectPipeline {

  String name
  String repository

  Set<String> stages
  Map<String, Set<Object>> jobGroups

  JobManagement jm

  ProjectPipeline(JobManagement jm) {
    this.jm = jm
    stages = []
    jobGroups = new HashMap<String, Set<Object>>()
  }

  def add(Map<String, String> options, Closure closure) {
    def jobGroup = options["type"].newInstance(jm)

    jobGroup.with(closure)

    jobGroups[options["stage"]] = jobGroup

    return jobGroup
  }

  void applyJobDependencies() {
    for (int i = 0; i < stages.size()-1; i++) {
      jobGroups[stages[i]].each { upstreamJob ->
        jobGroups[stages[i+1]].each { downstreamJob ->
          upstreamJob.addDownstream(downstreamJob.name)
        }
      }
    }
  }

  def generate() {
    applyJobDependencies()
    return jobGroups.values().collect { it.generate() }.flatten()
  }

}