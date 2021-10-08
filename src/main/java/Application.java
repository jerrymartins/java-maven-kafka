import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Application {
    public static void main(String[] args) {
        try {
            System.out.println("enviando");
            var producer = new KafkaProducer<String, String>(properties());
            String value = "id, 123, qty, 500";
            var record = new ProducerRecord("ECOMMERCE_NEW_ORDER", value, value);
            producer.send(record);
            System.out.println("enviado");
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }
}
