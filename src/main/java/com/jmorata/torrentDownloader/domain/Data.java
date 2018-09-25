package com.jmorata.torrentDownloader.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@lombok.Data
@EqualsAndHashCode
public class Data {

    private String title;

    private String category;

    private String link;

    private String torrentLink;

    private String torrent;

}
