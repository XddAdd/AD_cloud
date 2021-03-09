package com.add.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class File {
    //文件id
    private Integer id;
    //文件名称
    private String fileName;
    //文件存储路径
    private String filePath;
    //文件下载次数
    private Integer downloadCount ;
    //所属文件夹id
    private Integer parentFolderId;
    //所属文件仓库id
    private Integer fileStorehouseId;
    //文件上传时间
    private Date uploadTime;
    //文件后缀
    private String filePostfix;
    //文件大小
    private Long fileSize;

}
