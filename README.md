# LAURA Context/Situation Broker

For testing purposes a version of this version is available in [https://lcb.multicast.vix.br](https://lcb.multicast.vix.br)

## Context Broker

#### `POST` /entities

Register a new entity in the broker.

*request* (`/entities`)
```json
{
  "kind": "Person",
  "descriptor": "Dr. Mary Sue",
  "attributes": {
    "name": "Mary Sue",
    "email": "marysue@laura.com",
    "role": "Doctor"
  }
}
```
*response*
```json
{
  "id": 1,  
  "kind": "Person",
  "descriptor": "Dr. Mary Sue",
  "attributes": {
    "name": "Mary Sue",
    "email": "marysue@laura.com",
    "role": "Doctor"
  }
}
```
------
#### **`POST`** /entities/`{entityId}`/contexts

Register an intrinsic context for an entity.

*request* (`/entities/1/contexts`)
```json
{
  "type": "intrinsic",
  "kind": "Geolocation"
}
```
*response*
```json
{
  "id": 1,
  "type": "intrinsic",
  "kind": "Geolocation"
}
```

------

##### **`POST`** /entities/`{entityId}`/contexts/`{contextId}`/values

Publish a new value for a entity's context (intrinsic).

*request* (`/entities/1/contexts/1/values`)
```json
{
    "timestamp": "2018-10-29T00:00:00.000Z",
    "entries": [
      {
        "latitude": -20.3582821,
        "longitude": -20.3582821
      }
    ]
}
```
*response*
```json
{
    "id": 1,
    "timestamp": "2018-10-29T00:00:00.000Z",
    "entries": [
      {
        "latitude": -20.3582821,
        "longitude": -20.3582821
      }
    ]
}
```
------
#### `GET` /entities

Retrieve all entities in the broker.

*response*
```json
[
 {
  "kind": "Person",
  "descriptor": "Dr. Mary Sue",
  "contexts": [
    {
      "id": 14232,
      "type": "intrinsic",
      "kind": "Geolocation",
      "value": {
        "timestamp": "2018-10-29T00:00:00.000Z",
        "entries": [
          {
            "latitude": -20.3582821,
            "longitude": -20.3582821
          }
        ]
      }
    },
    {
      "id": 1234,
      "type": "relational",
      "kind": "Observation",
      "parts": {
        "observer": "232342",
        "observed": "332121"
      },
      "value": true
    }
  ],
  "attributes": {
    "name": "Mary Sue",
    "email": "marysue@laura.com",
    "role": "Doctor"
  }
 }
]
```

------
#### **`POST`** /relations?role={entityId}&role={entityId}...

Register a **relational context** in the broker. The query params specifies role-id pairs. 
For example `observer=1&observed=5` defines a relation between an entity of id `1` as the **observer** part 
and another entity of `id` 5 as the **observed** part.

*request* (`/relations?observer=1&observed=5`)



```json
{
  "type": "relational",
  "kind": "Observation",
  "descriptor": "Dr. Mary Sue's observation of product number 0005"
}
```
*response*
```json
{
  "type": "relational",
  "kind": "Observation",
  "descriptor": "Dr. Mary Sue's observation of product number 0005",
  "parts": {
    "observer": 1,
    "observed": 5
  }
}
```

------

##### **`POST`** /relations/`{relationId}`/values

Publish a new value for a **relational context**.

*request* (`/relations/2/values`)
```json
{
    "timestamp": "2018-10-29T00:00:00.000Z",
    "entries": [
      {
        "latitude": -20.3582821,
        "longitude": -20.3582821
      }
    ]
}
```
*response*
```json
{
    "id": 23,
    "timestamp": "2018-10-29T00:00:00.000Z",
    "entries": [
      {
        "latitude": -20.3582821,
        "longitude": -20.3582821
      }
    ]
}
```

------
## Situation Broker

This session endpoints are intended for consumers from the application layer. Developers from this layer can use the above endpoints to deal with detected situations.

### Websocket

------
#### `GET` /ws/situations

Subscribe to every event for activation/deactivation of situations detected by the engine.

*sample*
```json
{
  "type" : "ACTIVATION",
  "timestamp" : 1541978996090,
  "situation" : {
    "id" : 1,
    "type" : "scene.Fever",
    "active" : true,
    "started" : 1541978996090,
    "participations" : {
      "febrile" : {
        "id" : 1,
        "kind" : "Person",
        "descriptor" : "Dr. Mary Sue",
        "attributes" : {
          "age" : 35.0
        },
        "contexts" : {
          "Temperature" : {
            "type" : "intrinsic",
            "id" : 1,
            "descriptor" : "Dr. Mary Sue's temperature",
            "kind" : "Temperature",
            "value" : {
              "timestamp" : 1541066766,
              "entries" : {
                "value" : 39.0
              }
            },
            "bearer" : 1
          }
        }
      }
    }
  }
}

```

------
#### `GET` /situations/{situationId}

Get a specific situation instance.

*response*
```json
{
  "id" : 1,
  "type" : "scene.Fever",
  "active" : true,
  "started" : 1541978996090,
  "participations" : {
    "febrile" : {
      "id" : 1,
      "kind" : "Person",
      "descriptor" : "Dr. Mary Sue",
      "attributes" : {
        "age" : 35.0
      },
      "contexts" : {
        "Temperature" : {
          "type" : "intrinsic",
          "id" : 1,
          "descriptor" : "Dr. Mary Sue's temperature",
          "kind" : "Temperature",
          "value" : {
            "timestamp" : 1541066766,
            "entries" : {
              "value" : 39.0
            }
          },
          "bearer" : 1
        }
      }
    }
  }
}


```
