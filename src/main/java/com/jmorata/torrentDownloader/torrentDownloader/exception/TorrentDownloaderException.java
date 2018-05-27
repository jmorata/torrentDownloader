package com.jmorata.torrentDownloader.torrentDownloader.exception;

public class TorrentDownloaderException extends RuntimeException{

    public TorrentDownloaderException(String message) {
        super(message);
    }

    public TorrentDownloaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
