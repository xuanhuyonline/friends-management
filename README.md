# The Friend Management RESTFUL API

### Project Features

* Build Api enpoints for Friend management project
* Write unit test for API endpoints

### Project Requirement

1. Java 11
2. Gradle 8.1
3. Install and run PostgreSQL on your localhost for storing data

### Test coverage
    Class: 94%
    Method: 87%
    Line: 92%

### How to use from this sample project

##### Clone the repository

```
git clone git@github.com:xuanhuyonline/friends-management.git
```

##### Run project

* Docker
```
./start.sh
```
* Or
```
./gradlew build
```

```
docker-compose up -d
```

##### Test api with postman

```
https://www.postman.com/downloads/
```

### RestApi Endpoints

* Create a friend connection :POST http://localhost:8080/api/v1/friends

 ````
  Example Request
  {
     "friends":
        [
         "nguyenvanthanh@gmail.com",
         "lexuanhuy2k1@gmail.com"       
        ] 
  }  
  Success Response Example
  {
    true
  }
  Error Response Example
  {
    "status": 400,
    "message": "Invalid email format"
  }
   ````

  -------------------------------------------------------------

* Retrieve the friends list for an email address :GET  http://localhost:8080/api/v1/users

````
 Params 
    * Key: email
    * Value: lexuanhuy2k1@gmail.com
 Success Response Example
 {
    "success": true,
    "friends": [
        "levanan@gmail.com",
        "nguyenvankhoa@gmail.com",
        "nguyenvanthanh@gmail.com"
    ],
    "count": 3
 }
 Error Response Example
 {
    "status": 400,
    "message": "Invalid email format"
 }
 ````

 -------------------------------------------------------------

* Retrieve the common friends list between two email addresses :
  GET  http://localhost:8080/api/v1/users/common

 ````
 Params 
    * Key: friends
    * Value: levanan@gmail.com,nguyenvankhoa@gmail.com
 Success Response Example
 {
    "success": true,
    "friends": [
        "lexuanhuy2k1@gmail.com"
    ],
    "count": 1
}
 Error Response Example
 {
    "status": 400,
    "message": "Invalid email format"
}
  ````

  -------------------------------------------------------------

* Create subscribe to updates from an email address :POST http://localhost:8080/api/v1/friends/subscription

 ````
  Example Request
  {
    "requestor": "lexuanhuy2k1@gmail.com",
    "target": "nguyenvanthanh@gmail.com"
  }
  Success Response Example
  {
    "success": true
  }
  Error Response Example
  {
    "status": 400,
    "message": "Subscription for updates already exists"
  }
````

  -------------------------------------------------------------

* Block updates from an email address:POST http://localhost:8080/api/v1/friends/block

````
  Example Request
  {
    "requestor": "lexuanhuy2k1@gmail.com",
    "target": "vohoanglong@gmail.com"
  } 
  Success Response Example
  {
    "success": true
  }
  Error Response Example
  {
    "status": 400,
    "message": "Two users have blocked each other"
  }
   ````

  -------------------------------------------------------------

* Retrieve all email addresses that can receive updates from an email address :GET  http://localhost:8080/api/v1/users/subscribed

````
  Params 
    * Key: sender
    * Value: lexuanhuy2k1@gmail.com
    * Key: text
    * Value: Hello World! nguyenthanhngan@example.com, nguyenthanhhang@gmail.com
  Success Response Example
  {
    "success": true,
    "recipients": [
        "nguyenvankhoa@gmail.com",
        "nguyenthanhngan@example.com",
        "nguyenthanhhang@gmail.com"
    ]
  }
  {
    "status": 400,
    "message": "Invalid email format"
  }
````


