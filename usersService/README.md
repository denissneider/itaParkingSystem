# 🧑‍💻 Users Service – FastAPI Microservice

To je mikrostoritev za upravljanje uporabnikov v aplikaciji itaParkingSystem. Uporablja FastAPI kot ogrodje, PostgreSQL za bazo podatkov ter je pripravljena za zagon v Dockerju.

## Tehnologije
	•	Python 3.11
	•	FastAPI
	•	PostgreSQL
	•	SQLAlchemy
	•	Docker & Docker Compose

API Endpoints

--- Ustvari uporabnika

POST /users
Ustvari novega uporabnika.
Body (JSON):
{
  "name": "Janez",
  "surname": "Novak",
  "email": "janez@example.com",
  "license_plate": "MB123AB",
  "phone_number": "040123456",
  "password": "securepassword"
}

--- Pridobi vse uporabnike

GET /users
Vrne seznam vseh uporabnikov.

--- Izbriši uporabnika

DELETE /users/{user_id}
Izbriše uporabnika glede na njegov UUID.

Primer:
DELETE /users/ff05f4ea-aefa-46e5-b67f-f223306a70b8

Aplikacija bo dostopna na:
	•	http://localhost:8000

Swagger dokumentacija:
	•	http://localhost:8000/docs
