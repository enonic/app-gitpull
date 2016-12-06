# GitPull for Enonic XP

[![Build Status](https://travis-ci.org/enonic/app-gitpull.svg?branch=master)](https://travis-ci.org/enonic/app-gitpull)
[![codecov](https://codecov.io/gh/enonic/app-gitpull/branch/master/graph/badge.svg)](https://codecov.io/gh/enonic/app-gitpull)
[![License](https://img.shields.io/github/license/enonic/app-gitpull.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

This Enonic XP application pulls changes from multiple configured git repositories. It also exposes an endpoint that can be
triggered to pull on demand.



## Configuration

To configure this application, just place a file named `com.enonic.app.gitpull.cfg` inside one of your configuration directories. This
file holds a set of repositories to pull from. Every set is named and the keys is described as follows:

* `<name>.url`      - Git URL (only http and https is supported).
* `<name>.user`     - Git server user (optional).
* `<name>.password` - Git server password (optional).
* `<name>.dir`      - Destination directory to checkout.

Here's an example:

```
# Repo one
prod.url = https://some/git/url
prod.user = username
prod.password = password
prod.dir = ${xp.home}/config/prod

# Repo two
dev.url = https://some/git/url
dev.dir = ${xp.home}/config/dev
```

This will pull down to the destination directory every time it's maually triggered and when the app (or server) is started.
