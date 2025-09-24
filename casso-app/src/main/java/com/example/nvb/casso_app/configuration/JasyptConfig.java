package com.example.nvb.casso_app.configuration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JasyptConfig {
    @NonFinal
    @Value("${token.JASYPT_SECRET}")
    String JASYPT_SECRET;
    @Bean
    public StringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(JASYPT_SECRET);
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        return encryptor;
    }
}
