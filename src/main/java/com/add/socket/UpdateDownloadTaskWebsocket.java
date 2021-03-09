package com.add.socket;

import com.add.dao.FileDao;
import com.add.exception.AppException;
import com.add.model.FileInfo;
import com.add.util.JsonUtil;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;

@ServerEndpoint("/updateDownloadTaskWebsocket")
public class UpdateDownloadTaskWebsocket {
    private FileDao fileDao = new FileDao();
    private Session session;


    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        System.out.println("有新连接");
    }

    /**
     * 收到客户端消息后调用的方法
     * @param fileInfoJson
     * @param session
     */
    @OnMessage
    public void onMessage(String fileInfoJson, Session session) {
        System.out.println("来自客户端的消息:");
        FileInfo fileInfo = JsonUtil.deSerialize(fileInfoJson, FileInfo.class);
        boolean flag = true;
        //查询当前用户所有的下载任务
        while (true) {
            try {
                if (flag == true) {
                    List<FileInfo> fileInfos = fileDao.queryAllDownloadTask(fileInfo.getFileStorehouseId());
                    //返回所有下载任务的json字符串
                    this.session.getBasicRemote().sendText(JsonUtil.serialize(fileInfos));
                    if (fileInfos.size() == 0) break;//无下载任务
                    flag = false;
                } else {
                    Thread.sleep(1000);
                    flag = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AppException("SOCKET001", "WebSocket发送数据出错");
            }
        }

        System.out.println("下载完成");
    }



    @OnClose
    public void onClose() throws IOException {
        System.out.println("已经断开");
    }

}
