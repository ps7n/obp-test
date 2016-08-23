package obp.test;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by pstepaniak on 2016-08-23.
 */
class ConsumerThread extends Thread {

    private KafkaProducer<String, String> producer;
    private KafkaConsumer<String,String> consumer;

    private static final String CONSUMER_TOPIC = "Request";
    private static final String PRODUCER_TOPIC = "Response";

    private String response;

    public ConsumerThread(String bootstrapUrl) {
        initConsumer(bootstrapUrl);
        initProducer(bootstrapUrl);
    }

    private void initProducer(String bootstrapUrl) {
        Properties configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapUrl);
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer"); //ByteArraySerializer
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer"); //StringSerializer
        producer = new KafkaProducer<String, String>(configProperties);
    }

    private void initConsumer(String bootstrapUrl) {
        Properties configProperties = new Properties();
        configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapUrl);
        configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "1");
        configProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, "simple");

        consumer = new KafkaConsumer<String, String>(configProperties);
        consumer.subscribe(Arrays.asList(CONSUMER_TOPIC));
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, String> records = consumer.poll(1);
                for (ConsumerRecord<String, String> record : records)
                    processMsg(record);
            }
        } catch(Exception e){
            System.out.println("Exception caught " + e.getMessage());
            e.printStackTrace();
        } finally{
            consumer.close();
            System.out.println("After closing KafkaConsumer");
        }
    }

    private void processMsg(ConsumerRecord<String, String> record) {
        System.out.println("Received msg from Kafka.Request queue\n\tkey: " + record.key() + "\n\tvalue: " + record.value() + "\n");
        System.out.println("Sending msg to Kafka.Response queue\n\tkey: " + record.key() + "\n\tvalue: " + response + "\n");
        ProducerRecord<String, String> rec = new ProducerRecord<String, String>(
                PRODUCER_TOPIC,
                record.key(),
                response);

        producer.send(rec);
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
