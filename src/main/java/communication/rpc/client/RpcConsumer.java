/*
 * Copyright 2011 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.jhui.communication.rpc.client;

/**
 * RpcConsumer
 * 
 * @author william.liangf
 */
public class RpcConsumer {
    
    public static void main(String[] args) throws Exception {
        HelloService service = RpcRefer.refer(HelloService.class, "127.0.0.1", 9234);
        String hello = service.hello("World");
        System.out.println(hello);
//        
//        for (int i = 0; i < Integer.MAX_VALUE; i ++) {
//            String hello = service.hello("World" + i);
//            System.out.println(hello);
//            Thread.sleep(1000);
//        }
    }
    
}
