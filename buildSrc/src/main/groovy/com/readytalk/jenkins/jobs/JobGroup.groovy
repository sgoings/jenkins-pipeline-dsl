package com.readytalk.jenkins.jobs

import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.JobManagement

interface JobGroup {
  Set<Job> generate()
}
