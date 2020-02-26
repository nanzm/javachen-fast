//package com.wesine.ads.support.redis;
//
//import com.wesine.ads.common.enums.WSPubEventType;
//import com.wesine.ads.common.utils.JsonUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.wesine.ads.config.RedisConfig.REDIS_CHANNEL;
//
//@Component
//@Slf4j
//public class MessageSender {
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    public void sendMessage(WSPubEventType wsPubEventType, List<String> objIds) {
//        if (wsPubEventType == WSPubEventType.StoreAdschedUpdated) {
//            Map<String, Object> message = new HashMap<>();
//            message.put("type", WSPubEventType.StoreAdschedUpdated);
//            message.put("ids", objIds);
//
//            log.info("publish event={}", message);
//            stringRedisTemplate.convertAndSend(REDIS_CHANNEL, JsonUtils.toJson(message));
//        }
//    }
//}
