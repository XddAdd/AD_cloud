package com.add.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class FileFolder {
    //文件夹id
    private Integer id;
    //文件夹名称
    private String fileFolderName;
    //父文件夹id
    private Integer parentFolderId;
    //文件夹存储路径
    private String fileFolderPath ;
    //所属文件仓库id
    private Integer fileStorehouseId;
    //文件夹创建时间
    private Date createTime;
}
