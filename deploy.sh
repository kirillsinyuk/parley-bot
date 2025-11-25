#!/bin/bash
set -e  # exit on any error

# Fail if required variables are missing
: "${ADMIN_CHAT_ID:?Environment variable ADMIN_CHAT_ID is required}"
: "${BOT_TOKEN:?Environment variable BOT_TOKEN is required}"
: "${OPENAI_API_KEY:?Environment variable OPENAI_API_KEY is required}"

CONTAINER_NAME="parley-bot"
IMAGE_NAME="sinyukkirill/parley-bot"
IMAGE_TAG="latest"

echo "Stopping old container if exists..."
docker stop $CONTAINER_NAME 2>/dev/null || true

echo "Removing old container if exists..."
docker rm $CONTAINER_NAME 2>/dev/null || true

echo "Removing old images..."
OLD_IMAGES=$(docker images --format '{{.ID}} {{.Repository}}:{{.Tag}}' | grep "$IMAGE_NAME" | awk '{print $1}')

if [ ! -z "$OLD_IMAGES" ]; then
  echo "$OLD_IMAGES" | xargs -r docker rmi
else
  echo "No old images found."
fi

echo "Pulling latest image..."
docker pull $IMAGE_NAME:$IMAGE_TAG

echo "Starting new container..."
docker run -d \
  --memory=650m \
  --name $CONTAINER_NAME \
  -p 8080:8080 \
  -v /var/log/myapp:/logs \
  -v /opt/parley/data:/data \
  -e ADMIN_CHAT_ID="$ADMIN_CHAT_ID" \
  -e BOT_TOKEN="$BOT_TOKEN" \
  -e OPENAI_API_KEY="$OPENAI_API_KEY" \
  $IMAGE_NAME:$IMAGE_TAG

echo "Done!"