package com.jmorata.torrentDownloader.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Builder
@lombok.Data
@EqualsAndHashCode
public class Data {

    private String title;

    private String category;

    private String link;

    @Setter(AccessLevel.NONE)
    private String torrentLink;

    private String torrent;

    public void setTorrentLink(String torrentLink) {
        this.torrentLink = getWithoutSpaces(torrentLink);
    }

    private static String getWithoutSpaces(String torrentLink) {
        return torrentLink.replaceAll(" ", "%20");
    }

    public static class DataBuilder {
        public DataBuilder torrentLink(String torrentLink) {
            this.torrentLink = getWithoutSpaces(torrentLink);
            return this;
        }
    }

}
