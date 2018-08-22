package com.dreawer.goods.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.dreawer.goods.lang.PurchaseInfo;
import com.dreawer.goods.service.SkuService;
import com.dreawer.goods.utils.GoodsPacket;
import com.dreawer.goods.utils.OrderPacket;
import com.dreawer.responsecode.rcdt.ResponseCode;
import com.google.gson.Gson;
import static com.dreawer.goods.constants.DomainConstants.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultMessageListener implements MessageListener {
	
	private Logger logger = LoggerFactory.getLogger(DefaultMessageListener.class);

    @Autowired
    private SkuService skuService; //SKUservice
	
	@Override
	public Action consume(Message message, ConsumeContext context) {
		logger.info("收到MQ消息：" + message.getMsgID());
		System.out.println(message.getBody());
		try {
			
			//创建购买信息列表
			List<PurchaseInfo> purchaseInfos = new ArrayList<>();
			
			//获取请求体
			String body = new String(message.getBody());
			if(StringUtils.isNotEmpty(body)){
				OrderPacket orderPacket = new Gson().fromJson(body, OrderPacket.class);
				
				//获取用户ID
				String userId = orderPacket.getUserId();
				
				//判断用户ID是否为空
				if(StringUtils.isNotEmpty(userId)){
					
					//获取订单ID
					String orderId = orderPacket.getOrderId();
					
					//判断订单ID是否为空
					if(StringUtils.isNotEmpty(orderId)){
						//获取商品购买信息
						List<GoodsPacket> goodsPackets = orderPacket.getGoods();
						
						//判断购买信息是否为空
						if(goodsPackets != null && goodsPackets.size()>0){
							for (GoodsPacket goodsPacket : goodsPackets) {
								String spuId = goodsPacket.getSpuId();
								String skuId = goodsPacket.getSkuId();
								Integer quantity = goodsPacket.getQuantity();
								if(StringUtils.isNotEmpty(spuId) && StringUtils.isNotEmpty(skuId) && quantity!=null){
									PurchaseInfo purchaseInfo = new PurchaseInfo();
									purchaseInfo.setGoodsId(spuId);
									purchaseInfo.setSkuId(skuId);
									purchaseInfo.setQuantity(quantity);
									purchaseInfos.add(purchaseInfo);
								}
							}
							
							//判断tag
							String tag = message.getTag();
							if(StringUtils.isNotEmpty(tag)){
								if(tag.equals(LOCK_INVENTORY)){
									
									//执行锁定库存
									ResponseCode responseCode = skuService.lockBatchInventory(purchaseInfos, orderId, userId, new Timestamp(System.currentTimeMillis()));
									
									if(responseCode.getCode().equals("000000")){
										//锁定库存成功
										
									}else{
										//锁定库存失败
										
									}
									
								}else if(tag.equals(RELEASE_INVENTORY)){
									
									//执行释放库存
									ResponseCode responseCode = skuService.releaseBatchInventory(purchaseInfos, orderId, userId, new Timestamp(System.currentTimeMillis()));
									
									if(responseCode.getCode().equals("000000")){
										//释放库存成功
										
									}else{
										//释放库存失败
										
									}
									
									
								}else if(tag.equals(DEDUCTION_INVENTORY)){
									
									//执行扣减库存
									ResponseCode responseCode = skuService.deductionBatchInventory(purchaseInfos, orderId, userId, new Timestamp(System.currentTimeMillis()));
									
									if(responseCode.getCode().equals("000000")){
										//扣减库存成功
										
									}else{
										//扣减库存失败
										
									}
									
								}
							}
						}
					}
				}
			}
			
            return Action.CommitMessage;
        }catch (Exception e) {
        	
            //消费失败
            return Action.ReconsumeLater;
        }
	}

}
