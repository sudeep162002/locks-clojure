
# Step 1: Create a custom network
docker network create locks-network

# Step 2: Run the Redis container
docker run --name redis-container --network locks-network -p 6379:6379 -d redis

# Step 3: Run the Clojure container
docker run --name clojure-locks --network locks-network -p 3000:3000 -v /D/web_development:/src -w /src -it clojure /bin/bash
