package com.jmorata.torrentDownloader.repository;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.exception.TorrentDownloaderException;
import com.jmorata.torrentDownloader.service.PropertiesService;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Iterator;
import java.util.Set;

public class SynologyRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Boolean clear;

    private final String user;

    private final String pass;

    private static Boolean connect = false;

    private static Connection torrentConn = null;

    private static Connection synoConn = null;

    public SynologyRepository(PropertiesService propertiesService) throws TorrentDownloaderException {
        clear = new Boolean(propertiesService.getProperty("sql.clear"));
        user = propertiesService.getProperty("sql.user");
        pass = propertiesService.getProperty("sql.pass");

        afterPropertiesSet();
    }

    private void afterPropertiesSet() throws TorrentDownloaderException {
        try {
            connectTorrent();
            clearDB();

        } catch (PSQLException e) {

            try {
                createDB();

            } catch (Exception e1) {
                throw new TorrentDownloaderException("There is an error creating database, please close all open instances", e1);
            }

        } catch (Exception e) {
            throw new TorrentDownloaderException("There is an error connecting to database", e);

        } finally {
            connect = true;
        }
    }

    private void clearDB() throws SQLException {
        if (clear) {
            logger.info("Clearing BBDD rssdown");
            torrentConn.close();
            Statement stmt = connect();
            stmt.execute("drop database rssdown");
            buildDB(stmt);
        }
    }

    private void createDB() throws SQLException {
        logger.info("Clearing BBDD rssdown");
        buildDB(connect());
    }

    private void buildDB(Statement stmt) throws SQLException {
        stmt.execute("create database rssdown");
        torrentConn.close();
        connectTorrent();
        stmt = torrentConn.createStatement();
        stmt.execute("create table data (title VARCHAR(2000),category VARCHAR(256),link VARCHAR(512))");
    }

    private Statement connect() throws SQLException {
        torrentConn = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", user, pass);
        return torrentConn.createStatement();
    }

    private void connectTorrent() throws SQLException {
        if (torrentConn == null || torrentConn.isClosed()) {
            torrentConn = DriverManager.getConnection("jdbc:postgresql://localhost/rssdown", user, pass);
        }
    }

    private void connectSynology() throws SQLException {
        if (synoConn == null || synoConn.isClosed()) {
            synoConn = DriverManager.getConnection("jdbc:postgresql://localhost/download", user, pass);
        }
    }

    public void persistTorrentDataSet(Set<Data> data) throws Exception {
        connectTorrent();
        for (Data rss : data) {

            PreparedStatement pstmt = torrentConn
                    .prepareStatement("insert into data(title, category, link) values (?,?,?)");

            pstmt.setString(1, rss.getTitle());
            pstmt.setString(2, rss.getCategory());
            pstmt.setString(3, rss.getLink());

            pstmt.executeUpdate();
        }
    }

    public void persistDownloaderDataSet(Set<Data> data) throws Exception {
        connectSynology();
        java.util.Date date = new java.util.Date();
        for (Data rss : data) {

            // download_queue
            PreparedStatement pstmt = synoConn.prepareStatement(
                    "insert into download_queue (username,pid,url,filename,status,created_time,started_time,"
                            + "total_size,current_size,current_rate,total_peers,connected_peers,total_pieces,downloaded_pieces,"
                            + "available_pieces,upload_rate,total_upload,seeding_ratio,seeding_interval,seeding_elapsed,task_flags,"
                            + "seeders,leechers,destination,unzip_progress,thumbnail_status,completed_time,waiting_until_time,file_oid) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pstmt.setString(1, "admin");
            pstmt.setInt(2, -1);
            pstmt.setString(3, rss.getTorrentLink());
            pstmt.setString(4, rss.getTorrent());
            pstmt.setInt(5, 1);
            pstmt.setLong(6, (date.getTime() / 1000L));
            for (int i = 7; i <= 20; i++) {
                pstmt.setInt(i, 0);
            }
            pstmt.setInt(21, 512);
            pstmt.setInt(22, 0);
            pstmt.setInt(23, 0);
            pstmt.setString(24, "public");
            for (int i = 25; i <= 29; i++) {
                pstmt.setInt(i, 0);
            }

            logger.debug(pstmt.toString());
            pstmt.executeUpdate();
        }
    }

    public void checkTorrentDataSet(Set<Data> data) throws Exception {
        connectTorrent();
        Iterator<Data> it = data.iterator();
        while (it.hasNext()) {
            Statement stmt = torrentConn.createStatement();
            ResultSet rs = stmt
                    .executeQuery("select count(*) from data where title='" + it.next().getTitle() + "'");
            while (rs.next()) {
                Integer count = rs.getInt(1);
                if (count > 0) {
                    it.remove();
                    break;
                }
            }
        }
    }

    public String getCategory(String title) throws Exception {
        String category = null;
        connectTorrent();
        Statement stmt = torrentConn.createStatement();
        ResultSet rs = stmt.executeQuery("select category from data where title ilike '%" + title + "%'");
        while (rs.next()) {
            category = rs.getString(1);
            break;
        }

        return category;
    }

    public Boolean isConnected() {
        return connect;
    }

}
