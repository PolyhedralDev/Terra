# Pull Request

## Brief description.

<!-- Please provide a brief description of the goals of your PR -->

<!--
###########################################################################
## WARNING!                                                              ##
## IGNORING THE FOLLOWING TEMPLATE WILL RESULT IN YOUR PR BEING CLOSED   ##
###########################################################################
-->
<!--
  Please go through this checklist item by item and make sure you have successfully completed each of these steps.
    - Your pull request MUST be either on the latest version of Terra, or on a branch for a future release.
    - Make sure that there are no already existing PRs that fix this. If so, it will be closed as a duplicate.
    - Make sure that this change is actually within the scope of Terra and is something a terrain generator should be doing.
    - Make sure that this is not an issue with a specific Terra *pack*, and instead applies to all of Terra.
    - Make sure that you have filled out all the required information and given descriptions of everything.
-->
<!-- You can erase any parts of this template not applicable to your Pull Request. -->

### What Issues Does This Fix?

<!--
    Put Fix #XXXX or Closes #XXXX here if there are any open issues that this PR fixes.
    This is to automatically close the relevant issues.
    You may remove this if there is no issue for this PR.
    But unless this is a very small change, you should make an issue for it.
-->

## Licensing

<!-- In order to be accepted, your changes must be under the GPLv3 license. Please check one of the following: -->

- [ ] I am the original author of this code, and I am willing to release it
  under [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html).
- [ ] I am not the original author of this code, but it is in public domain or
  released under [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html) or a
  compatible license.
    <!--
      Please provide reliable evidence of this.
      NOTE: for compatible licenses, you must make sure to add the included license somewhere in the program, if so required.
      (And even if it's not required, it's still nice to do it. Also add attribution somewhere.)
    -->

## Goal of the PR

<!--
    What is the goal of the PR?
    Put a checklist here of what has been done
    (and what *hasn't*, but you plan to do),
    so we can easily know what was changed.
    Note: this is only required for PRs that add new features.
    If your PR is not adding new features, only fixing bugs or adding translations, then you may delete this section.
-->

- [ ] <!-- First thing -->
    - [ ] <!-- A requirement of the first thing. -->
    - [ ] <!-- A second requirement of the first thing. -->
- [ ] <!-- Second thing -->
- [ ] <!-- etc. -->

## Affects of the PR

<!---
    What types of changes does your code introduce? (Select any that apply. You may select multiple.)
    You must put an x in all the boxes that it applies to. (Like this: [x])
-->

#### Types of changes

- [ ] Bug Fix <!-- Anything which fixes an issue in Terra. -->
- [ ] Build system <!-- Anything which pretain to the build system. -->
- [ ] Documentation <!-- Anything which adds or improves documentation for existing features. -->
- [ ] New Feature <!-- Anything which adds new functionality to Terra. -->
- [ ] Performance <!-- Anything which is imrpoves the performance of Terra. -->
- [ ] Refactoring <!-- Anything which does not add any new code, only moves code around. -->
- [ ] Repository <!-- Anything which affects the repository. Eg. changes to the `README.md` file. -->
- [ ] Revert <!-- Anything which reverts previous commits. -->
- [ ] Style <!-- Anything which updates style. -->
- [ ] Tests <!-- Anything which adds or updates tests. -->
- [ ] Translation <!-- Anything which is internationalizing the Terra program to other languages. -->

#### Compatiblity

- [ ] Breaking
  change <!-- A fix, or a feature, that breaks some previous functionality to Terra. -->
- [ ] Non-Breaking change.
    <!--
        A change which does not break *any* previous functionality of Terra.
        (ie. is backwards compatible and will work with *any* previously existing supported features.
        Note: if a feature is annotated with @Incubating, @Preview, @Experimental,
        or is in a package called something similar to the previous annotations,
        then you may push breaking changes to only THOSE parts of Terra.)
    -->

#### Contribution Guidelines.

- [ ] I have read
  the [`CONTRIBUTING.md`](https://github.com/PolyhedralDev/Terra/blob/master/CONTRIBUTING.md)
  document in the root of the git repository.
- [ ] My code follows the code style for this
  project. <!-- There is an included `.editorconfig` file in the base of the repo. Please use a plugin for your IDE of choice that follows those settings. -->

#### Documentation

- [ ] My change requires a change to the documentation.
- [ ] I have updated the documentation accordingly.

#### Testing

- [ ] I have added tests to cover my changes.
- [ ] All new and existing tests passed.
    <!--
        If it only introduces small changes, you don't need to add tests.
        But if you add big changes, you should probably at least write *some* testing, where applicable.
    -->
