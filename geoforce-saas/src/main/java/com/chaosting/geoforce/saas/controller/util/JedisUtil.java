package com.chaosting.geoforce.saas.controller.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>Title:JedisUtil</p>
 * <p>Description: </p>
 * 网关jedis工具类
 * <p>Company: 成都地图慧科技有限公司</p>
 *
 * @author Yun Zhou
 * @date 2017年5月4日10:16:44
 */
@Component
public class JedisUtil {

    //Redis服务器地址
    @Value("${jedis.host}")
    private String host;

    //Redis的端口号
    @Value("${jedis.port}")
    private int port;

    //访问密码
    @Value("${jedis.password}")
    private String password;

    @Value("${jedis.timeout}")
    private int timeout;

    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private int max_active = 1024;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private int max_idle = 200;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private int max_wait = 10000;


    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private boolean test_on_borrow = true;

    private final Logger LOGGER = Logger.getLogger(JedisUtil.class);

    private static JedisPool jedisPool;

    /**
     * 初始化Redis连接池
     */
    @PostConstruct
    private void init() {
        try {
            System.out.println("redis init");
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(max_active);
            config.setMaxIdle(max_idle);
            config.setMaxWaitMillis(max_wait);
            config.setTestOnBorrow(test_on_borrow);
            jedisPool = new JedisPool(config, host, port, timeout, password);
        } catch (Exception e) {
            LOGGER.error("初始化Redis连接池异常", e);
        }
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public synchronized Jedis getJedis() {
        try {
            if (null == jedisPool)
                init();
            Jedis resource = jedisPool.getResource();
            return resource;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返还到连接池
     *
     * @param jedis
     */
    public void returnResource(final Jedis jedis) {
        if (jedis != null && jedisPool != null) {
            jedis.close();
        }
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    public void returnBrokenResource(final Jedis jedis) {
        if (jedis != null && jedisPool != null) {
            jedisPool.close();
        }
    }

    /**
     * 释放Pipeline资源
     *
     * @param pipeline
     */
    public void returnPipeline(final Pipeline pipeline) throws IOException {
        if (pipeline != null ) {
            pipeline.close();
        }
    }

    /**
     * 存在的key
     *
     * @param keys
     * @return
     */
    public List<String> exist(Set<String> keys) {
        List<String> keysExist = new ArrayList<>();
        List<Response<Boolean>> exists = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            Pipeline pipeline = jedis.pipelined();
            for (String key : keys) {
                Response<Boolean> response = pipeline.exists(key);
                exists.add(response);
            }
            pipeline.sync();
            int n = 0;
            for (String key : keys) {
                if (exists.get(n).get()) {
                    keysExist.add(key);
                }
                n++;
            }
        } catch (Exception e) {
            returnBrokenResource(jedis);
            LOGGER.error(e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }
        return keysExist;
    }

    /**
     * 根据key获取值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            value = jedis.get(key);
        } catch (Exception e) {
            returnBrokenResource(jedis);
            LOGGER.error(e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 根据多个key获取值list
     *
     * @param keys
     * @return
     */
    public List<String> get(String... keys) {
        List<String> valueList = new ArrayList<>();
        Jedis jedis = null;
        try {
            getJedis().mget(keys);
            for (String key : keys) {
                valueList.add(get(key));
            }
        } catch (Exception e) {
            returnBrokenResource(jedis);
            LOGGER.error(e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }
        return valueList;
    }

    /**
     * 根据多个keyList获取值list
     *
     * @param keys
     * @return
     */

    public List<String> get(List<String> keys) {
        List<String> valueList = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            valueList = jedis.mget(keys.toArray(new String[keys.size()]));
        } catch (Exception e) {
            returnBrokenResource(jedis);
            LOGGER.error(e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }
        return valueList;
    }

    /**
     * 设置值 默认'永不过期'
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        set(key, value, Integer.MAX_VALUE);
    }

    /**
     * 设置值和过期时间
     *
     * @param key
     * @param value
     */
    public void set(String key, String value, int time) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
            jedis.expire(key, time);
        } catch (Exception e) {
            returnBrokenResource(jedis);
            LOGGER.error(e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 删除
     *
     * @param key
     */
    public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.del(key);
        } catch (Exception e) {
            returnBrokenResource(jedis);
            LOGGER.error(e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 批量删除
     */
    public void del(List<String> keys) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.del(keys.toArray(new String[keys.size()]));
        } catch (Exception e) {
            returnBrokenResource(jedis);
            LOGGER.error(e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 修改
     */
    public void update(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis.exists(key)) {
                this.set(key, value);
            }
        } catch (Exception e) {
            returnBrokenResource(jedis);
            LOGGER.error(e.getMessage(), e);
        } finally {
            returnResource(jedis);
        }
    }

}
