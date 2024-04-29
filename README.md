**Important: Don't forget to update the [Candidate README](#candidate-readme) section**

Real-time Transaction Challenge
===============================
## Overview
Welcome to Current's take-home technical assessment for backend engineers! We appreciate you taking the time to complete this, and we're excited to see what you come up with.

Today, you will be building a small but critical component of Current's core banking enging: real-time balance calculation through [event-sourcing](https://martinfowler.com/eaaDev/EventSourcing.html).

## Schema
The [included service.yml](service.yml) is the OpenAPI 3.0 schema to a service we would like you to create and host. 

## Details
The service accepts two types of transactions:
1) Loads: Add money to a user (credit)

2) Authorizations: Conditionally remove money from a user (debit)

Every load or authorization PUT should return the updated balance following the transaction. Authorization declines should be saved, even if they do not impact balance calculation.

You may use any technologies to support the service. We do not expect you to use a persistent store (you can you in-memory object), but you can if you want. We should be able to bootstrap your project locally to test.

## Expectations
We are looking for attention in the following areas:
1) Do you accept all requests supported by the schema, in the format described?

2) Do your responses conform to the prescribed schema?

3) Does the authorizations endpoint work as documented in the schema?

4) Do you have unit and integrations test on the functionality?

# Candidate README
## Bootstrap instructions
*Replace this: To run this server locally, do the following:*
The Application can be run using the "jar" file attached in the GitHub Repository
1) use the following command:
    ```
    java -jar CodeScreen_fvv8fyxl-1.0.0.jar
    ```
    The Application is running on the "9000" port  (make sure that you do not have any other service running on this port)
    use the following things:

    ```
    http://localhost:9000/load/messageid-1
    Body:
    {
        "messageId": "messageid-1",
        "userId": "user-3",
        "transactionAmount": {
            "amount": "10.23",
            "currency": "USD",
            "debitOrCredit": "CREDIT"
        }
    }

    http://localhost:9000/authorization/messageid-1
    Body:
    {
        "messageId": "messageid-1",
        "userId": "user-3",
        "transactionAmount": {
            "amount": "10.23",
            "currency": "USD",
            "debitOrCredit": "DEBIT"
        }
    }
    ```
2) For running the test cases, there is a requirement for maven in the system. use the following command to run all the test cases:
  ```
  mvn clean test
  ```

## Design considerations
*Replace this: I decided to build X for Y reasons.*
1) I opted to employ the ```Adapter Design Pattern``` for our Service. This approach allows us to accommodate future changes seamlessly. If there's a requirement change in the future necessitating the creation of a new service class, we simply need to add another ImplementationService and ensure the correct class is called using runtime binding. Consequently, we can achieve this change with minimal modifications in other classes.
2) I have chosen to utilize the ```H2 in-memory database``` for the data layer. This choice was made due to its ease of use and minimal setup requirements, making it straightforward for anyone to run and test the services and controllers. Additionally, the flexibility of H2 allows for easy replacement with other databases with minimal changes, providing versatility in deployment options.
3) Implemented a ````Global Exception Handler``` to seamlessly manage exceptions occurring within the application. This allows for centralized error handling, ensuring that any exceptions are caught and processed consistently across the application. By leveraging the Global Exception Handler, we can customize error responses based on the type of exception encountered, facilitating clearer communication with clients and maintaining adherence to HTTP status code standards.



## Bonus: Deployment considerations
*Replace this: If I were to deploy this, I would host it in this way with these technologies.*

For deployment, leveraging cloud technologies like AWS offers scalability, reliability, and ease of management. Here's how I would host this application using AWS services:
Compute: 
1) Amazon EC2 or AWS Lambda:
  For traditional deployment, I'd opt for Amazon EC2 instances. They offer full control over the server environment and are suitable for Spring Boot applications.
  Alternatively, if the application is designed for serverless architecture, AWS Lambda can be used. It automatically scales based on incoming traffic and eliminates the need to manage servers.
2) Database: Amazon RDS (Relational Database Service) or Dynamo DB
  For structured data storage, Amazon RDS offers managed database services supporting SQL databases like MySQL, PostgreSQL, or MariaDB. Utilizing Amazon RDS can enhance data retrieval performance and facilitate disaster recovery management. Additionally, DynamoDB provides a fully managed NoSQL database service, offering scalability, high availability, and low latency for applications requiring flexible and dynamic data models.
3) Storage: Amazon S3 (Simple Storage Service):
  Amazon S3 can be used to store static assets, such as weekly reports on Transaction. It provides scalable object storage with high availability and durability.
4) Use a CDN network like CloudFront:
  Implementing a CDN like CloudFront optimizes the global accessibility of the application, enhancing user experience by reducing latency and improving content delivery speed across different regions. This ensures that users worldwide can access the application efficiently, leading to a smoother and more responsive user experience.



## ASCII art
*Optional but suggested, replace this:*

Is something supposed to be done here?
```
                                                                                
                   @@@@@@@@@@@@@@                                               
               @@@@@@@@@@@@@@@@@@@@@                                            
             @@@@@@@@@@@@@@@@@@@@@@@@@@                                         
          @@@@@@@@@@@@@@@@@@@@@@@@                                  @@@@        
        @@@@@@@@@@@@@@@@@@@@@      @@@@@@                        @@@@@@@@@      
     @@@@@@@@@@@@@@@@@@@@@    @@@@@@@@@@@@@@@                 .@@@@@@@@@@@@@@   
   @@@@@@@@@@@@@@@@@@@@   @@@@@@@@@@@@@@@@@@@@@           @@@@@@@@@@@@@@@@@@@@@ 
 @@@@@@@@@@@@@@@@@@@    @@@@@@@@@@@@@@@@@@@@@@@@@@   @@@@@@@@@@@@@@@@@@@@@@@@@@ 
    @@@@@@@@@@@@@@               @@@@@@@@@@@@@@@@@@@    @@@@@@@@@@@@@@@@@@@@    
      @@@@@@@@@@                     @@@@@@@@@@@@@@@@@@    @@@@@@@@@@@@@@       
         @@@@                          @@@@@@@@@@@@@@@@@@@@                     
                                          @@@@@@@@@@@@@@@@@@@@@@@@@@@@@         
                                            @@@@@@@@@@@@@@@@@@@@@@@@            
                                               @@@@@@@@@@@@@@@@@@               
                                                    @@@@@@@@                    
```
## License

At CodeScreen, we strongly value the integrity and privacy of our assessments. As a result, this repository is under exclusive copyright, which means you **do not** have permission to share your solution to this test publicly (i.e., inside a public GitHub/GitLab repo, on Reddit, etc.). <br>

## Submitting your solution

Please push your changes to the `main branch` of this repository. You can push one or more commits. <br>

Once you are finished with the task, please click the `Submit Solution` link on <a href="https://app.codescreen.com/candidate/0fede799-4f7b-4aa4-8595-7c06190b9cfc" target="_blank">this screen</a>.
