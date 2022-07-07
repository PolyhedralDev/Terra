# Contributing to Terra

First off, thank you for considering contributing to Terra. It's people like you
that make Terra such a great tool.

Following these guidelines helps to effectively use the time of the developers
managing and developing this open source project, making it more enjoyable for
all of us.

Terra is an open source project and we love to receive contributions from our
community, you! There are many ways to contribute, from writing tutorials or
blog posts, improving the documentation, submitting bug reports and feature
requests or writing code which can be incorporated into Terra.

The following is a set of guidelines for contributing to Terra and its packages,
which are hosted in
the [PolyhedralDev Organization](https://github.com/PolyhedralDev) on GitHub.
These are mostly guidelines, not rules. Use your best judgment, and feel free to
propose changes to this document in a pull request.

#### Table Of Contents

[Code of Conduct](#code-of-conduct)

[I don't want to read this whole thing, I just have a question!!!](#i-dont-want-to-read-this-whole-thing-i-just-have-a-question)

[Getting Started](#getting-started)

- [Your First Contribution](#your-first-contribution)
- [Reporting Bugs](#reporting-bugs)
    - [Before Submitting A Bug Report](#before-submitting-a-bug-report)
    - [How Do I Submit A (Good) Bug Report?](#how-do-i-submit-a-good-bug-report)
- [Suggesting Enhancements](#suggesting-enhancements)
    - [Before Submitting An Enhancement Suggestion](#before-submitting-an-enhancement-suggestion)
    - [How Do I Submit A (Good) Enhancement Suggestion?](#how-do-i-submit-a-good-enhancement-suggestion)
- [Pull Requests](#pull-requests)
    - [Before Submitting A Pull Request](#before-submitting-a-pull-request)
    - [How Do I Submit A (Good) Pull Request?](#how-do-i-submit-a-good-pull-request)

[Styleguides](#styleguides)

- [Git Commits](#git-commits)
    - [Committing](#committing)
    - [Git Commit Messages](#git-commit-messages)
- [Code Styleguide](#code-styleguide)
- [Documentation Styleguide](#documentation-styleguide)
    - TODO

[Coding Pratices](#coding-practices)

- [Compatibility](#compatibility)
    - [General Compatibility](#general-compatibility)
    - [Specific Compatibility](#specific-compatibility)
- [Platform-Agnostic Design](#platform-agnostic-design)
- [Data-Driven](#data-driven)

## Code of Conduct

This project and everyone participating in it is governed by
the [Terra of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected
to uphold this code. Please report unacceptable behavior
to [Terra global moderation team](CODE_OF_CONDUCT.md#Reporting).

## I don't want to read this whole thing I just have a question!!!

> **Note:** Please don't file an issue to ask a question. You'll get faster
> results by using the resources below.

We have an official discord server where you can request help from various users

- [The official PolyhedralDev discord server](https://discord.dfsek.com)

## Getting Started

### Your First Contribution

Unsure where to begin contributing to Terra? You can start by looking through "
beginner" and "help wanted" issues:

- [Beginner issues](https://github.com/PolyhedralDev/Terra/labels/Note%3A%20Good%20First%20Issue)
    - issues which should be friendly to anyone new to terra.
- [Help wanted issues](https://github.com/PolyhedralDev/Terra/labels/Note%3A%20Help%20Wanted)
    - issues which should be a bit more involved than "beginner" issues.

New to github? Working on your first Pull Request? Check
out [How to Contribute to an Open Source Project on GitHub](https://app.egghead.io/playlists/how-to-contribute-to-an-open-source-project-on-github)
to get you up on your feet.

At this point, you're ready to make your changes! Feel free to ask for help;
everyone is a beginner at first!

If a maintainer asks you to "rebase" your PR, they're saying that a lot of code
has changed, and that you need to update your branch so it's easier to merge.

### Reporting Bugs

This section guides you through submitting a bug report for Terra. Following
these guidelines helps maintainers and the community understand your report, and
spend their time fixing the issue instead of understanding what you mean.

Before creating bug reports, please
check [this list](#before-submitting-a-bug-report) as you might find out that
you don't need to create one. When you are creating a bug report,
please [include as many details as possible](#how-do-i-submit-a-good-bug-report)
.

> **Note:** If you find a **Closed** issue that seems like it is the same thing
> that you're experiencing, open a new issue and include a link to the original
> issue in the body of your new one.

#### Before Submitting A Bug Report

- Join the [discord server](https://discord.dfsek.com) to help resolve simple
  issues.
- You must be on the LATEST version of Terra to receive any support. There is no
  support for older versions of Terra.
- Make sure that this is not a *specific* compatibility issue with another
  terrain generation mod. Do not request *specific* compatibility with mods or
  plugins (e.g. "Compatibility with TechCraft v7"). That should be implemented
  in an addon, **not** in the platform project.
  *General* compatibility (e.g. "Ability to pull Vanilla/Modded features from
  parent biomes") will be considered in the platform project.
- Search for
  any [already existing issues](https://github.com/PolyhedralDev/Terra/issues?q=is%3Aissue+)
  open with your problem. If you open a duplicate, it will be closed as such.
- Make sure that it is actually Terra causing the issue, and not another
  mod/plugin. You can do this by testing to see if you can recreate the issue
  without Terra installed.
- Double check that this is not an issue with a specific Terra *pack* or Terra *
  addon*, and instead applies to all of Terra.
- Include a copy of the latest.log file. Putting *just* the exception is not
  enough. We need to be able to check that there wasn't anything else before
  that caused it.
- Be sure to fill out all the required information and give descriptions of
  everything.

#### How Do I Submit A (Good) Bug Report?

Bugs are tracked as [GitHub issues](https://guides.github.com/features/issues/)
. [Create an issue](https://github.com/PolyhedralDev/Terra/issues/new) and
provide the prerequisite information by filling in the Bug Report template.

Explain the problem and include additional details to help maintainers reproduce
the problem:

- **Use a clear and descriptive title** for the issue to identify the problem.
- **Describe the exact steps which reproduce the problem** in as many details as
  possible. When listing steps, **don't just say what you did, but explain how
  you did it**.
- **Provide specific examples to demonstrate the steps**.
- **Describe the behavior you observed after following the steps** and point out
  what exactly is the problem with that behavior.
- **Explain which behavior you expected to see instead and why.**
- **If the problem wasn't triggered by a specific action**, describe what you
  were doing before the problem happened and share more information using the
  guidelines below.

Include details about your configuration and environment:

- **Which version of Terra are you using?** You can get the exact version by
  running `/te version`.
- **What's the name and version of the platform you're using**? (eg. Spigot,
  Fabric, Paper, etc.)
- **Which external plugins or mods do you have installed?**
- **Which Terra packs do you have installed?** You can get that list by
  running `/te packs`.
- **Which Terra addons do you have installed?** You can get that list by
  running `/te addons`.

### Suggesting Enhancements

This section guides you through submitting an enhancement suggestion for Terra,
including completely new features and minor improvements to existing
functionality. Following these guidelines helps maintainers and the community
understand your suggestion and find related suggestions.

Before creating enhancement suggestions, please
check [this list](#before-submitting-an-enhancement-suggestion) as you might
find out that you don't need to create one. When you are creating an enhancement
suggestion,
please [include as many details as possible](#how-do-i-submit-a-good-enhancement-suggestion)
.

#### Before Submitting An Enhancement Suggestion

- You must be on the **LATEST** version of Terra to make sure your feature
  hasn't been added yet.
- Search for
  any [already existing issues](https://github.com/PolyhedralDev/Terra/issues?q=is%3Aissue+) (
  Including closed!) with your problem. If you open a duplicate, it will be
  closed as such.
- Verify that this is actually within the scope of Terra.
- Be sure that this is not a feature request that should be made for a specific
  Terra *pack*, and instead applies to all of Terra.
- Be sure that this is not something that should be implemented as a Terra
  addon, and instead applies to all of Terra.
- Make sure that you attach a copy of the latest.log file, if there are any
  exceptions thrown in the console. Putting *just* the exception
  **is not enough**. We need to be able to check that there wasn't anything else
  before that caused it.

#### How Do I Submit A (Good) Enhancement Suggestion?

Enhancement suggestions are tracked
as [GitHub issues](https://guides.github.com/features/issues/). Create an issue
on our platform repository and provide the following information:

- **Use a clear and descriptive title** for the issue to identify the
  suggestion.
- **Provide a step-by-step description of the suggested enhancement** in as many
  details as possible.
- **Provide specific examples to demonstrate the steps**.
- **Describe the current behavior** and **explain which behavior you expected to
  see instead** and why.
- **Explain why this enhancement would be useful** to most Terra users and isn't
  something that can or should be implemented as an addon.

### Pull Requests

This section guides you through submitting a pull request for Terra.

While the prerequisites above must be satisfied prior to having your pull
request reviewed, the reviewer(s) may ask you to complete additional design
work, tests, or other changes before your pull request can be ultimately
accepted.

#### Before Submitting A Pull Request

- You must be on the **LATEST** version of Terra to make sure your feature
  hasn't been added yet.
- Search for
  any [already existing issues](https://github.com/PolyhedralDev/Terra/issues?q=is%3Aissue+) (
  Including closed!) with your problem. If you open a duplicate, it will be
  closed as such.
- Verify that this is actually within the scope of Terra.
- Be sure that this is not a feature request that should be made for a specific
  Terra *pack*, and instead applies to all of Terra.
- Be sure that this is not something that should be implemented as a Terra
  addon, and instead applies to all of Terra.
- Make sure that you attach a copy of the latest.log file, if there are any
  exceptions thrown in the console. Putting *just* the exception **is not
  enough**. We need to be able to check that there wasn't anything else before
  that caused it.

#### How Do I Submit A (Good) Pull Request?

Pull Requests are tracked
as [GitHub Pull Requests](https://guides.github.com/activities/forking/#making-a-pull-request)
. Create a pr on our platform repository and provide the following information:

- **Use a clear and descriptive title** to identify the pull request.
- **State what this pull request adds/fixes**.
- **Be sure that you are the owner of the code you contributed** or that it can
  be licensed under the GPLv3.
- **Provide a description goals and non-goals of the pull request** in as many
  details as possible.
- **Describe the current behavior** and **explain which behavior you expected to
  see instead** and why.
- **Explain why this enhancement would be useful** to most Terra users and isn't
  something that can or should be implemented as an addon.

## Styleguides

### Git Commits

Following this is not mandatory, but rather a set of guidelines. As long as your
commit messages aren't absolutely awful, it's probably fine. But it would be
nice if you followed them.

#### Committing

When you commit code, try to avoid committing large amounts of code in a single
go. Splitting up code into smaller commits is much nicer and makes it easier to
trace a feature to a single commit.

Try to stick to one feature/fix/etc. per commit. A good rule of thumb is if you
need to use the word "and" in the subject line, then it should probably™ be two
commits.

#### Git Commit Messages

- Subject line must fit the following format: `<type>: <short summary>`. Type
  must be one of the following:
    - Build: Changes that affect the build system or external dependencies.
    - Docs: Documentation only changes.
    - Feat: A new feature.
    - Fix: A bug fix.
    - Perf: Performance improvements.
    - Refactor: Refactoring sections of the codebase.
    - Repo: Changes to the repository structure that do not affect code. (Eg.
      modification of the `README.md` file, etc.)
    - Revert: Revert a previous commit.
    - Style: Code style updates.
    - Test: Anything related to testing.
    - Trans: Translation and localization of Terra to other languages.
    - WIP: Work in progress.
- Separate the subject line from the body with a single blank line.
- Do not end subject line with a period.
- Limit the subject line to 50 or less.
- The subject line and all body lines should be in sentence case.
- Use the present tense. ("Add feature" not "Added feature")
- Use the imperative mood. ("Move cursor to..." not "Moves cursor to...")
- Reference relevant issues and pull requests in the body.

>
> Here is a template you can follow:
> ```
> Capitalized, short (50 chars or less) summary
>
> More detailed explanatory text, if necessary. Wrap it to about 72
> characters or so. In some contexts, the first line is treated as
> the subject of the commit and the rest of the text as the body. The
> blank line separating the summary from the body is critical (unless
> you omit the body entirely); various tools like `log`, `shortlog` and
> `rebase` can get confused if you run the two together.
>
> Explain the problem that this commit is solving. Focus on why you are
> making this change as opposed to how (the code explains that). Are
> there side effects or other unintuitive consequences of this
> change? Here's the place to explain them.
>
>
> Further paragraphs come after blank lines.
> - Bullet points are okay, too
> - Typically a hyphen or asterisk is used for the bullet, followed
>   by a single space, with blank lines in between, but conventions vary
>   here
> - Use a hanging indent
>
> Reference any relevant issues at the bottom, like so:
>
> Resolves: #123
> See also: #456, #789
> ```

### Code Styleguide

Use an IDE with support for `.editorconfig` files. There is an included
editorconfig file in the base of the project so that your IDE should
automatically use the correct code style settings.

### Documentation Styleguide

TODO

## Coding Practices

### Compatibility

#### General Compatibility

General compatibility (example: injection of Vanilla structures/features/carvers
into packs) is acceptable in the platform project.

- General compatibility features should be *disabled by default*. Having things
  auto-injected causes unpredictable behaviour that is annoying to diagnose.
  General-compatibility options should have config values attached which are
  disabled by default.
- These config options should also be *simple to use*. Think of the people who
  will be using these compatibility options. They want to flick a switch and
  have things be compatible. That means that a majority of compatibility options
  should stay in `pack.yml`, to make it simple to go into a pack and turn on
  specific compatibilities. This does *not* mean that more advanced
  compatibility options are off the table, for example, look at Feature
  compatibility, where features can either be automatically injected, *or*
  configured individually per Terra biome, depending on how much control the
  user wants.

#### Specific Compatibility

Specific compatibility should *not* be put in the platform project. (Example:
Adding the ability to generate TechCraft v7's doo-dads with a TerraScript
function)

Having specific compatibilities leads to tons of extra dependencies to keep
track of, as well as adding lots of additional stuff to maintain. It quickly
becomes a mess. Especially when most users will never need to use this feature.

We have designed an addon API for exactly this purpose. **Specific
compatibilities are welcome and encouraged, in the form of addons.**

### Platform-Agnostic Design

Terra must, at all times, remain platform agnostic. This means it must be able
to run on theoretically any voxel based platform. Including non-minecraft games
like Terasology.

When adding a new feature to `common`, make no assumptions about what platform
it'll be running on.

Examples:

- Don't assume the world height is 256.
- Don't assume that a specific block, item, or entity exists. (Eg. don't assume
  there exists a block called `minecraft:grass_block`)

### Data-Driven

When adding a new feature, make it abstract. Don't make assumptions about "
specific use cases." If you can only think of a few use cases, your idea should
probably be generalized.

You must use configs effectively. Make configs that are *powerful* but also *
make sense* and are \[easy\] to use.