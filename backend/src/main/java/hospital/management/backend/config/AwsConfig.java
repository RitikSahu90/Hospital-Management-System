package hospital.management.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfig {
    @Bean
    public S3Client s3Client(SecretsConfig secretsConfig) {
        SecretsConfig.Aws aws = secretsConfig.getAws();
        var builder = S3Client.builder().region(Region.of(aws.getRegion()));
        if (aws.getAccessKeyId() != null && !aws.getAccessKeyId().isBlank()
                && aws.getSecretAccessKey() != null && !aws.getSecretAccessKey().isBlank()) {
            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(aws.getAccessKeyId(), aws.getSecretAccessKey())));
        }
        return builder.build();
    }

    @Bean
    public S3Presigner s3Presigner(SecretsConfig secretsConfig) {
        SecretsConfig.Aws aws = secretsConfig.getAws();
        var builder = S3Presigner.builder().region(Region.of(aws.getRegion()));
        if (aws.getAccessKeyId() != null && !aws.getAccessKeyId().isBlank()
                && aws.getSecretAccessKey() != null && !aws.getSecretAccessKey().isBlank()) {
            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(aws.getAccessKeyId(), aws.getSecretAccessKey())));
        }
        return builder.build();
    }
}