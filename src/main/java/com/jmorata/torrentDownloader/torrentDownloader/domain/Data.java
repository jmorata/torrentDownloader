package com.jmorata.torrentDownloader.torrentDownloader.domain;

import lombok.Builder;

@Builder
public class Data {

    private String title;

    private String category;

    private String link;

    private String fileName;

    private String torrentLink;

    private String torrent;

}
