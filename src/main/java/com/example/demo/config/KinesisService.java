package com.example.demo.config;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.amazonaws.services.kinesis.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

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
