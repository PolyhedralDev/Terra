# Pull Request

## Description

<!-- Include a description of the PR here -->

<!--
If applicable, include 'Fixes #XXXX' or 'Closes #XXXX' for any related open issues.
This helps us relate, track, and close the relevant issues.
-->

### Changelog

<!--
Fill out a changelog below of what has been done, and what might be planned to be done. 
You may delete this if your PR is not adding new features, only fixing bugs or adding translations.
-->

- [ ] <!-- First thing -->
    - [ ] <!-- A requirement of the first thing. -->
    - [ ] <!-- A second requirement of the first thing. -->
- [ ] <!-- Second thing -->
- [ ] <!-- etc. -->

## Checklist

<!--
Select the options below that apply.
You must put an x in all the boxes that it applies to. (Like this: [x])
-->

#### Mandatory checks

- [ ] The base branch of this PR is an unreleased version branch (that has a `ver/` prefix)
  or is a branch that is intended to be merged into a version branch.
  <!--
  This is not applicable if the PR is a version branch itself.
  PRs for new versions should use the `master` branch instead.
  -->
- [ ] There are no already existing PRs that provide the same changes.
  <!-- If this is not applicable, the PR will be removed as a duplicate. -->
- [ ] The PR is within the scope of Terra (i.e. is something a configurable terrain generator should be doing).
- [ ] Changes follow the code style for this project.
  <!-- There is an included `.editorconfig` file in the base of the repo. Please use a plugin for your IDE of choice that follows those settings. -->
- [ ] I have read the [`CONTRIBUTING.md`](https://github.com/PolyhedralDev/Terra/blob/master/CONTRIBUTING.md)
  document in the root of the git repository.

#### Types of changes

- [ ] Bug Fix <!-- Changes include bug fixes. -->
- [ ] Build system <!-- Changes the build system. -->
- [ ] Documentation <!-- Changes add to or improve documentation. -->
- [ ] New Feature <!-- Changes add new functionality to Terra. -->
- [ ] Performance <!-- Changes improve the performance of Terra. -->
- [ ] Refactoring <!-- Changes do not add any new code, only moves code around. -->
- [ ] Repository <!-- Changes affect the repository. E.g. changes to the `README.md` file. -->
- [ ] Revert <!-- Changes revert previous commits. -->
- [ ] Style <!-- Changes update style, namely the .editorconfig file. -->
- [ ] Tests <!-- Changes add or update tests. -->
- [ ] Translation <!-- Changes include translations to other languages for Terra. -->

#### Compatibility

<!-- The following options determine if the PR pertains to a major, minor, or patch version bump -->

- [ ] Introduces a breaking change
  <!--
  Breaking changes are any fix or feature that breaks some previous functionality to Terra / is not backwards compatible.
  Breaking changes do not include:
    - changes that are backwards compatible and will work with *any* previously existing supported features.
    - changes to code marked as in a pre-release
      state (annotated with @Incubating, @Preview, @Experimental
      or in a package called something similar to the previous annotations)
  -->
- [ ] Introduces new functionality in a backwards compatible way.
- [ ] Introduces bug fixes

#### Documentation

- [ ] My change requires a change to the documentation.
- [ ] I have updated the documentation accordingly.

#### Testing

- [ ] I have added tests to cover my changes.
- [ ] All new and existing tests passed.
  <!--
  Tests are typically not necessary for small changes.
  Including *some* testing is recommended for large changes where applicable.
  -->

#### Licensing

<!-- In order to be accepted, your changes must be under the GPLv3 license. Please check one of the following: -->

- [ ] I am the original author of this code, and I am willing to
  release it under [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html).
- [ ] I am not the original author of this code, but it is in public domain or
  released under [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html) or a compatible license.
  <!--
  Please provide reliable evidence of this.
  NOTE: for compatible licenses, you must make sure to add the included license somewhere in the program, if so required.
  (And even if it's not required, it's still nice to do it. Also add attribution somewhere.)
  -->
