from sqlalchemy import Column, String, Boolean, DateTime
from uuid import uuid4
from datetime import datetime
from .database import Base

class User(Base):
    __tablename__ = "users"

    id = Column(String, primary_key=True, index=True, default=lambda: str(uuid4()))
    name = Column(String, nullable=False)
    surname = Column(String, nullable=False)
    email = Column(String, unique=True, nullable=False)
    license_plate = Column(String, unique=True, nullable=False)
    phone_number = Column(String, nullable=True)
    password = Column(String, nullable=False)  # Hashed password
    created_at = Column(DateTime, default=datetime.utcnow)
    active = Column(Boolean, default=True)