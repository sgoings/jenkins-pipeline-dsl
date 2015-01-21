package com.readytalk.jenkins.jobs

import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.JobManagement

import com.google.inject.Inject

import com.readytalk.jenkins.jobs.components.DownstreamHelpers
import com.readytalk.jenkins.jobs.components.JobComponent

class BasicJob implements JobGroup, DownstreamHelpers {

  @Delegate Job job
  Set<JobComponent> components = new ArrayList<JobComponent>()

  @Inject
  BasicJob(JobManagement jm) {
    job = new Job(jm)
  }

  JobComponent add(JobComponent component) {
    JobComponent c = component.clone()
    components << c
    return c
  }

  JobComponent add(JobComponent component, Closure closure) {
  	def c = add(component)
  	c.with(closure)
  	return c
  }

  void component(Class name, Closure closure) {
    components.find { c -> c.class.name == this.class.name }.with(closure)
  }

  Set<Job> generate() {
    components.each { it.apply(job) }

    return [job]
  }

}
