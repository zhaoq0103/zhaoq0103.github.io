package com.ai.x.web;

import com.ai.x.bean.TransferRequestBean;
import com.ai.x.enums.UserType;
import com.ai.x.message.event.TransferEvent;
import com.ai.x.service.SendEventService;
import com.ai.x.support.AbstractApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/internal")
public class TradingApi2EngineInternalController extends AbstractApiController {
    @Autowired
    SendEventService sendEventService;

    /**
     * 处理一个转账请求，可重复调用，重复发送消息，根据un
     *
     * iqueId去重，仅定序一次。
     */
    @PostMapping("/transfer")
    public Map<String, Boolean> transferIn(@RequestBody TransferRequestBean transferRequest) {
        logger.error("TradingInternalApiController ???");
        logger.info("transfer request: transferId={}, fromUserId={}, toUserId={}, asset={}, amount={}",
                transferRequest.transferId, transferRequest.fromUserId, transferRequest.toUserId, transferRequest.asset,
                transferRequest.amount);
        transferRequest.validate();

        var message = new TransferEvent();
        // IMPORTANT: set uniqueId to make sure the message will be sequenced only once:
        message.uniqueId = transferRequest.transferId;
        message.fromUserId = transferRequest.fromUserId;
        message.toUserId = transferRequest.toUserId;
        message.asset = transferRequest.asset;
        message.amount = transferRequest.amount;
        message.sufficient = transferRequest.fromUserId.longValue() != UserType.DEBT.getInternalUserId();
        this.sendEventService.sendMessage(message);
        logger.info("transfer event sent: {}", message);
        return Map.of("result", Boolean.TRUE);
    }
}
