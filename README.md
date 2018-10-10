## TorrentDownloader - a torrent downloader program for Synology NAS

*TorrentDownloader* is a program for download torrents from web pages that publish them.

* It uses engines for selected torrent source webs.
* It was developed as proof of concept for Synology's Download Station.

## Installation

For create distribution package execute maven command as:

    mvn package -P dist

When maven finished, it created a new directory in target called dist.

* Copy the contents of this directory into your NAS (e.g. /opt/local/torrentDownloader)
* Download JRE 8 package, e.g:

    https://pcloadletter.co.uk/2011/08/23/java-package-for-synology/

* `Optional:` Copy S84torrentDown bash script from `/src/etc/init.d/S84torrentDown` to /opt/etc/init.d and grants exec permissions
(the S84torrentDown file is an example for start the app as daemon service)

## Configuration

Edit `torrentDownloader.properties` file as defaults:

```java
    # --------------------
    # torrentDownloader config
    # --------------------

    # engine type
    torrent.engine=EliteTorrent

    # url feed
    torrent.url=https://www.elitetorrent.biz/peliculas-8

    # categories (splited by ,)
    # note: program uses first position as default directory
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
    dir.in=c:\\temp\\in
    dir.out=c:\\temp\\out

    # nano httpd parameter
    nano.host=localhost
    nano.port=9090

    # delete old files downloaded (0 disabled)
    delete.days=180
```



