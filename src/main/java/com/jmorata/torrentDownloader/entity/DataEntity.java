package com.jmorata.torrentDownloader.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@lombok.Data
@EqualsAndHashCode
public class DataEntity {

    private String title;

    private String category;

    private String link;

    private String torrentLink;

    private String torrent;

}
