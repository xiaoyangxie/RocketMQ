package com.alibaba.rocketmq.test.client;

import junit.framework.Assert;

import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;
import com.alibaba.rocketmq.client.consumer.MQPullConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.impl.MQClientManager;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.MixAll;
import com.alibaba.rocketmq.common.namesrv.TopAddressing;
import com.alibaba.rocketmq.test.BaseTest;
import org.junit.Test;

public class ClientRunTimer extends BaseTest{
	
	
	@Test//	���������������JVM����Ҫָ����ͬ��ʵ����
	//	ͨ���ڴ�����ָ��instanceName
	//ͨ����Java����������ָ��-Drocketmq.client.name=
	public void testMultiClient() throws MQClientException{
		DefaultMQPullConsumer consumer1=new DefaultMQPullConsumer("example.consumer.active");
		consumer1.getMQClientConfig().setInstanceName("DEFAULT1");
		consumer1.start();
		
		
		DefaultMQPullConsumer consumer2=new DefaultMQPullConsumer("example.consumer.active");
		consumer2.getMQClientConfig().setInstanceName("DEFAULT2");
		consumer2.start();
		
		System.setProperty("rocketmq.client.name", "DEFAULT3");
		DefaultMQPullConsumer consumer3=new DefaultMQPullConsumer("example.consumer.active");
		consumer3.getMQClientConfig().setInstanceName("DEFAULT3");
		consumer3.start();
		
		DefaultMQPullConsumer consumer4=new DefaultMQPullConsumer("example.consumer.active4");
		consumer4.getMQClientConfig().setInstanceName("DEFAULT1");
		consumer4.start();
		String clientid1 =MQClientManager.getInstance().getAndCreateMQClientFactory(consumer1.getMQClientConfig()).getClientId();
		String clientid2 =MQClientManager.getInstance().getAndCreateMQClientFactory(consumer2.getMQClientConfig()).getClientId();
		String clientid3 =MQClientManager.getInstance().getAndCreateMQClientFactory(consumer3.getMQClientConfig()).getClientId();
		String clientid4 =MQClientManager.getInstance().getAndCreateMQClientFactory(consumer4.getMQClientConfig()).getClientId();
		
		Assert.assertNotSame(clientid1, clientid2);
		Assert.assertNotSame(clientid1, clientid3);
		Assert.assertEquals(clientid1, clientid4);
		consumer1.shutdown();
		consumer2.shutdown();
		consumer3.shutdown();
		consumer4.shutdown();
		
	}
	
	
	@Test//	�ͻ����ṩ����Ѱַ��ʽ������Name Server��ַ�ķ�ʽ��
	
	//	ָ��ͨ���ڴ�����ָ��Name Server��ַ
	public void testClientFindNameServer() throws MQClientException{
		
		
		//ָ��ͨ���ڴ�����ָ��Name Server��ַ
		DefaultMQPullConsumer consumer2=new DefaultMQPullConsumer("example.consumer2.active");
		consumer2.getMQClientConfig().setNamesrvAddr("127.0.0.2:9876");
		consumer2.getMQClientConfig().setInstanceName("DEFAULT2");
		consumer2.start();
		Assert.assertEquals(consumer2.getMQClientConfig().getNamesrvAddr(),"127.0.0.2:9876");
		
//		//ͨ�����û�������NAMESRV_ADDR
//		DefaultMQPullConsumer consumer3=new DefaultMQPullConsumer("example.consumer3.active");
//		consumer3.getMQClientConfig().setInstanceName("DEFAULT3");
//		consumer3.start();
//		Assert.assertEquals(consumer3.getMQClientConfig().getNamesrvAddr(),"10.235.136.47:9876;127.0.0.1:9876");
		
		
		
		
		
		//�ɳ����Զ����� http://diamondserver.tbsite.net:8080/rocketmq/nsaddr ��ȡName Server��ַ���Ƽ�ʹ�ã�
		DefaultMQPullConsumer consumer4=new DefaultMQPullConsumer("example.consumer4.active");
		consumer4.getMQClientConfig().setInstanceName("DEFAULT4");
		consumer4.start();
		TopAddressing topAddressing = new TopAddressing();
		String nameSrvAddr = topAddressing.fetchNSAddr();
		Assert.assertEquals(consumer4.getMQClientConfig().getNamesrvAddr(),nameSrvAddr);
		
		//ͨ����Java����������ָ��-Drocketmq.namesrv.addr
		System.setProperty(MixAll.NAMESRV_ADDR_PROPERTY,"127.0.0.1:9876");
		DefaultMQPullConsumer consumer1=new DefaultMQPullConsumer("example.consumer1.active");
		consumer1.getMQClientConfig().setInstanceName("DEFAULT1");
		consumer1.start();
		Assert.assertEquals(consumer1.getMQClientConfig().getNamesrvAddr(),"127.0.0.1:9876");
		
		
		consumer1.shutdown();
		consumer2.shutdown();
//		consumer3.shutdown();
		consumer4.shutdown();
		
	}
	
	

}