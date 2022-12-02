## IJServer

Server inside running headless intellij to process PSI analysis tasks.

### Modules:
[ij-server](ij-server) - contains implementation of simple watching server which runs.\
[ij-starter](ij-starter) - module where headless intellij application starters are declared.\
[ij-core](ij-core) - modules with analysis related files like analyzers interfaces.


### Pipeline:
1. Headless intellij application starts.
To start application run following script passing as `-PwatchDir` directory where server will watch for tasks:
```shell 
./gradlew :ij-starter:runIde -PwatchDir=path/to/watch/directory
```
2. Server starts inside application and watch for tasks which create to `watchDir`.
3. Each task is a specially configured `.json` file with task description. 
For example:
```json
{
    "inputType": "FILE",
    "inputPath": "{PATH_TO_PROJECT_DIR}/IJServer/ij-server/src/main/resources/data/input",
    "outputPath": "{PATH_TO_PROJECT_DIR}/IJServer/ij-server/src/main/resources/data/output/output.txt",
    "jarPath": "{PATH_TO_PROJECT_DIR}/IJServer/ij-server/src/main/resources/data/jar",
    "analyzerClassName": "org.jetbrains.research.ij.server.analysis.CallExpressionsAnalyzer"
}
```
where: 
- `inputType` -- can be `FILE` (if file analysis task) or `PROJECT` (if project analysis task)
- `inputPath` -- directory with code files/projects to analyse
- `outputPath` -- directory with analysis result
- `jarPath` -- path to jar with analyzer class implementation
- `analyzerClassName` -- analyzer class name to invoke on code
4. When new task comes server process in following steps:
    * If task is **batch of files** analysis:
      1. Load analyzer class from given jar file
      2. Create dummy project 
      3. For each file create PSI file with given content inside
      4. Invoke analyzer on file and get serializable result
      5. Save result to file
