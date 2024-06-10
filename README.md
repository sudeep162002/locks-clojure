<p align="center" width="100%">
 <img src="https://github.com/sudeep162002/locks-clojure/assets/80563363/0d44082d-cdd0-45ff-8fd5-818d955a0998" alt="Gemini_Generated_Image_kp91e9kp91e9kp91">
</p>
<!-- ![334918914-620e863f-7f29-423e-aff7-db7ccb7907d4-removebg-preview](https://github.com/sudeep162002/locks-clojure/assets/80563363/0d44082d-cdd0-45ff-8fd5-818d955a0998) -->

**Title:** Highly Available Distributed Lock System (Clojure)

**Description:**

This repository implements a robust, high-availability distributed lock system designed to prevent race conditions and ensure data consistency in Clojure microservice environments. It leverages the power of Docker for containerization, Redis for distributed locking, and MongoDB for data persistence.

**Key Features:**

- **Distributed Locking:** Employs Redis as a distributed locking mechanism to coordinate access to shared resources across multiple microservices, eliminating race conditions.
- **High Availability:** Adopts a distributed Redis architecture to ensure fault tolerance and minimize downtime.
- **MongoDB Integration:** Maintains consistency with MongoDB, a popular distributed database, for persistent data storage.
- **Dockerized:** Facilitates deployment and scaling in containerized environments.
- **Clojure-Friendly API:** Provides a clean and concise Clojure API for seamless integration with your microservices.




![1717586008971](https://github.com/sudeep162002/locks-clojure/assets/80563363/79739e5d-ccda-49a3-a167-a6041eed3af8)


**Getting Started:**

1. **Prerequisites:**
   - Docker ([https://www.docker.com/](https://www.docker.com/))
   - Clojure ([https://clojure.org/](https://clojure.org/))
   - Redis ([https://redis.io/](https://redis.io/))
   - MongoDB ([https://www.mongodb.com/](https://www.mongodb.com/))

2. **Clone the Repository:**

   ```bash
   git clone https://github.com/your-username/distributed-lock-system.git
   ```

3. **Build and Run the Docker Containers:**

   (Replace `<redis-port>` and `<mongodb-port>` with desired port numbers)

   ```bash
   docker-compose up 
   ```

4. **Usage Example (Clojure):**

   ```[clojure](http://localhost:3000/release-lock)```

   ```[clojure](http://localhost:3000/get-lock)```

   ```[clojure](http://localhost:3000/)```

**Configuration (Optional):**

- Adjust Redis and MongoDB configuration options in the `docker-compose.yml` file as needed.

**Contributing:**

We welcome contributions to improve this project! Please refer to the CONTRIBUTING guide for details.

**License:**

This project is licensed under the MIT License ([https://opensource.org/license/mit](https://opensource.org/license/mit)).

**Additional Notes:**

- Consider including a badge for continuous integration ([https://www.travis-ci.com/](https://www.travis-ci.com/)) or similar services to showcase build status.
- Provide more detailed usage examples or a tutorial for users unfamiliar with Clojure or distributed locking concepts.
- Thoroughly test and document your lock acquisition and release mechanisms to ensure correct behavior.
- Explore advanced features like leader election or distributed transactions based on your use case.

By following these guidelines, you can create a compelling and informative GitHub README that effectively promotes your distributed lock system in Clojure.

# todo 
1. if lock is already released then no need to release again 
