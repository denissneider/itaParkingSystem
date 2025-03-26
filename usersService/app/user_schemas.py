from pydantic import BaseModel, EmailStr
from typing import Optional
from datetime import datetime

class UserCreate(BaseModel):
    name: str
    surname: str
    email: EmailStr
    license_plate: str
    phone_number: Optional[str]
    password: str

class UserOut(BaseModel):
    id: str
    name: str
    surname: str
    email: EmailStr
    license_plate: str
    phone_number: Optional[str]
    created_at: datetime
    active: bool

    class Config:
        orm_mode = True