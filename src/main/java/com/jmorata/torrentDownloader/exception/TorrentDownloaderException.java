package com.jmorata.torrentDownloader.exception;

public class TorrentDownloaderException extends Exception {

    public TorrentDownloaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public TorrentDownloaderException(String message) {
        super(message);
    }

}
