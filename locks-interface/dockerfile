# Use the official Clojure image as base
FROM clojure:openjdk-11-lein

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy the project files into the container
COPY . .

# Expose port 3000
EXPOSE 3000

# Run Leiningen to build and start the app
CMD ["lein", "run"]
