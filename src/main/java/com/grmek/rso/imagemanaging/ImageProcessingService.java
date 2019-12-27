package com.grmek.rso.imagemanaging;

import com.kumuluz.ee.streaming.common.annotations.StreamListener;
import com.kumuluz.ee.streaming.common.annotations.StreamProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@ApplicationScoped
public class ImageProcessingService {

    @Inject
    private ConfigurationProperties cfg;

    @Inject
    @StreamProducer
    private Producer producer;

    public void requestProcessing(int id, String url) {
        ProducerRecord<String, String> record =
                new ProducerRecord<String, String>("ptjc95jg-requests", Integer.toString(id), url);

        producer.send(record, (metadata, e) -> {
            if (e != null) {
                System.err.println(e);
            }
            else {
                System.out.println("The offset of the produced message record is: " + metadata.offset());
            }
        });
    }

    @StreamListener(topics = {"ptjc95jg-responses"})
    public void onMessage(ConsumerRecord<String, String> record) {
        String id = record.key();
        String labels = record.value();

        try (
            Connection con = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
            Statement stmt = con.createStatement();
        ) {
            stmt.executeUpdate("UPDATE images SET labels = '" + labels + "' WHERE id = " + id);
        }
        catch (SQLException e) {
            System.err.println(e);
        }
    }
}
