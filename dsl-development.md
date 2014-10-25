ReadyTalk Jenkins DSL Development
---------------------------------

Vision
======
The goal as a devtools team with this DSL is to provide a simple, elegant way of
configuring pipelines. The simplicity of this DSL through conventions should
have cascading simplification and standardization effects on everyone.

Adherence
=========
What I'd like to make sure remains true about this project is:

- Maintain strict division between "our dsl" and the jenkins-job-dsl. Maybe part
  of the problem is wording. We don't have a specific name for "our dsl", and it
  goes by: pipeline dsl, job dsl, etc. - No one knows what it means

- Encourage simplicity by convention over configuration (if my job looks complex
  in the DSL, it makes me feel like I'm doing something wrong)

These two points are especially important for dsl development. I don't want to
see us building escape hatches, but enabling people the ability to work around
our dsl conventions to still get at individual jenkins job configurations.

The way that I've seen this work best so far is by stepping away from thinking
about Jenkins jobs at our DSL level and thinking about our DSL more in terms of
"I have a project and I want CI/CD/CT." So the simplest way to get what I want
should look something like:

```
gradle {
  name "voip_client"
  group Team.Core
}
```

This supplies enough information to show:
the project "voip_client" is:
- a gradle project adhering to all our gradle project conventions
- a git repo named voip_client, living in the Core repo group
- a project owned by the Core team (determines who gets notified when it's
  broken)

As we expand the model of a project from just enabling CI to CD and CT I
envision a change looking like:

```
pipeline {
  name "voip_client"
}
```

Where we can infer all the information specified above automatically by
inspecting the voip_client project itself. Then, if people want to configure
certain parts of the pipeline, they grab the specific job they want to configure
and drop down into the jenkins-job-dsl.

So let's say we want to change the code metrics support a bit on the build job.
We do:

```
pipeline {
  name "voip_client"

  build {
    publishers {
      cobertura('build/reports/coverage/**/cobertura*.xml')
      archiveJunit('build/reports/junit/*.xml')
    }
  }
}
```

This dsl above means that we're manually overriding the publishers portion of
the voip_client build job (which I propose should be doing pull request jobs as
well as master builds). Having a separate pull request job makes our job really
messy all because we don't have Gradle looking at its context more (I'm building
master, so I should publish a snapshot, vs. I'm building a pr, so I should do
something different when publishing... or not publish at all).

Errata
======

### Complications with underlying Jenkins Job DSL

The best way I've seen (practically) to deal with quirks of the Jenkins Job DSL
is to simply avoid encouraging layman use of the jenkins-job-dsl through our
abstracted dsl. Let's leave the development of the jenkins-job-dsl up to the
maintainers, and coordinate with them when we have problems with the underlying
jenkins job dsl functionality. The simpler interface we expose to "everyone" the
less work we will have in maintaining our dsl in the long run. The more features
we expose, the more complex our dsl becomes.

I've heard complaints that the jenkins-job-dsl is hard to work with and has
varying behavior per dsl block. This is a sign that maybe we're trying to solve
problems at the wrong level. The DSL should be the most idealistic part of our
system, and drive simplicity and convention as a result. If people think that
our dsl is too opinionated, they can always drop into the regular dsl and deal
with the quirks themselves. :-)
