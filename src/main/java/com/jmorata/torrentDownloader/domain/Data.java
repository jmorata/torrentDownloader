package com.jmorata.torrentDownloader.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Builder
@lombok.Data
@EqualsAndHashCode
@lombok.ToString
public class Data {

    @Setter(AccessLevel.NONE)
    private String title;

    private String name;

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

    private static String getOnlyPermChars(String title) {return title.replaceAll("[^\\w.-]", "_");}

    public static class DataBuilder {
        public DataBuilder torrentLink(String torrentLink) {
            this.torrentLink = getWithoutSpaces(torrentLink);
            return this;
        }

        public DataBuilder title(String title) {
            this.title = getOnlyPermChars(title);
            return this;
        }
    }

}
