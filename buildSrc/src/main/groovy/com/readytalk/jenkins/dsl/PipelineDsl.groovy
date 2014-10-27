package com.readytalk.jenkins.dsl

import com.readytalk.jenkins.jobs.*
import javaposse.jobdsl.dsl.JobParent
import javaposse.jobdsl.dsl.Job

@Category(JobParent)
class PipelineDsl {

  static Map<Class, Closure> templates = new HashMap<Class, Closure>()

  void template(Map<String, String> options, Closure closure) {
    templates[options['type']] = closure
  }

  def create(Map<String, String> options, Closure closure) {
    def jobGroup = options['type'].newInstance(jm)

    if(templates[options['type']]) {
      jobGroup.with(templates[options['type']])
    }

    jobGroup.with(closure)

    referencedJobs.addAll(jobGroup.generate())

    return jobGroup
  }
}