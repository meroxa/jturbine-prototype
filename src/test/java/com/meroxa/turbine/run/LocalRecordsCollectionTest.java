package com.meroxa.turbine.run;

import com.google.protobuf.ByteString;
import com.meroxa.turbine.TurbineRecord;
import com.meroxa.turbine.proto.Record;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalRecordsCollectionTest {

    @Test
    void test() {
        Record rec = Record.newBuilder()
            .setValue(ByteString.copyFromUtf8(
                "{\"payload\":{\"after\":\"eyJjYXRlZ29yeSI6ImNhbXBpbmciLCJjdXN0b21lcl9lbWFpbCI6InVzZXJjQGV4YW1wbGUuY29tIiwiaWQiOjk1ODI3MjYsInByb2R1Y3RfaWQiOjIwMzAwMCwicHJvZHVjdF9uYW1lIjoiWmVwaHlyIDI1IFJlY3ljbGVkIFNsZWVwaW5nIEJhZyAtIEtpZCIsInByb2R1Y3RfdHlwZSI6InNsZWVwaW5nLWJhZyIsInNoaXBwaW5nX2FkZHJlc3MiOiI0MyBTdW5ueXNsb3BlIFN0cmVldCBQb3J0IFJpY2hleSwgRkwgMzQ2NjgiLCJzdG9jayI6dHJ1ZX0=\",\"before\":null,\"op\":\"r\",\"source\":{\"connector\":\"postgresql\",\"db\":\"database\",\"lsn\":537810437656,\"name\":\"source_name\",\"schema\":\"public\",\"snapshot\":\"true\",\"table\":\"collection_name\",\"ts_ms\":1644362356740,\"txId\":8680,\"version\":\"1.2.5.Final\",\"xmin\":null},\"transaction\":null,\"ts_ms\":1644362356743},\"schema\":{\"fields\":[{\"field\":\"before\",\"fields\":[{\"field\":\"id\",\"optional\":false,\"type\":\"int32\"},{\"field\":\"category\",\"optional\":false,\"type\":\"string\"},{\"field\":\"product_type\",\"optional\":false,\"type\":\"string\"},{\"field\":\"product_name\",\"optional\":false,\"type\":\"string\"},{\"field\":\"stock\",\"optional\":false,\"type\":\"boolean\"},{\"field\":\"product_id\",\"optional\":false,\"type\":\"int32\"},{\"field\":\"shipping_address\",\"optional\":false,\"type\":\"string\"},{\"field\":\"customer_email\",\"optional\":false,\"type\":\"varchar\"}],\"name\":\"resource.public.collection_name.Value\",\"optional\":true,\"type\":\"struct\"},{\"field\":\"after\",\"fields\":[{\"field\":\"id\",\"optional\":false,\"type\":\"int32\"},{\"field\":\"category\",\"optional\":false,\"type\":\"string\"},{\"field\":\"product_type\",\"optional\":false,\"type\":\"string\"},{\"field\":\"product_name\",\"optional\":false,\"type\":\"string\"},{\"field\":\"stock\",\"optional\":false,\"type\":\"boolean\"},{\"field\":\"product_id\",\"optional\":false,\"type\":\"int32\"},{\"field\":\"shipping_address\",\"optional\":false,\"type\":\"string\"},{\"field\":\"customer_email\",\"optional\":false,\"type\":\"varchar\"}],\"name\":\"resource.public.collection_name.Value\",\"optional\":true,\"type\":\"struct\"},{\"field\":\"source\",\"fields\":[{\"field\":\"version\",\"optional\":false,\"type\":\"string\"},{\"field\":\"connector\",\"optional\":false,\"type\":\"string\"},{\"field\":\"name\",\"optional\":false,\"type\":\"string\"},{\"field\":\"ts_ms\",\"optional\":false,\"type\":\"int64\"},{\"default\":\"false\",\"field\":\"snapshot\",\"name\":\"io.debezium.data.Enum\",\"optional\":true,\"parameters\":{\"allowed\":\"true,last,false\"},\"type\":\"string\",\"version\":1},{\"field\":\"db\",\"optional\":false,\"type\":\"string\"},{\"field\":\"schema\",\"optional\":false,\"type\":\"string\"},{\"field\":\"table\",\"optional\":false,\"type\":\"string\"},{\"field\":\"txId\",\"optional\":true,\"type\":\"int64\"},{\"field\":\"lsn\",\"optional\":true,\"type\":\"int64\"},{\"field\":\"xmin\",\"optional\":true,\"type\":\"int64\"}],\"name\":\"io.debezium.connector.postgresql.Source\",\"optional\":false,\"type\":\"struct\"},{\"field\":\"op\",\"optional\":false,\"type\":\"string\"},{\"field\":\"ts_ms\",\"optional\":true,\"type\":\"int64\"},{\"field\":\"transaction\",\"fields\":[{\"field\":\"id\",\"optional\":false,\"type\":\"string\"},{\"field\":\"total_order\",\"optional\":false,\"type\":\"int64\"},{\"field\":\"data_collection_order\",\"optional\":false,\"type\":\"int64\"}],\"optional\":true,\"type\":\"struct\"}],\"name\":\"resource.public.collection_name.Envelope\",\"optional\":false,\"type\":\"struct\"}}"
            ))
            .build();

        TurbineRecord res = LocalRecordsCollection.toTurbineRecord(rec);
        Assertions.assertNotNull(res);
        assertNotEquals("", res.getPayload());
    }

}