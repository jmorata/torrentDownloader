package com.jmorata.torrentDownloader.domain;

import lombok.Builder;

@Builder
@lombok.Data
public class Data {

    private String title;

    private String category;

    private String link;

    private String fileName;

    private String torrentLink;

    private String torrent;

}
