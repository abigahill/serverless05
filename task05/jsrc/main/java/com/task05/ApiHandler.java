package com.task05;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.resources.DependsOn;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.ResourceType;
import com.syndicate.deployment.model.DeploymentRuntime;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.UUID;

import com.task05.model.ApiResponse;
import com.task05.model.ApiRequest;

@LambdaHandler(lambdaName = "api_handler",
	roleName = "api_handler-role",
	isPublishVersion = false,
	runtime = DeploymentRuntime.JAVA11,
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@DependsOn(
	name = "Events",
	resourceType = ResourceType.DYNAMODB_TABLE
)
@EnvironmentVariables(value = {
	@EnvironmentVariable(key = "region", value = "${region}"),
	@EnvironmentVariable(key = "target_table", value = "${target_table}")
})
public class ApiHandler implements RequestHandler<ApiRequest, ApiResponse> {
	//private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
	private final AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
			.withRegion(System.getenv("region")).build();
	private final DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);

	public ApiResponse handleRequest(ApiRequest request, Context context) {
		context.getLogger().log("Received request: " + request.toString());

		Table dbTable = dynamoDB.getTable(System.getenv("target_table"));
		Item item = buildItem(request);
		context.getLogger().log("Saving item: " + item);

		PutItemOutcome putItemOutcome = dbTable.putItem(item);
		context.getLogger().log("putItemOutcome: " + putItemOutcome.toString());

		ApiResponse response = new ApiResponse();
		response.setStatusCode(201);
		response.setEvent(item.toString());
		return response;
	}

	private Item buildItem(ApiRequest request) {
		return new Item()
				.withPrimaryKey("id", UUID.randomUUID().toString())
				.withInt("principalId", request.getPrincipalId())
				.withString("createdAt", new DateTime().toString())
				.withMap("body", request.getContent());
	}
}
