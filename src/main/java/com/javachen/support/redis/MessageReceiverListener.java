//package com.wesine.ads.support.redis;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.wesine.ads.common.enums.WSPubEventType;
//import com.wesine.ads.common.utils.JsonUtils;
//import com.wesine.ads.business.service.WebSocketService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Component
//public class MessageReceiverListener implements MessageListener {
//    @Autowired
//    private WebSocketService webSocketService;
//
//    @Override
//    public void onMessage(final Message message, final byte[] pattern) {
//        log.info("subscribe message,pattern:{}，message:{}", new String(pattern), message.toString());
//
//        Map<String, Object> messageMap = JsonUtils.fromJson(message.toString(), new TypeReference<Map<String, Object>>() {
//        });
//        String eventType = (String) messageMap.get("type");
//        if (StringUtils.isEmpty(eventType)) {
//            return;
//        }
//        List<String> objIds = (List<String>) messageMap.get("ids");
//        if (eventType.equals(WSPubEventType.StoreAdschedUpdated.name())) {
//            webSocketService.handlePlayListRequest(objIds);
//        }
//    }
//}
