# hse-payord

## API Docs

**[Postman Collection](https://web.postman.co/workspace/d3b4d30f-3bd9-4ad3-ae0c-87535d10ed59)**

---

## Launch

```bash
cd docker
docker compose up
```
You can test the application after all containers are up.

---

## Features

### Order Service

- View list of orders for a user
- View individual order status
- Create new order (async payment processing, WS)
- Initiate payment (Transactional Outbox)

### Payment Service

- Create a user account
- Deposit
- Check balance
- Asynchronous payment processing (Transactional Inbox and Outbox)
- Atomic balance updates (CAS)

### Common

- All messages delivered via RabbitMQ
- Traefik as API Gateway
- Containerized in Docker
- Frontend client for simulation
- WebSocket for real-time order status updates

---

## Create Order flow

```mermaid
sequenceDiagram
    participant Client
    participant Gateway as API Gateway (WebSocket)
    participant Orders as Orders Service
    participant Outbox as Orders Outbox
    participant RabbitMQ
    participant Inbox as Payments Inbox
    participant Payments as Payments Service
    participant DB as Payments DB
    participant PaymentsOutbox as Payments Outbox
    participant OrdersWS as Orders WS Producer

    Client ->> Gateway: WebSocket connect
    Client ->> Gateway: WS: createOrder
    Gateway ->> Orders: Create Order (async)
    Orders ->> Outbox: Save order_created event
    Outbox ->> RabbitMQ: Publish order_created

    RabbitMQ ->> Inbox: Consume order_created
    Inbox ->> Payments: Process payment
    Payments ->> DB: Update balance
    Payments ->> PaymentsOutbox: Save payment_processed event
    PaymentsOutbox ->> RabbitMQ: Publish payment_processed

    RabbitMQ ->> OrdersWS: Consume payment_processed
    OrdersWS ->> Gateway: WS push with updated order status
    Gateway ->> Client: WS: order status updated
```

---

## Dashboards and Interfaces

- **Web UI**: [http://localhost:3000](http://localhost:3000)
- **RabbitMQ UI**: [http://localhost:15672](http://localhost:15672) (creds: admin/secret)
- **Traefik Dashboard**: [http://localhost:8080](http://localhost:8080)
