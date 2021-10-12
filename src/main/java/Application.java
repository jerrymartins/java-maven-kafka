import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;



public class Application {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("enviando");
        var producer = new KafkaProducer<String, String>(properties());
        Callback callback = (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }

            System.out.println("enviado para: " + data.topic() + " OFFSET::: " + data.offset() + " PARTITION::: " + data.partition() );
        };

        for (int i = 0; i < 2; i++) {
            String value = UUID.randomUUID().toString();
            var record = new ProducerRecord("ECOMMERCE_NEW_ORDER", value, value);
            producer.send(record, callback).get();
        }

        try (AdminClient adminClient = AdminClient.create(properties())) {
            try {
                var result = adminClient.deleteTopics(List.of("ECOMMERCE_NEW_ORDER"));
                result.wait();
            } catch (final Exception e) {
                throw new RuntimeException("Failed :", e);
            }
        }

    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;
    }


}
