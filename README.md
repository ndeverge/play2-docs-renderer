A Play2 documentation renderer
=====================================

A quick and dirty (and buggy ?) app to navigate through the Play2 documentation hosted on the [Play2 Github repository](https://github.com/playframework/Play20/tree/master/documentation/manual)

Written using Play 2.1 RC1 with Scala (bad Scala actually since it is my first Scala code...).

Do not hesitate to submit a pull request of fill an issue for enhancements :-)

## Thanks to

* [Pegdown](https://github.com/sirthias/pegdown/) for the markdown rendering.
* [Github API](http://developer.github.com/) for the navigation through the repository

## Continuous integration

Current build status (stucked at the moment due to a BuildHive/SBT issue): [![Build Status](https://buildhive.cloudbees.com/job/ndeverge/job/play2-docs-renderer/badge/icon)](https://buildhive.cloudbees.com/job/ndeverge/job/play2-docs-renderer/)

## TODOs

* Use the Github API to look for the file tree (<ouch>currently it is using a hard coded list of files</ouch>)
* Navigate through branches and tags (actually, you can only see master)
* Render images
* Fulltext search
* Search for broken links

## License

                DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
                        Version 2, December 2004

    Everyone is permitted to copy and distribute verbatim or modified
    copies of this license document, and changing it is allowed as long
    as the name is changed.

                DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
      TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION

    0. You just DO WHAT THE FUCK YOU WANT TO.