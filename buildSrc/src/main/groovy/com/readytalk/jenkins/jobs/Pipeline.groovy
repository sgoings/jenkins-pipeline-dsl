package com.readytalk.jenkins.jobs

import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.JobManagement

class Pipeline {

  String environmentName

  Map<String, BasicJob> jobs

  //ExtensionHandler extensions

  boolean triggerClosureOpened = false

  JobManagement jm

  Pipeline(JobManagement jm, Map<String, String> arguments = [:]) {
    this.jm = jm
    jobs = new HashMap<String, BasicJob>()
    jobs['trigger'] = new BasicJob(jm, arguments)
    //jobs['deploy'] = new DeployJob(jm, arguments)
  }

  /*void apply(ExtensionHandler otherExtensions) {
    extensions = new ExtensionHandler(otherExtensions)

    jobs['deploy'].apply(extensions)
  }*/

  void setEnvironmentName(String environmentName) {
    this.environmentName = environmentName

    jobs['trigger'].name = "${environmentName}-pipeline"
    jobs['deploy'].environmentName = "${environmentName}"
  }

  Set<Job> generate() {
    assert environmentName

    def genJobs = []

    jobs['trigger'].addDownstream(jobs['deploy'])

    if(!triggerClosureOpened) {
      trigger {
        triggers {
          cron("H ${triggerHour()} * * *")
        }
      }
    }

    jobs.findAll { name, job ->
      job instanceof PostDeployTest
    }.each { name, job ->
      jobs['deploy'].addDownstream(job)
    }

    jobs.each { name, job ->
      genJobs.addAll(job.generate())
    }

    return genJobs
  }

  BasicJob add(Map<String, String> options, Closure closure) {
    if(!jobs[options['name']]) {
      jobs[options['name']] = options['type'].newInstance(jm)
      jobs[options['name']].setEnvironmentName(environmentName)
    }

    jobs[options['name']].with(closure)

    return jobs[options['name']]
  }

  void trigger(Closure closure) {
    triggerClosureOpened = true
    jobs['trigger'].with(closure)
  }

  void deploy(Closure closure) {
    jobs['deploy'].with(closure)
  }

  // Allows auto pipeline trigger setting if no trigger set
  private int triggerHour() {
    int startHour = 20
    int hour
    int distribution = environmentName.inject(0,{ s, c -> s += c.toCharacter() }) % 8

    if(startHour + distribution >= 24) {
      hour = distribution - (24 - startHour)
    } else {
      hour = startHour + distribution
    }

    return hour
  }

}
