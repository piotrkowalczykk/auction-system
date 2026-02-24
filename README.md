<h1 align="center">Auction System</h1>
<h4 align="center">Event-Driven Microservices Architecture with Real-Time Bidding</h4>
<div  align="center">  
<img  src="https://img.shields.io/badge/architecture-microservices-blue">  
<img  src="https://img.shields.io/badge/pattern-event--driven-orange">  
<img  src="https://img.shields.io/badge/Apache_Kafka-event_streaming-black?logo=apachekafka">  
<img  src="https://img.shields.io/badge/Redis-TTL_cache-red?logo=redis">  
<img  src="https://img.shields.io/badge/API_Gateway-REST→gRPC-green">  
</div>
<br>
<img src="https://github.com/piotrkowalczykk/auction-system/blob/2cb11645b988f5e851d22bb30466bd09ef90ebe5/architecture.png?raw=true">

## Overview
Auction System is a distributed, event-driven microservices platform that allows users to:
-   Register and authenticate using JWT (access + refresh tokens)
-   Verify email and reset password via asynchronous email notifications
-   Manage user profile
-   Create auctions (bidding, buy-now, or hybrid)
-   Place bids in real time
-   Participate in live auction channels
-   Automatically finish auctions using Redis TTL
-   Receive email notification when auction ends
    

The system demonstrates production-inspired architectural patterns including:

-   Microservices separation of concerns
-   gRPC-based internal communication
-   Kafka event-driven workflows
-   Redis-based time-based orchestration
-   Real-time bidirectional streaming
-   JWT security model
-   Clean exception handling with mappers

## Architecture Overview  
  
The system follows a microservices architecture with:  
  
- API Gateway as a REST entry point  
- Internal communication via gRPC  
- Asynchronous workflows powered by Kafka  
- Time-based auction expiration handled via Redis TTL  
- Shared Protobuf contracts for service communication

## Services

<h3>Auth Service</h3>

Responsible for:
-   User registration
-   Login
-   JWT access & refresh tokens
-   Email verification
-   Password reset
    

Events published:
-   `UserCreatedEvent`
-   `EmailVerificationEvent`
-   `PasswordResetEvent`
    
<h3>User Service</h3>

Responsible for:
-   Creating user profile after email verification
-   Managing user profile data
    
Triggered by:
-   `UserCreatedEvent`

<h3>Auction Service</h3>

Responsible for:
-   Creating auctions
-   Persisting auction data
-   Updating auction status
-   Publishing `AuctionCreatedEvent`
-   Handling `AuctionEndedEvent`
    
<h3>Bidding Service</h3>

Responsible for:
-   Placing bids
-   Buy-now functionality
-   Storing bid history
-   Managing auction state in Redis
-   Broadcasting live auction updates
-   Publishing `AuctionEndedEvent`
    
<h3>Notification Service</h3>

Responsible for:
-   Sending verification emails
-   Sending password reset emails
-   Sending auction winner notifications
    
Triggered by:

-   `EmailVerificationEvent`
-   `PasswordResetEvent`
-   `AuctionEndedEvent`

## Real-Time Live Auction
The system includes a real-time Live Auction channel, implemented using bidirectional gRPC Streaming

### RPC Definition
```
rpc LiveAuction(stream AuctionClientEvent)  
 returns (stream AuctionServerEvent);
```

### How It Works

1.  User connects to auction channel
2.  User sends `JoinAuction` event
3.  Server stores active stream session
4.  When someone places a bid:
    -   Auction is validated
    -   Redis is updated
    -   Broadcast sent to all connected users
5.  When auction ends:
    -   All users receive `AuctionEnded` event

## Event-Driven Auction Lifecycle

Auction lifecycle is fully event-driven:

### Auction Creation Flow

1.  User creates auction
2.  Auction saved in DB
3.  `AuctionCreatedEvent` published
4.  BiddingService initializes Redis record
5.  TTL set to auction end time
   
### Auction Expiration Flow

1.  Redis key expires (TTL reached)
2.  Redis expiration listener triggers
3.  `AuctionEndedEvent` published
4.  AuctionService updates status in DB
5.  NotificationService sends winner email

## Security

-   JWT access token
-   Refresh token rotation
-   Stateless authentication
-   REST exposed only through API Gateway
- Contract-based internal communication via gRPC and Protocol Buffers


## Technologies  
  
<p  align="center">  
<img src="https://img.shields.io/badge/Java-ff9100?style=for-the-badge&logo=coffeescript&labelColor=black" alt="Java">
<img  src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white&labelColor=black"  alt="Spring Boot">  
<img  src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white&labelColor=black"  alt="Spring Security">  
<img  src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white&labelColor=black"  alt="Hibernate">  
<img  src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white&labelColor=black"  alt="PostgreSQL">  
<img  src="https://img.shields.io/badge/gRPC-244c5a?style=for-the-badge&logo=grpc&logoColor=white&labelColor=black"  alt="gRPC">  
<img  src="https://img.shields.io/badge/Protocol_Buffers-3c78d8?style=for-the-badge&logo=protocols&logoColor=white&labelColor=black"  alt="Protocol Buffers">  
<img  src="https://img.shields.io/badge/Apache_Kafka-000000?style=for-the-badge&logo=apachekafka&logoColor=white&labelColor=black"  alt="Apache Kafka">  
<img  src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white&labelColor=black"  alt="Redis">  
<img  src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white&labelColor=black"  alt="Docker">   
<img  src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white&labelColor=black"  alt="JWT">  
<img  src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white&labelColor=black"  alt="Maven">  
<img  src="https://img.shields.io/badge/Lombok-bc060c?style=for-the-badge&logo=lombok&logoColor=white&labelColor=black"  alt="Lombok">  
<img  src="https://img.shields.io/badge/MailHog-009688?style=for-the-badge&logo=maildotru&logoColor=white&labelColor=black"  alt="MailHog"> 
</p>

## Running the Project

#### Requirements
-   Docker
-   Docker Compose
-   Java 21
    
#### Start Infrastructure
```
docker-compose up -d
```
#### Start Services

Each service can be started individually:
```
mvn spring-boot:run
```

## Project Status  
  
This repository represents a completed architectural exploration of a real-time auction platform built using event-driven microservices.  
  
The system showcases distributed communication patterns, bidirectional streaming, and JWT-based authentication within a production-inspired architecture.  
  
No further feature development is planned.

## License
Available under the [MIT license](https://github.com/piotrkowalczykk/auction-system/blob/861ffbccdbb4734496f31e6bbcb621bf45794477/LICENSE)
