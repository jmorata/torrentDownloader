package com.jmorata.torrentDownloader.adapter;

import com.jmorata.torrentDownloader.domain.Data;
import com.jmorata.torrentDownloader.entity.DataEntity;

import java.util.HashSet;
import java.util.Set;

public class DataAdapter {

    public static Set<DataEntity> getDataEntitySet(Set<Data> dataSet) {
        Set<DataEntity> dataEntitySet = new HashSet<>();
        for (Data data : dataSet) {
            DataEntity dataEntity = getDataEntity(data);
            dataEntitySet.add(dataEntity);
        }

        return dataEntitySet;
    }

    private static DataEntity getDataEntity(Data data) {
        return DataEntity.builder()
                        .category(data.getCategory())
                        .link(data.getLink())
                        .title(data.getTitle())
                        .torrent(data.getTorrent())
                        .torrentLink(data.getTorrentLink())
                        .build();
    }

    public static Set<Data> getDataSet(Set<DataEntity> dataEntitySet) {
        Set<Data> dataSet=new HashSet<>();
        for (DataEntity dataEntity : dataEntitySet) {
            Data data = getData(dataEntity);
            dataSet.add(data);
        }

        return dataSet;
    }

    private static Data getData(DataEntity dataEntity) {
        return Data.builder()
                .category(dataEntity.getCategory())
                .link(dataEntity.getLink())
                .title(dataEntity.getTitle())
                .torrent(dataEntity.getTorrent())
                .torrentLink(dataEntity.getTorrentLink())
                .build();
    }

}
