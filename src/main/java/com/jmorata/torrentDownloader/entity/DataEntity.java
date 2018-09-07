package com.jmorata.torrentDownloader.entity;

import lombok.Builder;

@Builder
@lombok.Data
public class DataEntity {

    private String title;

    private String category;

    private String link;

    private String torrentLink;

    private String torrent;

}
