# How to release

- make a release branch
- update the hard-coded version in the driver class of the `silent-genes-cli` module
- update `pom.xml` versions using `versions:set` goal of the `versions-maven-plugin`
- create a release commit, open a release PR, ensure CI passes (no PR merging yet)
- perform the release by running the code below and by closing the Sonatype repo.
  ```shell
  cd SilentGenes
  ./mvnw -Prelease clean package
  ./mvnw -Prelease --also-make --projects silent-genes-io deploy 
  ```
- merge the PR and tag the merge commit with the version (e.g. `v0.1.2`)
- create a GitHub release for the tag, and include the CLI JAR as an asset
- merge `main` into `development and set SNAPSHOT tags
