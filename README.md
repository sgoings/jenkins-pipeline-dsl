Jenkins Job DSL
===============

Purpose
=======

Configuring Jenkins jobs is painful. Pain points include

- Single Point of Maintenance: having hundreds of jobs with similar configurations, and wanting to make a change (configure a new plugin) on all of them.
- Problem of Overchoice: When one goes to add a new job/configure an existing job, they are faced with lots of options, where quirks of plugins and options are not well known except by a limited few (devtools team).

This jenkins-jobs repository holds Jenkins job configurations in Groovy form, leveraging:
- an [open source project](https://github.com/jenkinsci/job-dsl-plugin)
- an [open source Gradle plugin](https://github.com/ghale/gradle-jenkins-plugin)

We wrap some of our own conventions on top of these open source projects enabling us to easily control complex deployment pipelines within this repository.

Before diving into this repository, it might be helpful to play around in the [open source Jenkins job playground](http://job-dsl.herokuapp.com/)

Normal Workflow
===============

1. Change code
2. Test code locally

```
./gradlew build
```

3. Commit + Push to remote
4. Jenkins seed job triggered on remote repo change
5. Jenkins seed job adds/updates all jobs defined in DSL

Deploying Individual Jobs
=========================

- you have to modify the *username* and *password* in the build.gradle file... but then you can do:

```
./gradlew update -PjenkinsJobFilter="<yourfilter>"
```

DSL Help
========

- Any file within src/main/jenkins will be used to generate Jenkins jobs.
- More DSL help available from: [Jenkins Job DSL wiki](https://github.com/jenkinsci/job-dsl-plugin/wiki)

Examples
========

Simple Job
----------

```
job {
  name "test"
}
```

Simplest Pipeline
--------------------------
```
pipeline {
  environmentName = "myEnvironment"
}
```

This is the start of a very extensible pipeline. These lines create 2 related jobs:

#### myEnvironment-pipeline
this can be configured to run on a cron-job-like schedule to kick off the nightly deploy. This enables you to prevent a nightly deployment/reset if you're using the environment for a couple of days and don't want it to change out from under you.

- *NOTE:* if not configured, a dedicated pipline trigger time (nightly between 8PM and 4AM) will be chosen for you

#### myEnvironment-deploy
this job is essentially a wrapper around deploy, offering you the ability to run any/all deploy actions on the target environment

Pipeline Configurability
------------------------

You can add as much complexity onto the basic pipeline. If you add a job (e.g. smokeTest { ... } it will get generated and placed in the proper position in the pipeline

```
pipeline {
  environmentName = "myEnvironment"

  # you can override any of the "all job" defaults for all the environment related jobs here
  defaults { ... }

  # configure trigger parameters/options
  trigger { ... }

  # configure deploy parameters/options
  deploy { ... }

  # FUNCTIONS BELOW ARE CREATED IN JENKINS IF CONFIGURED IN DSL

  Possible Post Deployment Tests
  -------------------------------------------------------------

  # configure smoke test job to run after deployment
  smokeTest {
    # REQUIRED
    suite = "..."
  }

  Possible Other Tests
  -------------------------------------------------------------

  # configure regression test job to run whenever you please
  regressionTest {
    # REQUIRED
    suite = "..."
  }

  Add Any Job You Like
  -------------------------------------------------------------
  add(name: <a key to identify job internally>, type: ClassnameForJob) {
    ...
  }
}
```
