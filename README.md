# Event Sourcing Tutorial Project

This project is a tutorial for showing how to implement Event Sourcing + CQRS application based on
DDD.

Language, Frameworks and Libraries for this project are:

* Kotlin 1.6.21
* SpringBoot 2.7.3
    * spring-starter-web
    * spring-data-jdbc
    * etc
* mysql-connector-java

---

## Starting up

1. If not installed, install docker.
2. Execute command below to start up Mysql docker container instance.

```shell
docker compose up -d
```

3. After Mysql booted, execute command below to start up SpringBoot Tutorial Application

```shell
./gradlew bootrun
```

4. Run Intellij Http Requests. Http Request files are in the [Test](src/test/http) directory.
    * Run Create Requests first. The other requests need UUID of the created Entity.

---

## Domain Entities

* Client represents a business partner for our company. A Client has more than zero Projects.
* Department represents a department of our company. A Department has more than zero Employee.
* Employee represents an employee for our company. An employee can be:
    * assigned to only one Department
    * assigned to more than zero Projects.

## Requirements

* CRUD for Client
* CRUD for Department
* CRUD for Employee
* All deletes should be soft delete

* An Employee can be assigned to only one Department. But A Department can have no more than 8 team
  members.
* An Employee can be unassigned from the Department that he/she has been assigned. But if the
  Department have only one team member, unassignment should be canceled.

* An Employee can be assigned to more than one Project. And the client that has the project can
  approve/veto the assignment.
* An Employee can be unassigned from the Project that he/she has been assigned. And the client that
  has the project can approve/veto the unassignment.

---

## Event Sourcing

Lifecycle methods for Entities should register domain event that represents current change. Every
Aggregate Root Entity should
extend [DomainAggregateRoot](src/main/kotlin/com/bory/eventsourcingtutorial/core/domain/DomainAggregateRoot.kt)
abstract class for this. For example check
the [Client](src/main/kotlin/com/bory/eventsourcingtutorial/client/domain/Client.kt)
entity file. This entity's lifecycle methods(such as constructor, update, delete, and so on)register
domain event like this.

```kotlin
constructor(uuid: String, command: CreateClientCommand) : this(
    uuid = uuid,
    name = command.client.name,
    phoneNumber = command.client.phoneNumber,
    address = command.client.address,
    projects = command.client.projects.map {
        Project(
            name = it.name,
            description = it.description,
            clientUuid = uuid
        )
    }
) {
    registerEvent(ClientCreatedEvent(this)) // <-- HERE
}

fun updateWith(updating: Client) = this.apply {
    name = updating.name
    address = updating.address
    phoneNumber = updating.phoneNumber

    registerEvent(ClientUpdatedEvent(this)) // <-- HERE
}
```

The [CoreEventListener](src/main/kotlin/com/bory/eventsourcingtutorial/core/infrastructure/eventlistener/CoreEventListener.kt)
listens all AbstractCustomEvent events and save it to event_source table using
EventSourceRepository.

## Domain Projector

Every Domain Project should
implement [AggregateRootProjector](src/main/kotlin/com/bory/eventsourcingtutorial/core/domain/AggregateRootProjector.kt)
. AggregateRootProjector interface has two abstract methods and one default method.

```kotlin
interface AggregateRootProjector<T : DomainAggregateRoot> {
    fun initialLoad(eventSource: EventSource): T
    fun eventCases(): Map<Class<out Any>, (T, EventSource) -> T>

    fun project(eventSources: List<EventSource>): T {
        // ... omitted for brevity
    }
    // ... omitted for brevity
}
```

* initialLoad(EventSource) method loads and creates an entity from the first row of eventsource
  list.
* eventCases() method is for mapping event and event handler function type of (T, EventSource) -> T.
* project(List<EventSource>) method is for projecting an entity for eventsource list. To use this
  method, you should load eventsource list for a specific aggregateId from event_source table. You
  can see the example of entity projection in
  the [ClientQueryController](src/main/kotlin/com/bory/eventsourcingtutorial/client/infrastructure/web/query/ClientQueryController.kt)
  .

```kotlin
@GetMapping("/{aggregateId}")
fun projectClient(@PathVariable("aggregateId") aggregateId: String): ClientDto {
    // loads all event sources by aggregateId 
    val eventSources = eventSourceRepository.findByAggregateIdOrderByCreatedAt(aggregateId)
    if (eventSources.isEmpty()) throw NoSuchClientException("EventSource for AggregateId[$aggregateId] not found")

    // project Client entity with ClientProjector
    return clientProjector.project(eventSources).toDto()
}
```

---

## CQRS

In fact, there is no heavy CQRS features in this project. There are just an event_source table and
tables for domain entity classes.

You can check [mysql-init.sq](docker/initdb.d/mysql-init.sql) file for entity relationship.
