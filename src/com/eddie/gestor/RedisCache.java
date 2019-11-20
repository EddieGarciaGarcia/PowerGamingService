package com.eddie.gestor;

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
            redisPool = new JedisPool(poolConfig, "sdsa",6379 , Protocol.DEFAULT_TIMEOUT, null);
        }
    }

    public Jedis getClient() {
        try {
            if (redisPool != null) {
                return redisPool.getResource();
            } else {
                throw new Exception("Error conseguir pool del redis: RedisCache.java");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getValue(String key) {
        try (Jedis client = this.getClient()) {
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

    public boolean setValue(String key, Object value, Integer expiryTimeInSeconds) {
        try (Jedis client = this.getClient()) {
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

    public boolean setValue(Jedis client, String key, Object value, Integer expiryTimeInSeconds) {
        try {
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

    public boolean delValue(String key) {
        try (Jedis client = this.getClient()) {
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
