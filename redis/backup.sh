#!/bin/bash

# Get instance ID from environment variable
INSTANCE_ID=${REDIS_INSTANCE_ID:-"missing_instance_id"}

# Check if environment variable is set
if [[ "$INSTANCE_ID" == "missing_instance_id" ]]; then
  echo "Error: REDIS_INSTANCE_ID environment variable not found."
  exit 1
fi

# MongoDB connection details
MONGO_HOST=${MONGO_HOST:-"mongo-container"}
MONGO_PORT=${MONGO_PORT:-"27017"}
MONGO_DATABASE=${MONGO_DATABASE:-"mydatabase"}
MONGO_COLLECTION=${MONGO_COLLECTION:-"redis_backups"}

# Redis connection details
REDIS_HOST=${REDIS_HOST:-"localhost"}
REDIS_PORT=${REDIS_PORT:-"6379"}

# Generate timestamp
TIMESTAMP=$(date +%Y-%m-%dT%H:%M:%SZ)

# Create filename for the dump
FILENAME="redis-${INSTANCE_ID}-${TIMESTAMP}.rdb"

# Backup Redis data
redis-cli -h "$REDIS_HOST" -p "$REDIS_PORT" SAVE

# Check if dump creation was successful
if [[ $? -ne 0 ]]; then
  echo "Error: Failed to save Redis data"
  exit 1
fi

# Fetch the Redis dump file
REDIS_DUMP_FILE="/data/dump.rdb"
if [[ ! -f "$REDIS_DUMP_FILE" ]]; then
  echo "Error: Redis dump file not found at '$REDIS_DUMP_FILE'"
  exit 1
fi

# Encode dump data
ENCODED_DATA=$(base64 -w 0 "$REDIS_DUMP_FILE")

# Construct MongoDB document
DOCUMENT="{ \"instance_id\": \"$INSTANCE_ID\", \"timestamp\": \"$TIMESTAMP\", \"data\": \"$ENCODED_DATA\" }"

# Send document to MongoDB using mongoimport
echo "$DOCUMENT" | mongoimport --host "$MONGO_HOST" --port "$MONGO_PORT" --db "$MONGO_DATABASE" --collection "$MONGO_COLLECTION" --jsonArray --upsert

# Check if MongoDB insert was successful
if [[ $? -ne 0 ]]; then
  echo "Error: Failed to insert backup data into MongoDB"
  exit 1
fi

echo "Successfully backed up Redis instance '$INSTANCE_ID' to MongoDB (timestamp: $TIMESTAMP)"
