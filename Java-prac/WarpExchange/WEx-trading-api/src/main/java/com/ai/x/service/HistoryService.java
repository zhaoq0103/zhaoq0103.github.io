package com.ai.x.service;

import com.ai.x.bean.SimpleMatchDetailRecord;
import com.ai.x.db.DbTemplate;
import com.ai.x.model.trade.MatchDetailEntity;
import com.ai.x.model.trade.OrderEntity;
import com.ai.x.support.AbstractDbSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistoryService extends AbstractDbSupport {
    public List<OrderEntity> getHistoryOrders(Long userId, int maxResults) {
        List<OrderEntity> list = db.from(OrderEntity.class).where("userId = ?", userId).orderBy("id").desc().limit(maxResults).list();
        return list;
    }

    public OrderEntity getHistoryOrder(Long userId, Long orderId) {
        OrderEntity entity = db.fetch(OrderEntity.class, orderId);
        if (entity == null || entity.userId.longValue() != userId.longValue()) {
            return null;
        }
        return entity;
    }

    public List<SimpleMatchDetailRecord> getHistoryMatchDetails(Long orderId) {
        List<MatchDetailEntity> details = db.select("price", "quantity", "type").from(MatchDetailEntity.class)
                .where("orderId = ?", orderId).orderBy("id").list();
        return details.stream().map(e -> new SimpleMatchDetailRecord(e.price, e.quantity, e.type))
                .collect(Collectors.toList());
    }

}
