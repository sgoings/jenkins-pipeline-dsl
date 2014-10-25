package com.readytalk.jenkins.dsl

import com.readytalk.jenkins.jobs.*
import javaposse.jobdsl.dsl.JobParent
import javaposse.jobdsl.dsl.Job

@Category(JobParent)
class PipelineDsl {
  Job create(Map<String, String> options, Closure closure) {
    def job = options['type'].newInstance(jm)

    //job.apply(extensions)

    job.dsl(closure)

    referencedJobs.addAll(job.generate())

    return job
  }
}