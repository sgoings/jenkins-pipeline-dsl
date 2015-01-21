package com.readytalk.jenkins.jobs.components

import javaposse.jobdsl.dsl.Job

interface JobComponent {
  void apply(Job)
}