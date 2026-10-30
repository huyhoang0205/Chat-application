package com.huyhoang25.chatapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Value("${spring.data.redis.host:localhost}")
    private String host;

    @Value("${spring.data.redis.port:6379}")
    private int port;

    @Value("${spring.data.redis.password:}")
    private String password;

    @Value("${spring.data.redis.ssl.enabled:false}")
    private boolean sslEnabled;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(host, port);
        
        if (password != null && !password.isEmpty()) {
            redisConfiguration.setPassword(password);
        }

        // Cấu hình Lettuce Client (bật SSL nếu dùng Upstash)
        LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfigBuilder = LettuceClientConfiguration.builder();
        if (sslEnabled) {
            clientConfigBuilder.useSsl();
        }
        LettuceClientConfiguration clientConfig = clientConfigBuilder.build();

        return new LettuceConnectionFactory(redisConfiguration, clientConfig);
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        GenericJacksonJsonRedisSerializer jsonRedisSerializer = GenericJacksonJsonRedisSerializer.builder().build();
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);

        return template;
    }

    @Bean
    public CommandLineRunner checkRedisConnection(RedisConnectionFactory connectionFactory) {
        return args -> {
            try {
                // Thử gửi lệnh PING tới Redis server
                String response = connectionFactory.getConnection().ping();
                System.out.println("==================================================");
                System.out.println("🚀 [REDIS] Kết nối Redis thành công! Phản hồi: " + response);
                System.out.println("==================================================");
            } catch (Exception e) {
                System.err.println("==================================================");
                System.err.println("❌ [REDIS] Lỗi kết nối Redis: " + e.getMessage());
                System.err.println("==================================================");
            }
        };
    }
}
