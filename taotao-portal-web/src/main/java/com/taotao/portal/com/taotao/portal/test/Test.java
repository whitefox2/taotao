package com.taotao.portal.com.taotao.portal.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
public class Test {
    @org.junit.jupiter.api.Test
    public void show(){
        Jedis jedis = new Jedis("192.168.109.101",6379);
        jedis.set("key1","zhangsan");
        String key1 = jedis.get("key1");
        jedis.close();
        System.out.println(key1);
    }
    @org.junit.jupiter.api.Test
    public void show2(){
        JedisPool pool = new JedisPool("192.168.109.101",6379);
        Jedis jedis = pool.getResource();
        jedis.set("key2","lisi");
        String key2 = jedis.get("key2");
        jedis.close();
        pool.close();
        System.out.println(key2);
    }
}
