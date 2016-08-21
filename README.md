# Build-Templater a.k.a. Freemarker preprocessor
Do you want pre-process Freemarker templates at Gradle compile time?
I prepared a primitive tool, based on several tutorials. This tool join Freemarker framework to your build process.
I bring a WatchTask, a task, which monitor templates files and compile it, when change is detected.

# When can be it useful?
The first purpose of this tool was to process FTL templates at compile time.
So for example, in a Web project, you need to organize HTML code. Easiest way is to organize them in templates.
Later you need process templates to gain final HTML code. Usually you process them at runtime.
Runtime processing take some resources. To boost performance people start thinking about caching.
But there exist simpler solution, the compile time preprocessor.

# Dependecies
- Freemarker 2.3.23
- Guava 19.0

# Installation instructions
1. Copy [webResources.gradle](webResources.gradle) to your build.gradle
2. Apply Gradle script to your build process. Put this code to end of build.gradle.
```
apply from: "webResources.gradle"
processResources.dependsOn monitorWebResources
```
3. Configure source dirs at [webResources.gradle](webResources.gradle) file

# How front-end developer can use it?
1. Start monitoring of files
`gradlew monitorTemplates -t`
2. Edit FTL templates
3. Final files are copied to outputDir

# How can you add this step to your build process
Simple put next command to your build.gradle:
`processResources.dependsOn monitorWebResources`
Preprocessor will start at process-resources phase.

# Can i use another Template framework?
Sure, just replace the Freemarker.
Some [template frameworks](https://android-arsenal.com/tag/99) focused on Android.


# Info
- Inspired by [article](http://techbeats.deluan.com/processing-large-templates-with-gradle-and-freemarker/).
- More details about [Gradle continuous build](http://jruby-gradle.org/news/2015/09/01/gradle-spotlight-continuous-build/).
- About custom tasks and how to watch changes. [More](https://docs.gradle.org/current/userguide/custom_tasks.html#N144D1).
- Nice article why dont use [JSP](https://blog.stackhunter.com/2014/01/17/10-reasons-to-replace-your-jsps-with-freemarker-templates/#Template_Loaders).
- Nice similar [tool](https://github.com/rogern/fmpp).
