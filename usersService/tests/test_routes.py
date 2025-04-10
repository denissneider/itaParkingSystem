from fastapi.testclient import TestClient
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from app.main import app
from app import models, database
from uuid import uuid4

# Set up test database
SQLALCHEMY_DATABASE_URL = "sqlite:///./test.db"
engine = create_engine(SQLALCHEMY_DATABASE_URL, connect_args={"check_same_thread": False})
TestingSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
models.Base.metadata.create_all(bind=engine)

def override_get_db():
    try:
        db = TestingSessionLocal()
        yield db
    finally:
        db.close()

app.dependency_overrides[database.get_db] = override_get_db
client = TestClient(app)

def test_get_users():
    response = client.get("/users")
    assert response.status_code == 200
    assert isinstance(response.json(), list)

def test_create_user():
    user_data = {
        "name": "Test",
        "surname": "User",
        "email": "testuser@example.com",
        "license_plate": "TEST123",
        "phone_number": "123456789",
        "password": "securepassword"
    }
    response = client.post("/users", json=user_data)
    assert response.status_code == 200
    assert response.json()["email"] == user_data["email"]

def test_create_duplicate_user():
    user_data = {
        "name": "Test",
        "surname": "User",
        "email": "duplicate@example.com",
        "license_plate": "DUP123",
        "phone_number": "987654321",
        "password": "securepassword"
    }
    client.post("/users", json=user_data)
    response = client.post("/users", json=user_data)
    assert response.status_code == 400
    assert response.json()["detail"] == "Email already registered"

def test_delete_user():
    user_data = {
        "name": "Delete",
        "surname": "Me",
        "email": "deleteme@example.com",
        "license_plate": "DEL123",
        "phone_number": "111222333",
        "password": "securepassword"
    }
    create_response = client.post("/users", json=user_data)
    user_id = create_response.json()["id"]

    delete_response = client.delete(f"/users/{user_id}")
    assert delete_response.status_code == 204

def test_delete_nonexistent_user():
    fake_id = uuid4()
    response = client.delete(f"/users/{fake_id}")
    assert response.status_code == 404
    assert response.json()["detail"] == "User not found"
