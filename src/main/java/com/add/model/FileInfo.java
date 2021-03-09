package com.add.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


@Getter
@Setter
@ToString
public class FileInfo {
    private Integer id;//任务id
    private String url;//下载url
    private Long fileSize;//文件大小
    private Long downloadSize;//已经下载大小
    private Integer downloadStatus;//文件下载状态
    private String targetPath;//下载目标路径
    private Integer parentFolderId;//父文件id
    private Integer fileStorehouseId;//文件仓库id
    private Date startTime;//开始时间
    private String message;//提示信息
}
