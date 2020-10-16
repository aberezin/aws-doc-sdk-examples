//snippet-sourcedescription:[CreateTableWaiter.java demonstrates how to create an Amazon DynamoDB table using a waiter]
//snippet-keyword:[SDK for Java 2.0]
//snippet-keyword:[Code Sample]
//snippet-service:[Amazon DynamoDB]
//snippet-sourcetype:[full-example]
//snippet-sourcedate:[10/15/2020]
//snippet-sourceauthor:[scmacdon - AWS]

/*
   Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
   This file is licensed under the Apache License, Version 2.0 (the "License").
   You may not use this file except in compliance with the License. A copy of
   the License is located at
    http://aws.amazon.com/apache2.0/
   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
   CONDITIONS OF ANY KIND, either express or implied. See the License for the
   specific language governing permissions and limitations under the License.
*/

package com.example.dynamodb;

// snippet-start:[dynamodb.java2.create_tablewaiter.import]
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;
// snippet-end:[dynamodb.java2.create_tablewaiter.import]

/**
 * Creates an AWS DynamoDB table.
 *
 * This code expects that you have AWS credentials set up, as described here:
 * http://docs.aws.amazon.com/java-sdk/latest/developer-guide/setup-credentials.html
 */
public class CreateTableWaiter {

    public static void main(String[] args) {
        final String USAGE = "\n" +
                "Usage:\n" +
                "    CreateTable <table> <key >\n\n" +
                "Where:\n" +
                "    table - the table to create (i.e., Music3)\n\n" +
                "    key   - the key for the table (i.e., Artist)\n" +
                "Example:\n" +
                "    Music3 Artist \n";

        if (args.length < 2) {
            System.out.println(USAGE);
            System.exit(1);
        }

        /* Read the name from command args */
        String tableName = args[0];
        String key = args[1];

        System.out.format(
                "Creating table \"%s\" with a simple primary key: \"Name\".\n",
                tableName);

        // Create the DynamoDbClient object
        Region region = Region.US_EAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(region)
                .build();

        // Create a DynamoDbWaiter object
        DynamoDbWaiter dbWaiter = ddb.waiter();

        String result = createTable(ddb, dbWaiter, tableName, key);
        System.out.println("New table is "+result);
    }

    // snippet-start:[dynamodb.java2.create_tablewaiter.main]
    public static String createTable(DynamoDbClient ddb, DynamoDbWaiter dbWaiter, String tableName, String key) {

        // Create the CreateTableRequest object
        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName(key)
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .keySchema(KeySchemaElement.builder()
                        .attributeName(key)
                        .keyType(KeyType.HASH)
                        .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(new Long(10))
                        .writeCapacityUnits(new Long(10))
                        .build())
                .tableName(tableName)
                .build();

        String newTable ="";
        try {
            CreateTableResponse response = ddb.createTable(request);

            // Create a DescribeTableRequest object required for waiter functionality
            DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();

            // wait until the table is created
            WaiterResponse<DescribeTableResponse> waiterResponse =  dbWaiter.waitUntilTableExists(r -> r.tableName(tableName));

            // print out the matched response with a tableStatus of ACTIVE
            waiterResponse.matched().response().ifPresent(System.out::println);

            newTable = response.tableDescription().tableName();
            return newTable;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        // snippet-end:[dynamodb.java2.create_tablewaiter.main]
        return "";
    }
}
