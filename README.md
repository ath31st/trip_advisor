## Trip Advisor (training project)

![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)

#### Project objectives:

1. Connect and use the database postgresql
2. Connect and use the database mongodb
3. Use two databases in one project at once
4. Implement authentication using JWT (access and refresh tokens)
5. Use a geocoder service to convert text addresses to latitude and longitude

#### Results:

Not all the tasks have been achieved. In particular, it was not possible to assemble the entire project in
docker-compose.
I couldn't find the reason, I had to leave the application running locally, and the databases in docker containers.
Also, there were problems with the selection of interesting places of the specified addresses.
Google does not give free API keys for its service.</br>
Despite some unachieved goals, I gained a lot of experience in implementing the following techniques:</br>

1. JWT authentication (access and refresh tokens)
2. Connecting several, different databases in one project
3. Yandex Geocoder service
4. Practice in development restfull-service

#### Endpoints:

User controller:

- POST api/user/register - register new user</br>
  Response: ````{"firstname":"Vasilii","lastname":"Morozov","password":"123456","email":"ok@org.ru"}````</br>
  Request: ````{"type": "Bearer","access token": "eyJ0eXAlsI...","refresh token": "eyJ0eXAMg1NDMxMz..."}````</br>
- POST api/user/change-password - change user password</br>
  Response: ````{"old password":"123456","new password":"1234567"}````</br>
  Request: ````{"status": "password successfully changed"}````</br>
- DELETE api/user/delete/{email} - delete user by email</br>
  Request: ````{"status": "User with username ok@org.ru successful deleted!"}````</br>
- GET api/user/users/ - return list registered users</br>
  Request: ````[{"id": 2,"firstname": "Vasilii","lastname": "Morozov","registerDate": "2022-10-16T23:19:44.639185","roles": ["ROLE_USER"],
  "authorities": [{"authority": "ROLE_USER"}],"email": "ok@org.ru"}]````</br>
- GET api/user/me - return info about auth user</br>
  Request: ````{"firstname": "Vasilii","lastname": "Morozov","username": "ok@org.ru","roles":
  ["ROLE_USER"],"register date": "2022-10-16T23:19:44.639185"}````</br>

Authentication controller:

- POST api/auth/login - return access and refresh tokens for auth user</br>
  Response: ````{"email": "ok@org.ru","password": "123456"}````</br>
  Request: ````{"type": "Bearer","access token": "eyJ0eXAlsI...","refresh token": "eyJ0eXAMg1NDMxMz..."}````</br>
- POST api/auth/token - return new access token after using refresh</br>
  Response: ````{"refresh token":"eyJ0eXAiObC..."}````</br>
  Request: ````{"type": "Bearer","access token": "eyJ0eXAiOA...","refresh token": null }````</br>
- POST api/auth/refresh - renew both tokens</br>
  Response: ````{"refresh token":"eyJ0eXAiOiJKV1Q..."}````</br>
  Request: ````{"type": "Bearer","access token": "eyJ0eXAiOiJKV...","refresh token": "eyJ0eXAiOiJjg..."}````</br>
- GET api/auth/logout - logout auth user (with deleting refresh token from DB)</br>
  Request: ````{"status": "You successfully logout!"}````</br>

Trip controller:

- POST api/trip/new - create new trip</br>
  Response: ````{"from": "Moscow","to": "Kazan","start date": "2022-12-29","duration": "5"}````</br>
  Request: ````{"status": "success, your route: -1304910735-Mo-Ka"}````</br>
- GET api/trip/route-info/{route} - return info about trip</br>
  Request: ````{"route name": "-1304910735-Mo-Ka","start date": "2022-12-29","distance (km)": 1277,"duration (days)": 5,
  "info locations": "Москва, Россия - Казань, Республика Татарстан, Россия"}````</br>
- GET api/trip/weather/{route} - return forecast for specified addresses</br>
  Request: ````[{
  "date": "2022-10-16",
  "location": "Москва, Россия",
  "minimum temperature": 6,
  "maximum temperature": 7
  }, {
  "date": "2022-10-17",
  "location": "Москва, Россия",
  "minimum temperature": 5,
  "maximum temperature": 8
  },...````</br>
- POST api/trip/change-duration/{route}/?duration= - change duration trip</br>
  Request: ````{"status": "Duration successfully changed on 7 days."}````</br>
- DELETE api/trip/delete-trip/{route} - delete trip</br>
  Request: ````{"status": "Trip with rout -1304910735-Mo-Ka successfully deleted!"}````</br>

#### List of used libraries:

1. Java JWT(auth0) - Java implementation of JSON Web Token (JWT)
2. Project Lombok - Automatic Resource Management, automatic generation of getters, setters, equals, hashCode and
   toString, and more.
3. PostgreSQL JDBC Driver - JDBC Driver
4. Spring Boot Starter Data MongoDB - Starter for using MongoDB document-oriented database and Spring Data MongoDB
5. Spring starters: Data JPA, Security, Web, Validation

