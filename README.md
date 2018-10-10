## TorrentDownloader - a torrent downloader program for Synology NAS

*TorrentDownloader* is a program for download torrents from web pages

* It uses engines for selected torrent source webs
* It was developed as proof of concept for Synology's Download Station

## Features

* Download torrent files processing web pages with word tokens
* Adds this torrents to Synology's Download Station service
* Sort download files creating category directory for each torrent
* Delete unused files (rolling) configured by old days
* Uses NanoHttpd as internal web server for downloaded torrents

    https://github.com/NanoHttpd/nanohttpd

## Installation

For create distribution package execute maven command as:

    mvn package -P dist

When maven finished, it created a new directory in target called dist.

* Copy the contents of this directory into your NAS (e.g. /opt/local/torrentDown)
* Download JRE 8 package, e.g:

    https://pcloadletter.co.uk/2011/08/23/java-package-for-synology/

* `Optional`- copy S84torrentDown bash script from `/src/etc/init.d/S84torrentDown` to /opt/etc/init.d and grants exec permissions
(the S84torrentDown file is an example for start the app as daemon service)

* Execute as:

    java -Duser.timezone=Europe/Madrid -jar torrentDownloader-2.0.jar

## Configuration

Edit `torrentDownloader.properties` file as defaults:

```
    # --------------------
    # torrentDownloader config
    # --------------------

    # engine type
    torrent.engine=EliteTorrent

    # url feed
    torrent.url=https://www.elitetorrent.biz/peliculas-8

    # categories (splited by ,)
    torrent.categories=1080p-castellano

    # postgreSQL parameters
    sql.clear=false
    sql.user=admin
    sql.pass=dd@awylds

    # quartz parameters
    quartz.check=0 0/15 * * * ?
    quartz.sort=0 0/30 * * * ?
    quartz.delete=0 0 4 * * ?

    # input/output directories
    dir.in=/volume1/public
    dir.out=/volume1/video

    # nano httpd parameter
    nano.host=localhost
    nano.port=9090

    # delete old files downloaded (0 disabled)
    delete.days=180
```

* `torrent.engine` - selects engine implementation, currently only one is implemented
* `torrent.categories` - url base page for engine
* `torrent.categories` - highlighted words in web page for the engine
* `sql.clear` - recreate internal database (removes all torrents downloaded)
* `sql.user` - internal parameter for Synology's Download Station database
* `sql.pass` - internal parameters for Synology's Download Station database
* `quartz.check` - a cron expression for checking page updates
* `quartz.sort` - a cron expression for sorting files
* `quartz.delete` - a cron expression for delete old files (see delete.days)
* `dir.in` - download directory for torrents (as Download Station config)
* `dir.out` - destination directory for sorted files
* `nano.host` - host config for nano httpd
* `nano.port` - port config for nano httpd
* `delete.days` - number of days for delete old files (rolling files)

