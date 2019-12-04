package com.eddie.gestor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class RedisCache {
    private static RedisCache instance = null;
    private static JedisPool redisPool = null;
    private static Logger logger = LogManager.getLogger(RedisCache.class);

    public synchronized static RedisCache getInstance() {
        if (instance == null) {
            instance = new RedisCache();
            instance.init();
        }
        return instance;
    }
    private static byte[] compress(Object object) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(buffer);
        ObjectOutputStream salida = new ObjectOutputStream(gzip);
        salida.writeObject(object);
        salida.close();
        return buffer.toByteArray();
    }

    private static Object decompress(byte[] data) throws IOException, ClassNotFoundException {
        if (data != null) {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            GZIPInputStream gzip = new GZIPInputStream(in);
            ObjectInputStream entrada = new ObjectInputStream(gzip);
            Object object = entrada.readObject();
            entrada.close();
            return object;
        } else {
            return null;
        }
    }

    /**
     * Inicializa Redis con parámetros.
     *
     * @param host     Ip o nombre de host del servidor.
     * @param port     Puerto por el que escucha Redis.
     * @param timeout  Tiempo de espera de conexión.
     * @param database Base de datos a seleccionar (Por defecto 0).
     */
    public static void init(String host, int port, int timeout, int database) {
        instance = new RedisCache();
        if (redisPool == null) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfigParams(poolConfig);
            redisPool = new JedisPool(poolConfig, host, port, timeout, null, database);
        }
    }

    protected void init() {
        if (redisPool == null) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfigParams(poolConfig);
            redisPool = new JedisPool(poolConfig,"127.0.0.1",6379, Protocol.DEFAULT_TIMEOUT, null,0);
        }
    }

    public Jedis getClient(int database) {
        try {
            if (redisPool != null) {
                Jedis jedis = redisPool.getResource();
                jedis.select(database);
                return jedis;
            } else {
                throw new Exception("Error conseguir pool del redis: RedisCache.java");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getValue(String key ,int database) {
        try (Jedis client = this.getClient(database)) {
            return decompress(client.get(key.getBytes()));
        } catch (Exception e) {
            return null;
        }
    }

    public Object getValue(Jedis client, String key) {
        if (client == null) {
            return null;
        }
        try {
            return decompress(client.get(key.getBytes()));
        } catch (Exception e) {
            return null;
        }

    }

    public boolean setValue(String key, Object value, int database, Integer expiryTimeInSeconds) {
        try (Jedis client = this.getClient(database)) {
            String result = "";
            if (expiryTimeInSeconds != null && expiryTimeInSeconds > 0) {
                result = client.setex(key.getBytes(), expiryTimeInSeconds, compress(value));
            } else {
                result = client.set(key.getBytes(), compress(value));
            }
            return "OK".equalsIgnoreCase(result);
        } catch (Exception e) {
            return false;
        }
    }


    public boolean setValue(Jedis client, String key, String value, Integer expiryTimeInSeconds) {
        try {
            String result = "";
            if (expiryTimeInSeconds != null && expiryTimeInSeconds > 0) {
                result = client.setex(key, expiryTimeInSeconds, value);
            } else {
                result = client.set(key, value);
            }
            return "OK".equalsIgnoreCase(result);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delValue(String key,int database) {
        try (Jedis client = this.getClient(database)) {
            return client.del(key.getBytes()) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delValue(Jedis client, String key) {
        try {
            return client.del(key.getBytes()) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exist (String key,int database){
        try (Jedis client = this.getClient(database)) {
            return client.exists(key);
        } catch (Exception e) {
            return false;
        }
    }

    private static void poolConfigParams(JedisPoolConfig poolConfig) {
        poolConfig.setMaxTotal(500); //Maximum active connections to Redis instance
        poolConfig.setMaxIdle(100); //Number of connections to Redis that just sit there and do nothing
        poolConfig.setMinIdle(0);//Minimum number of idle connections to Redis - these can be seen as always open and ready to serve
        poolConfig.setTestOnBorrow(true); //Tests whether connection is dead when connection retrieval method is called
        poolConfig.setTestOnReturn(true); //Tests whether connection is dead when returning a connection to the pool
        poolConfig.setTestWhileIdle(true); //Tests whether connections are dead during idle periods
        poolConfig.setBlockWhenExhausted(true);
    }
}
