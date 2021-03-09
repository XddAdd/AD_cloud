package com.add.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Setter
public class FileStorehouse {
    //文件仓库id
    private Integer id;
    //所属用户id
    private Integer userId;
    //当前容量(KB)
    private Long currentSize;
    //最大容量(KB)
    private Long maxSize;
    //当前页展示的文件夹
    private List<FileFolder> fileFolderList;
    //当前页展示的文件
    private List<File> fileList;
}
