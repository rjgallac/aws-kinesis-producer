package com.example.demo.config;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.PutRecordResult;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service
public class KinesisService {
    public static final String SAMPLE_APPLICATION_STREAM_NAME = "test-stream";

    @Autowired
    private AmazonKinesis amazonKinesis;

    @PostConstruct
    public void setupKinesis() throws InterruptedException {

        while (true) {
            long createTime = System.currentTimeMillis();
            PutRecordRequest putRecordRequest = new PutRecordRequest();
            putRecordRequest.setStreamName(SAMPLE_APPLICATION_STREAM_NAME);
            putRecordRequest.setData(ByteBuffer.wrap(String.format("testData-%d", createTime).getBytes()));
            putRecordRequest.setPartitionKey(String.format("partitionKey-%d", createTime));
            PutRecordResult putRecordResult = amazonKinesis.putRecord(putRecordRequest);
            Thread.sleep(500L);
            System.out.printf("Successfully put record, partition key : %s, ShardID : %s, SequenceNumber : %s.\n",
                    putRecordRequest.getPartitionKey(),
                    putRecordResult.getShardId(),
                    putRecordResult.getSequenceNumber());
        }
    }

}
