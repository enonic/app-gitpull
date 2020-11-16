# GitPull for Enonic XP

[![Actions Status](https://github.com/enonic/app-gitpull/workflows/Gradle%20Build/badge.svg)](https://github.com/enonic/app-gitpull/actions)
[![codecov](https://codecov.io/gh/enonic/app-gitpull/branch/master/graph/badge.svg)](https://codecov.io/gh/enonic/app-gitpull)
[![License](https://img.shields.io/github/license/enonic/app-gitpull.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

This Enonic XP application pulls changes from multiple configured git repositories. It also exposes an endpoint that can be
triggered to pull on demand.


## Configuration

To configure this application, just place a file named `com.enonic.app.gitpull.cfg` inside one of your configuration directories. This
file holds a set of repositories to pull from. Every set is named and the keys are described as follows:

* `<name>.url`      - Git URL (http, https, ssh).
* `<name>.dir`      - Destination directory to checkout.
* `<name>.timeout`  - Connection timeout in seconds, by default 60 seconds.
* `<name>.ref`      - Can be specified as ref name (`refs/heads/main`) or branch name (`main`). The default is to use the branch pointed to by the cloned or pulled repository's HEAD.

### HTTPS-Authentication 
* `<name>.user`     - Git server user (optional).
* `<name>.password` - Git server password (optional).

### SSH-Authentication
* `<name>.keyPath`  - SSH private key path
* `<name>.strictHostKeyChecking`  - Boolean, allow ssh to hosts without specifying hosts in hosts-file.

Here's an example:

```
# Repo one
prod.url = https://some/git/url
prod.user = username
prod.password = password
prod.dir = ${xp.home}/config/prod
prod.timeout = 60
prod.ref = refs/heads/master

# Repo two
gitProd.url = ssh://git@github.com:agituser/agitrepo.git 
gitProd.dir = ${xp.home}/config/gitProd
gitProd.timeout = 60
gitProd.ref = develop
gitProd.keyPath = ${xp.home}/config/ssh/id_rsa
gitProd.strictHostKeyChecking = false
```

## Using SSH-Keys

The jgit is a bit particular on the format of the key, so use the ```-m PEM```-switch to generate keys if having trouble with private key not accepted, e.g

````
ssh-keygen -t rsa -m PEM
````

To allow ssh to a repo, add host to ```~/.ssh/known_hosts```, or optionally specify the ```strictHostKeyChecking=false``` option on a connection 


## Usage

Every time the app is triggered, all configured repos will be pulled.  There are three ways to trigger the app:
* Start the server
* Start the app - using the Applications app (after stopping it first), or by (re-)deploying it.
* Using the REST endpoint (method = POST), `/api/ext/gitpull`.  The request must be authenticated.
